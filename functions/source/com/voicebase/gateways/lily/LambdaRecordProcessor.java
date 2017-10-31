package com.voicebase.gateways.lily;

import static com.voicebase.gateways.lily.VoiceBaseAttributeExtractor.getS3RecordingLocation;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.voicebase.sdk.MediaServiceV2;
import com.voicebase.sdk.VoiceBaseV2;
import com.voicebase.sdk.processing.MediaProcessingRequest;
import com.voicebase.sdk.util.NoAuthHeaderHttpClientRedirectStrategy;
import com.voicebase.sdk.util.RetrofitToSlf4jLogger;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.client.Client;
import retrofit.converter.JacksonConverter;

public class LambdaRecordProcessor extends LambdaProcessor implements RequestHandler<KinesisEvent, Void> {



  private static final Logger LOGGER = LoggerFactory.getLogger(LambdaRecordProcessor.class);
  
  private static final List<String> DEFAULT_CALLBACK_INCLUDES = Lists.newArrayList("metadata", "transcripts",
      "keywords", "topics", "predictions");

  private static final long DEFAULT_MEDIA_URL_TTL_MILLIS = 900000L; // 15min

  private final ObjectMapper objectMapper;
  private final MimetypesFileTypeMap mimeMap;
  private final AmazonS3 s3Client;
  private MediaServiceV2 mediaService = null;
  private String mediaServiceId = null;


  public LambdaRecordProcessor() {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(df);

    mimeMap = new MimetypesFileTypeMap();
    mimeMap.addMimeTypes("audio/mpeg mp3 mpeg3\naudio/ogg ogg\naudio/flac flac");

    s3Client = AmazonS3ClientBuilder.defaultClient();
  }

  @Override
  public Void handleRequest(KinesisEvent event, Context ctx) {

    if (event == null || event.getRecords() == null || event.getRecords().isEmpty()) {
      return null;
    }

    Map<String, String> env = System.getenv();

    doHandleRequest(event.getRecords(), env);
    return null;
  }

  /**
   * Actual request handling.
   * 
   * @param eventRecords
   *          records received from Kinesis
   * @param env
   *          environment
   */
  void doHandleRequest(List<KinesisEventRecord> eventRecords, Map<String, String> env) {
    configureLogging(env);
    
    boolean configureSpeakers = getBooleanSetting(env, Lambda.ENV_CONFIGURE_SPEAKERS, true);
    boolean predictionsEnabled = getBooleanSetting(env, Lambda.ENV_ENABLE_PREDICTIONS, true);
    String leftSpeakerName = getStringSetting(env, Lambda.ENV_LEFT_SPEAKER, Constants.DEFAULT_LEFT_SPEAKER_NAME);
    String rightSpeakerName = getStringSetting(env, Lambda.ENV_RIGHT_SPEAKER, Constants.DEFAULT_RIGHT_SPEAKER_NAME);
    long mediaUrlTtl = getLongSetting(env, Lambda.ENV_MEDIA_URL_TTL_MILLIS, DEFAULT_MEDIA_URL_TTL_MILLIS);

    String vbApiUrl = getStringSetting(env, Lambda.ENV_API_URL, Lambda.DEFAULT_API_URL);
    String vbApiClientLogLevel = getStringSetting(env, Lambda.ENV_API_CLIENT_LOGLEVEL, Lambda.DEFAULT_API_CLIENT_LOG_LEVEL);
    String vbApiToken = getStringSetting(env, Lambda.ENV_API_TOKEN, null);
    long vbApiRetryDelay = getLongSetting(env, Lambda.ENV_API_RETRY_DELAY, Lambda.DEFAULT_API_RETRY_DELAY);
    int vbApiRetryAttempts = getIntSetting(env, Lambda.ENV_API_RETRY_ATTEMPTS, Lambda.DEFAULT_API_RETRY_ATTEMPTS);

    String callbackUrl = getStringSetting(env, Lambda.ENV_CALLBACK_URL, null);
    String callbackMethod = getStringSetting(env, Lambda.ENV_CALLBACK_METHOD, Lambda.DEFAULT_CALLBACK_METHOD);
    List<String> callbackIncludes = getStringListSetting(env, Lambda.ENV_CALLBACK_INCLUDES, DEFAULT_CALLBACK_INCLUDES);

    CallbackProvider callbackProvider = createCallbackProvider(callbackUrl, callbackMethod, callbackIncludes);

    VoiceBaseV2 voicebaseClient = new VoiceBaseV2();
    voicebaseClient.setMediaService(createMediaServiceV2(vbApiUrl, vbApiClientLogLevel));
    voicebaseClient.setMimeMap(mimeMap);

    for (KinesisEventRecord recordEvent : eventRecords) {
      if (recordEvent != null) {

        try {

          Map<String, Object> dataAsMap = readKinesisRecord(recordEvent);
          LOGGER.debug("Msg received: {}", dataAsMap);

          String externalId = dataAsMap.get(Constants.KEY_EXTERNAL_ID).toString();

          VoicebaseRequestBuilder builder = new VoicebaseRequestBuilder().withCallbackProvider(callbackProvider)
              .withConfigureSpeakers(configureSpeakers).withPredictionsEnabled(predictionsEnabled)
              .withAwsInputData(dataAsMap).withLeftSpeakerName(leftSpeakerName).withRightSpeakerName(rightSpeakerName);

          builder.build();
          MediaProcessingRequest req = builder.getRequest();

          String s3Location = getS3RecordingLocation(dataAsMap);

          if (s3Location != null) {
            String parts[] = s3Location.split("/", 2);
            String preSignedUrl = createPresignedUrl(parts[0], parts[1], mediaUrlTtl);
            req.setMediaUrl(preSignedUrl);
          }

          String mediaId = voicebaseClient.uploadMedia(vbApiToken, req, vbApiRetryAttempts, vbApiRetryDelay);
          if (mediaId != null) {
            LOGGER.info("Call ID {} sent for processing; mediaId={}", externalId, mediaId);
          }
        } catch (SdkClientException | IllegalArgumentException e) {
          LOGGER.warn("Skipping record, unable to generate pre-signed URL.");
        } catch (IOException e) {
          LOGGER.error("Error sending media to VB API", e);
        }

      }
    }

  }

  /**
   * Get Kinesis record and deserialize to map.
   * 
   * @param recordEvent
   * 
   * @return Record deserialized into a map.
   * 
   * @throws IOException
   */
  Map<String, Object> readKinesisRecord(KinesisEventRecord recordEvent) throws IOException {
    ByteBuffer data = recordEvent.getKinesis().getData();
    byte[] bytes = new byte[data.remaining()];
    data.get(bytes, 0, data.remaining());

    Map<String, Object> dataAsMap = null;
    try {
      dataAsMap = objectMapper.readValue(bytes, Constants.MSG_JAVA_TYPE);
      LOGGER.debug("Msg received: {}", dataAsMap);
    } catch (IOException e) {
      LOGGER.error("Unable to deserialize Kinesis record.", e);
      throw e;
    }
    return dataAsMap;
  }

  /**
   * Create a pre-signed URL for given S3 bucket, object key and time to live.
   * 
   * @param bucketName
   *          S3 bucket containing the object
   * @param objectKey
   *          S3 object key
   * @param ttl
   *          time to live for the pre-signed URL.
   * 
   * @return pre-signed URL
   * 
   * @throws SdkClientException
   *           if pre-signing the URL failed.
   * @throws IllegalArgumentException
   *           if bucket name or object key is empty
   */
  private String createPresignedUrl(String bucketName, String objectKey, long ttl)
      throws SdkClientException, IllegalArgumentException {
    if (StringUtils.isEmpty(bucketName) || StringUtils.isEmpty(objectKey)) {
      LOGGER.error("Need bucket and key to create presigned URL. Bucket: {}, key: {}", bucketName, objectKey);
      throw new IllegalArgumentException("Bucket name or object key missing.");
    }

    long msec = System.currentTimeMillis() + ttl;

    Date expiration = new Date(msec);

    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
        .withMethod(HttpMethod.GET).withExpiration(expiration);

    try {
      URL s = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
      return s.toExternalForm();
    } catch (SdkClientException e) {
      LOGGER.error("Unable to generate pre-signed URL to call recording, bucket {}, key {}.", bucketName, objectKey, e);
      throw e;
    }
  }

  /**
   * Create Voicebase MediaService.
   * 
   * Will returned a cached instance of the service as long as endpoint URL and
   * log level don't change.
   * 
   * @param endpointUrl
   *          URL of Voicebase API endpoint
   * @param logLevel
   *          log level of underlying HTTP client
   * 
   * @return the media service
   */
  private MediaServiceV2 createMediaServiceV2(String endpointUrl, String logLevel) {

    String id = endpointUrl + logLevel;

    if (mediaService != null && Objects.equals(id, mediaServiceId)) {
      return mediaService;
    }

    LogLevel clientLogLevel = LogLevel.valueOf(logLevel);
    RetrofitToSlf4jLogger log = new RetrofitToSlf4jLogger(MediaServiceV2.class);

    Client client = new ApacheClient(
        HttpClientBuilder.create().setRedirectStrategy(new NoAuthHeaderHttpClientRedirectStrategy()).build());

    RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(endpointUrl).setClient(client)
        .setConverter(new JacksonConverter(objectMapper)).setLog(log).setLogLevel(clientLogLevel).build();

    MediaServiceV2 service = retrofit.create(MediaServiceV2.class);

    mediaService = service;
    mediaServiceId = id;

    return service;

  }

  private CallbackProvider createCallbackProvider(String callbackUrl, String callbackMethod,
      Iterable<String> includes) {

    CallbackProvider provider = new CallbackProvider();
    provider.setIncludes(includes);
    provider.setCallbackMethod(callbackMethod);
    provider.setCallbackUrl(callbackUrl);

    return provider;
  }

}

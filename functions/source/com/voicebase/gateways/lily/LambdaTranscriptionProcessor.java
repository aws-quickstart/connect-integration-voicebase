package com.voicebase.gateways.lily;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.voicebase.sdk.processing.CallbackResult;

public class LambdaTranscriptionProcessor extends LambdaProcessor
    implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponse> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LambdaTranscriptionProcessor.class);

  private static final String ENV_OUTPUT_STREAM = "VOICEBASE_TRANSCRIPT_OUTPUT_STREAM";
  private static final String ENV_CALLBACK_SOURCE_IPS = "VOICEBASE_CALLBACK_SOURCE_IPS";
  private static final String ENV_CALLBACK_SOURCE_IPS_VALIDATE = "VOICEBASE_CALLBACK_SOURCE_IPS_VALIDATE";

  private static final List<String> DEFAULT_SOURCE_IPS = Lists.newArrayList("52.6.224.43", "52.6.208.178",
      "52.2.171.140");
  private static final boolean DEFAULT_SOURCE_IPS_VALIDATE = true;

  private final APIGatewayProxyResponse responseUnauthorized;
  private final APIGatewayProxyResponse responseSuccess;
  private final APIGatewayProxyResponse responseInvalidRequest;
  private final APIGatewayProxyResponse responseServerError;

  private final ObjectMapper objectMapper;
  private final AmazonKinesis kinesisClient = AmazonKinesisClientBuilder.defaultClient();

  public LambdaTranscriptionProcessor() {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(df);

    String successResponse;
    String failureResponse;
    try {
      successResponse = objectMapper.writeValueAsString(ServiceResponse.SUCCESS);
    } catch (JsonProcessingException e) {
      // won't happen
      successResponse = "OK";
      LOGGER.error("Invalid service success response", e);
    }

    try {
      failureResponse = objectMapper.writeValueAsString(ServiceResponse.FAILURE);
    } catch (JsonProcessingException e) {
      // won't happen
      failureResponse = "FAILED";
      LOGGER.error("Invalid service failure response", e);
    }

    responseUnauthorized = new APIGatewayProxyResponse().withStatusCode(401).withBody(failureResponse);
    responseSuccess = new APIGatewayProxyResponse().withStatusCode(200).withBody(successResponse);
    responseInvalidRequest = new APIGatewayProxyResponse().withStatusCode(406).withBody(failureResponse);
    responseServerError = new APIGatewayProxyResponse().withStatusCode(500).withBody(failureResponse);

  }

  @Override
  public APIGatewayProxyResponse handleRequest(APIGatewayProxyRequestEvent input, Context context) {
    if (input == null || input.getBody() == null || StringUtils.isEmpty(input.getBody())) {
      return responseInvalidRequest;
    }

    Map<String, String> env = System.getenv();

    if (!validateRequestSource(input, env)) {
      return responseUnauthorized;
    }

    try {
      doHandleRequest(input.getBody(), env);
    } catch (Exception e) {
      LOGGER.error("Error sending transcript to stream", e);
      return responseServerError;
    }

    return responseSuccess;
  }

  void doHandleRequest(String processingResult, Map<String, String> env) {
    configureLogging(env);
    
    byte[] msgData = createOutdata(processingResult);
    String mediaId = null;
    String externalId = null;

    try {
      CallbackResult result = objectMapper.readValue(processingResult.getBytes(), CallbackResult.class);
      mediaId = result.getMedia().getMediaId();
      externalId = result.getMedia().getMetadata().getExternal().getId();
    } catch (Exception e) {
    }

    LOGGER.info("Transcript for call ID {}, media ID {} received.", externalId, mediaId);

    if (msgData != null) {
      String partitionKey = "1";
      if (externalId != null) {
        partitionKey = externalId;
      } else if (mediaId != null) {
        partitionKey = mediaId;
      }
      String outputStream = getOutputStreamName(env);
      kinesisClient.putRecord(new PutRecordRequest().withStreamName(outputStream).withData(ByteBuffer.wrap(msgData))
          .withPartitionKey(partitionKey));
      LOGGER.info("Transcript for call ID {}, media ID {} sent to {}", externalId, mediaId, outputStream);
      LOGGER.trace("VB API processing result: {}", processingResult);
    } else {
      LOGGER.warn("No usable data received, not writing output to stream");
    }

  }

  /**
   * Check incoming request against source IP whitelist if there is one.
   * <p/>
   * Right now only accepts IP addresses on the whitelist, may want to extend to
   * process CIDRs.
   * 
   * @param input
   *          incoming event
   * @param env
   *          this function's environment
   * 
   * @return true if passed IP check or there is no whitelist, false otherwise
   */
  boolean validateRequestSource(APIGatewayProxyRequestEvent input, Map<String, String> env) {
    boolean validate = getBooleanSetting(env, ENV_CALLBACK_SOURCE_IPS_VALIDATE, DEFAULT_SOURCE_IPS_VALIDATE);
    if (!validate) {
      return true;
    }

    List<String> callbackIps = getStringListSetting(env, ENV_CALLBACK_SOURCE_IPS, DEFAULT_SOURCE_IPS);

    if (callbackIps != null && !callbackIps.isEmpty()) {
      try {
        String sourceIp = input.getRequestContext().getIdentity().getSourceIp();
        LOGGER.debug("Incoming request from {}", sourceIp);
        if (StringUtils.isEmpty(sourceIp) || !callbackIps.contains(sourceIp)) {
          LOGGER.warn("Request from {} not authorized, rejecting.", sourceIp);
          return false;
        }
      } catch (Exception e) {
        LOGGER.error("Unable to validate request source.", e);
        return false;
      }
    }

    return true;
  }

  /**
   * Extract output stream name from environment.
   * <p/>
   * Right now expects the env variable VOICEBASE_TRANSCRIPT_OUTPUT_STREAM to
   * contain a stream name. May want to extend this to also extract the stream
   * name from an ARN.
   * 
   * @param env
   *          this function's environment
   * 
   * @return the output stream name.
   * 
   * @see #ENV_OUTPUT_STREAM
   */
  String getOutputStreamName(Map<String, String> env) {
    return getStringSetting(env, ENV_OUTPUT_STREAM, null);
  }

  private byte[] createOutdata(String processingResult) {
    if (processingResult == null) {
      return null;
    }

    try {
      // deserialize/serialize to remove any existing JSON formatting
      Map<?, ?> deserialized = objectMapper.readValue(processingResult.getBytes(), Map.class);
      return objectMapper.writeValueAsBytes(deserialized);
    } catch (Exception e) {
      LOGGER.warn("Unable to deserialize/serialize response, sending original", e);
    }

    return processingResult.getBytes();
  }

}

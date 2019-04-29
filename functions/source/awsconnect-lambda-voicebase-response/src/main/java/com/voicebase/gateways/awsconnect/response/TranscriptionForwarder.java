/**
 * Copyright 2016-2019 Amazon.com, Inc. or its affiliates. All Rights Reserved. Licensed under the
 * Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.voicebase.gateways.awsconnect.response;

import static com.voicebase.gateways.awsconnect.ConfigUtil.getStringSetting;

import com.amazonaws.services.kinesis.AmazonKinesis;
import com.amazonaws.services.kinesis.AmazonKinesisClientBuilder;
import com.amazonaws.services.kinesis.model.PutRecordRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.voicebase.gateways.awsconnect.AmazonConnect;
import com.voicebase.gateways.awsconnect.ConfigUtil;
import com.voicebase.gateways.awsconnect.Metrics;
import com.voicebase.gateways.awsconnect.Metrics.Dimension;
import com.voicebase.gateways.awsconnect.Metrics.Name;
import com.voicebase.gateways.awsconnect.MetricsCollector;
import com.voicebase.gateways.awsconnect.VoiceBaseAttributeExtractor;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.v3client.JacksonFactory;
import com.voicebase.v3client.datamodel.VbCallbackFormatEnum;
import com.voicebase.v3client.datamodel.VbMedia;
import com.voicebase.v3client.datamodel.VbTranscriptFormat;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author Volker Kueffel <volker@voicebase.com> */
public class TranscriptionForwarder {

  private static final Logger LOGGER = LoggerFactory.getLogger(TranscriptionForwarder.class);
  static final int KINESIS_MAX_MSG_SIZE = 2097152; // 2 MB

  private static final List<VbCallbackFormatEnum> FORMAT_REMOVAL_LIST =
      Lists.newArrayList(
          VbCallbackFormatEnum.DFXP,
          VbCallbackFormatEnum.SRT,
          VbCallbackFormatEnum.WEBVTT,
          VbCallbackFormatEnum.TEXT,
          VbCallbackFormatEnum.JSON);

  private MetricsCollector metricsCollector;
  private String transcriptOutputStream;
  private boolean addNewlineToOutput = false;

  private ObjectMapper objectMapper;
  private final AmazonKinesis kinesisClient;
  private boolean trimTranscriptIfTooLarge;

  public TranscriptionForwarder(Map<String, String> env) {
    kinesisClient = AmazonKinesisClientBuilder.defaultClient();
    configure(env);
  }

  public void setMetricsCollector(MetricsCollector metricsCollector) {
    this.metricsCollector = metricsCollector;
  }

  public TranscriptionForwarder withMetricsCollector(MetricsCollector metricsCollector) {
    setMetricsCollector(metricsCollector);
    return this;
  }

  public void forward(String processingResult) {

    String mediaId = null;
    String externalId = null;
    String instanceId = null;

    VbMedia result = null;
    try {
      result = objectMapper.readValue(processingResult.getBytes(), VbMedia.class);
      mediaId = result.getMediaId();
      if (result.getMetadata() != null) {
        externalId = result.getMetadata().getExternalId();
        Map<String, Object> meta = result.getMetadata().getExtended();
        instanceId = AmazonConnect.getInstanceId(meta);
        publishRetrievalMetrics(meta);
      }
      if (result.getStatus() != null) {
        pushCounterMetric(
            Name.VoicebaseResultReceived,
            1,
            Pair.of(Dimension.InstanceId, instanceId),
            Pair.of(Dimension.ProcessingStatus, result.getStatus().name()));
      }
    } catch (Exception e) {
      LOGGER.debug("Problem accessing mediaId/externalId", e);
    }

    LOGGER.info("Transcript for call ID {}, media ID {} received.", externalId, mediaId);

    if (transcriptOutputStream != null) {
      byte[] msgData = createOutdata(instanceId, processingResult);
      if (msgData != null) {
        if (msgData.length > KINESIS_MAX_MSG_SIZE) {
          throw new IllegalArgumentException("Message too large to send to the stream");
        }

        String partitionKey = "1";
        if (externalId != null) {
          partitionKey = externalId;
        } else if (mediaId != null) {
          partitionKey = mediaId;
        }
        try {

          kinesisClient.putRecord(
              new PutRecordRequest()
                  .withStreamName(transcriptOutputStream)
                  .withData(ByteBuffer.wrap(msgData))
                  .withPartitionKey(partitionKey));
          LOGGER.debug(
              "Transcript for call ID {}, media ID {} sent to {}",
              externalId,
              mediaId,
              transcriptOutputStream);
          increment(Name.VoicebaseResultSendSuccess, instanceId);
          LOGGER.trace("VB API processing result: {}", processingResult);
        } catch (IllegalArgumentException e) {
          increment(Name.VoicebaseResultSendFailed, instanceId);
          LOGGER.error("{}. Skipping.", e.getMessage());
        } catch (Exception e) {
          increment(Name.VoicebaseResultSendFailed, instanceId);
          // re-throw to let the callback try again
          throw e;
        }
      } else {
        LOGGER.warn("No usable data received, not writing output to stream");
      }
    }
  }

  private byte[] createOutdata(String instanceId, String processingResult) {
    if (processingResult == null) {
      return null;
    }

    // reduce size if necessary
    byte[] msgBytes = truncateIfNecessary(instanceId, processingResult.getBytes());

    try {
      // deserialize/serialize to remove any existing JSON formatting
      if (!addNewlineToOutput) {
        return msgBytes;
      } else {
        Map<?, ?> deserialized = objectMapper.readValue(msgBytes, Map.class);
        return (objectMapper.writeValueAsString(deserialized) + "\n").getBytes();
      }
    } catch (Exception e) {
      LOGGER.warn("Unable to deserialize/serialize response, sending original", e);
    }

    if (addNewlineToOutput) {
      return (new String(msgBytes) + "\n").getBytes();
    } else {
      return msgBytes;
    }
  }

  byte[] truncateIfNecessary(String instanceId, byte[] msgBytes) {
    if (!trimTranscriptIfTooLarge || msgBytes.length < KINESIS_MAX_MSG_SIZE) {
      return msgBytes;
    }
    boolean truncated = false;
    try {
      VbMedia media = objectMapper.readValue(msgBytes, VbMedia.class);
      for (VbCallbackFormatEnum format : FORMAT_REMOVAL_LIST) {
        if (removeTranscriptFormat(media, format)) {
          LOGGER.warn("Removed transcript format {} from media ID {}", format, media.getMediaId());
          truncated = true;
          msgBytes = objectMapper.writeValueAsBytes(media);
          if (msgBytes.length < KINESIS_MAX_MSG_SIZE) {
            break;
          }
        }
      }
    } catch (Exception e) {
      LOGGER.warn("Error occured when trying to reduce message size, aborting result trimming");
    }
    if (truncated) {
      increment(Name.VoicebaseResultTrimmed, instanceId);
    }
    return msgBytes;
  }

  boolean removeTranscriptFormat(VbMedia payload, VbCallbackFormatEnum format) {
    if (payload == null || format == null) {
      return false;
    }
    boolean removed = false;
    if (payload.getTranscript() != null) {
      if (format == VbCallbackFormatEnum.JSON) {
        payload.getTranscript().setWords(null);
        removed = true;
      } else if (payload.getTranscript().getAlternateFormats() != null) {

        List<VbTranscriptFormat> formats = new ArrayList<>();
        for (VbTranscriptFormat transcriptFormat : payload.getTranscript().getAlternateFormats()) {
          if (StringUtils.equalsIgnoreCase(transcriptFormat.getFormat(), format.getValue())) {
            removed = true;
          } else {
            formats.add(transcriptFormat);
          }
        }
        if (removed) {
          payload.getTranscript().setAlternateFormats(formats);
        }
      }
    }
    return removed;
  }

  private void publishRetrievalMetrics(Map<String, Object> extendedMeta) {
    if (extendedMeta != null && metricsCollector != null) {
      String instanceId = AmazonConnect.getInstanceId(extendedMeta);
      increment(Name.VoicebaseResultReceived, instanceId);

      Long elapsed = VoiceBaseAttributeExtractor.getTimeElapsedSinceReceived(extendedMeta);
      if (elapsed != null) {
        Map<String, String> dims = null;
        if (!StringUtils.isBlank(instanceId)) {
          dims = Collections.singletonMap(Dimension.InstanceId.getName(), instanceId);
        }
        metricsCollector.addTiming(
            Name.VoicebaseTurnAroundTime.getName(), elapsed, TimeUnit.SECONDS, dims);
      }
    }
  }

  private void increment(Metrics.Name metricName, String instanceId) {
    if (instanceId != null) {
      pushCounterMetric(metricName, 1, Pair.of(Metrics.Dimension.InstanceId, instanceId));
    } else {
      pushCounterMetric(metricName, 1);
    }
  }

  @SafeVarargs
  private final void pushCounterMetric(
      Metrics.Name metricName, Number metricValue, Pair<Metrics.Dimension, String>... dimensions) {
    if (metricsCollector != null) {
      if (metricName != null) {
        Map<String, String> dims = null;
        if (dimensions != null) {
          dims =
              Stream.of(dimensions)
                  .collect(Collectors.toMap(p -> p.getLeft().getName(), Pair::getRight));
        }
        metricsCollector.addCount(metricName.getName(), metricValue, dims);
      }
    }
  }

  void configure(Map<String, String> env) {
    objectMapper = JacksonFactory.objectMapper();
    transcriptOutputStream = getStringSetting(env, Lambda.ENV_TRANSCRIPT_OUTPUT_STREAM, null);
    if (transcriptOutputStream == null) {
      LOGGER.info("No output stream, just registering VoiceBase response.");
    }
    addNewlineToOutput =
        ConfigUtil.getBooleanSetting(
            env,
            Lambda.ENV_TRANSCRIPT_OUTPUT_ADD_NEWLINE,
            Lambda.DEFAULT_TRANSCRIPT_OUTPUT_ADD_NEWLINE);
    trimTranscriptIfTooLarge =
        ConfigUtil.getBooleanSetting(
            env,
            Lambda.ENV_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE,
            Lambda.DEFAULT_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE);
  }
}

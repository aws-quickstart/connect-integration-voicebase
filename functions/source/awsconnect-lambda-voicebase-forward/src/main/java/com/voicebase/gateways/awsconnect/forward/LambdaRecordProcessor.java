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
package com.voicebase.gateways.awsconnect.forward;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent;
import com.amazonaws.services.lambda.runtime.events.KinesisEvent.KinesisEventRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.gateways.awsconnect.BeanFactory;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.gateways.awsconnect.lambda.LambdaHandler;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lambda function to retrieve Amazon Connect output from a Kinesis stream and send a request to the
 * VoiceBase API for processing.
 *
 * @author Volker Kueffel <volker@voicebase.com>
 */
public class LambdaRecordProcessor extends LambdaHandler
    implements RequestHandler<KinesisEvent, Void> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LambdaRecordProcessor.class);

  private ObjectMapper objectMapper;
  private RecordingForwarder forwarder;

  public LambdaRecordProcessor() {
    this(System.getenv());
  }

  LambdaRecordProcessor(Map<String, String> env) {
    super(env);
  }

  @Override
  protected void configure(Map<String, String> env) {
    objectMapper = BeanFactory.objectMapper();
    forwarder = new RecordingForwarder(env).withMetricsCollector(getMetricsCollector());
  }

  @Override
  public Void handleRequest(KinesisEvent event, Context ctx) {

    if (event == null || event.getRecords() == null || event.getRecords().isEmpty()) {
      return null;
    }

    for (KinesisEventRecord recordEvent : event.getRecords()) {
      if (recordEvent != null) {
        try {
          Map<String, Object> ctrAsMap = readKinesisRecord(recordEvent);
          forwarder.forwardRequest(ctrAsMap);
        } catch (Exception e) {
          LOGGER.error("Error sending media to VB API", e);
        }
      }
    }
    flushMetrics();
    return null;
  }

  /**
   * Get Kinesis record and deserialize to map.
   *
   * @param recordEvent
   * @return Record deserialized into a map.
   * @throws IOException
   */
  Map<String, Object> readKinesisRecord(KinesisEventRecord recordEvent) throws IOException {
    ByteBuffer data = recordEvent.getKinesis().getData();
    byte[] bytes = new byte[data.remaining()];
    data.get(bytes, 0, data.remaining());

    Map<String, Object> dataAsMap = null;
    try {
      dataAsMap = objectMapper.readValue(bytes, Lambda.MSG_JAVA_TYPE);
      LOGGER.debug("Msg received: {}", dataAsMap);
    } catch (IOException e) {
      LOGGER.error("Unable to deserialize Kinesis record.", e);
      throw e;
    }
    return dataAsMap;
  }
}

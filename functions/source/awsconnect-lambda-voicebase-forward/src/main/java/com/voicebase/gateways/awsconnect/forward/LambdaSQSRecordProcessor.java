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
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.gateways.awsconnect.BeanFactory;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.gateways.awsconnect.lambda.LambdaHandler;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lambda function to retrieve Amazon Connect output from a SQS stream and send a request to the
 * VoiceBase API for processing.
 */
public class LambdaSQSRecordProcessor extends LambdaHandler
    implements RequestHandler<SQSEvent, Void> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LambdaSQSRecordProcessor.class);

  private ObjectMapper objectMapper;
  private RecordingForwarder forwarder;

  public LambdaSQSRecordProcessor() {
    this(System.getenv());
  }

  LambdaSQSRecordProcessor(Map<String, String> env) {
    super(env);
  }

  @Override
  protected void configure(Map<String, String> env) {
    objectMapper = BeanFactory.objectMapper();
    forwarder = new RecordingForwarder(env).withMetricsCollector(getMetricsCollector());
  }

  @Override
  public Void handleRequest(SQSEvent event, Context ctx) {
    if (event == null || event.getRecords() == null || event.getRecords().isEmpty()) {
      return null;
    }
    for (SQSEvent.SQSMessage recordEvent : event.getRecords()) {
      if (recordEvent != null) {
        try {
          Map<String, Object> ctrAsMap = readSQSRecord(recordEvent);
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
   * Get SQS record and deserialize to map.
   *
   * @param recordEvent
   * @return Record deserialized into a map.
   * @throws IOException
   */
  Map<String, Object> readSQSRecord(SQSEvent.SQSMessage recordEvent) throws IOException {
    String jsonEvent = recordEvent.getBody();
    Map<String, Object> dataAsMap = null;
    try {
      dataAsMap = objectMapper.readValue(jsonEvent, Lambda.MSG_JAVA_TYPE);
      LOGGER.debug("Msg received: {}", dataAsMap);
    } catch (IOException e) {
      LOGGER.error("Unable to deserialize SQS record.", e);
      throw e;
    }
    return dataAsMap;
  }
}

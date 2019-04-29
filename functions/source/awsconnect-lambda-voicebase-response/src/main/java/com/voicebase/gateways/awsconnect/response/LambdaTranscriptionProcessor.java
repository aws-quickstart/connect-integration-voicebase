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

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.gateways.awsconnect.BeanFactory;
import com.voicebase.gateways.awsconnect.RequestSourceValidator;
import com.voicebase.gateways.awsconnect.lambda.LambdaHandler;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lambda function to retrieve response from the VoiceBase API callback servers and sending it to a
 * Kinesis stream.
 *
 * @author Volker Kueffel <volker@voicebase.com>
 */
public class LambdaTranscriptionProcessor extends LambdaHandler
    implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LambdaTranscriptionProcessor.class);

  private APIGatewayProxyResponseEvent responseUnauthorized;
  private APIGatewayProxyResponseEvent responseSuccess;
  private APIGatewayProxyResponseEvent responseInvalidRequest;
  private APIGatewayProxyResponseEvent responseServerError;

  private ObjectMapper objectMapper;
  private TranscriptionForwarder forwarder;
  private RequestSourceValidator requestSourceValidator;

  public LambdaTranscriptionProcessor() {
    this(System.getenv());
  }

  LambdaTranscriptionProcessor(Map<String, String> env) {
    super(env);
  }

  @Override
  public APIGatewayProxyResponseEvent handleRequest(
      APIGatewayProxyRequestEvent input, Context context) {
    if (input == null || input.getBody() == null || StringUtils.isEmpty(input.getBody())) {
      return responseInvalidRequest;
    }

    if (!validateRequestSource(input)) {
      return responseUnauthorized;
    }

    try {
      forwarder.forward(input.getBody());
    } catch (Exception e) {
      LOGGER.error("Error forwarding transcript to stream", e);
      return responseServerError;
    }

    flushMetrics();
    return responseSuccess;
  }

  /**
   * Check incoming request against source IP whitelist if there is one.
   *
   * <p>Right now only accepts IP addresses on the whitelist, may want to extend to process CIDRs.
   *
   * @param input incoming event
   * @param env this function's environment
   * @return true if passed IP check or there is no whitelist, false otherwise
   */
  boolean validateRequestSource(APIGatewayProxyRequestEvent input) {
    String sourceIp = null;
    if (input.getRequestContext() != null && input.getRequestContext().getIdentity() != null) {
      sourceIp = input.getRequestContext().getIdentity().getSourceIp();
    }

    return requestSourceValidator.validate(sourceIp);
  }

  @Override
  protected void configure(Map<String, String> env) {
    objectMapper = BeanFactory.objectMapper();
    forwarder = new TranscriptionForwarder(env).withMetricsCollector(getMetricsCollector());
    requestSourceValidator = BeanFactory.requestSourceValidator(env);

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

    responseUnauthorized =
        new APIGatewayProxyResponseEvent().withStatusCode(401).withBody(failureResponse);
    responseSuccess =
        new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(successResponse);
    responseInvalidRequest =
        new APIGatewayProxyResponseEvent().withStatusCode(406).withBody(failureResponse);
    responseServerError =
        new APIGatewayProxyResponseEvent().withStatusCode(500).withBody(failureResponse);
  }
}

/**
 * Copyright 2016-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not
 * use this file except in compliance with the License. A copy of the License is
 * located at
 *
 *      http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file
 * accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.voicebase.gateways.awsconnect.response;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.gateways.awsconnect.BeanFactory;
import com.voicebase.gateways.awsconnect.ConfigUtil;
import com.voicebase.gateways.awsconnect.RequestSourceValidator;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.gateways.awsconnect.lambda.LambdaProcessor;

/**
 * Lambda function to retrieve response from the VoiceBase API callback servers
 * and sending it to a Kinesis stream.
 *
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class LambdaTranscriptionProcessor extends LambdaProcessor
    implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final Logger LOGGER = LoggerFactory.getLogger(LambdaTranscriptionProcessor.class);

  private final APIGatewayProxyResponseEvent responseUnauthorized;
  private final APIGatewayProxyResponseEvent responseSuccess;
  private final APIGatewayProxyResponseEvent responseInvalidRequest;
  private final APIGatewayProxyResponseEvent responseServerError;

  private final ObjectMapper objectMapper;
  private TranscriptionForwarder forwarder;
  private RequestSourceValidator requestSourceValidator;

  public LambdaTranscriptionProcessor() {
    this(System.getenv());
  }

  LambdaTranscriptionProcessor(Map<String, String> env) {

    objectMapper = BeanFactory.objectMapper();

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

    responseUnauthorized = new APIGatewayProxyResponseEvent().withStatusCode(401).withBody(failureResponse);
    responseSuccess = new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(successResponse);
    responseInvalidRequest = new APIGatewayProxyResponseEvent().withStatusCode(406).withBody(failureResponse);
    responseServerError = new APIGatewayProxyResponseEvent().withStatusCode(500).withBody(failureResponse);

    configure(env);
  }

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
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

    return responseSuccess;
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
  boolean validateRequestSource(APIGatewayProxyRequestEvent input) {
    String sourceIp = null;
    if (input.getRequestContext() != null && input.getRequestContext().getIdentity() != null) {
      sourceIp = input.getRequestContext().getIdentity().getSourceIp();
    }

    return requestSourceValidator.validate(sourceIp);
  }

  protected void configure(Map<String, String> env) {
    configureLogging(env);
    forwarder = new TranscriptionForwarder(env);

    boolean validate = ConfigUtil.getBooleanSetting(env, Lambda.ENV_CALLBACK_SOURCE_IPS_VALIDATE,
        Lambda.DEFAULT_SOURCE_IPS_VALIDATE);
    List<String> callbackIps = ConfigUtil.getStringListSetting(env, Lambda.ENV_CALLBACK_SOURCE_IPS,
        Lambda.DEFAULT_SOURCE_IPS);
    requestSourceValidator = new RequestSourceValidator(callbackIps, validate);
  }
}

/**
 * Copyright 2016-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved. Licensed under the
 * Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 * 
 * http://aws.amazon.com/apache2.0/
 * 
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 */
package com.voicebase.gateways.lambda;

import static com.voicebase.gateways.awsconnect.ConfigUtil.getStringSetting;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.gateways.awsconnect.BeanFactory;
import com.voicebase.gateways.awsconnect.lambda.CustomResourceRequest;
import com.voicebase.gateways.awsconnect.lambda.CustomResourceResponse;
import com.voicebase.gateways.awsconnect.lambda.CustomResourceResponse.Status;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.gateways.awsconnect.lambda.LambdaProcessor;
import com.voicebase.sdk.v3.ServiceFactory;
import com.voicebase.sdk.v3.VoiceBaseClient;

/**
 * Lambda function to be used as custom resource to validate the provided Voicebase API token.
 * 
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class LambdaVoicebaseApiTokenValidator extends LambdaProcessor
    implements RequestHandler<InputStream, Void> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(LambdaVoicebaseApiTokenValidator.class);
  private static final ObjectMapper OM = new ObjectMapper();

  private final VoiceBaseClient voicebaseClient;
  private String vbApiToken;


  public LambdaVoicebaseApiTokenValidator() {
    this(System.getenv());
  }

  LambdaVoicebaseApiTokenValidator(Map<String, String> env) {
    voicebaseClient = BeanFactory.voicebaseClient();
    configure(env);
  }

  @Override
  public Void handleRequest(InputStream rawRequest, Context context) {

    // AWS OM doesn't honor annotations, need to deserialize here due to upper case property names
    CustomResourceRequest request;
    try {
      request = OM.readValue(rawRequest, CustomResourceRequest.class);
      doHandleRequest(request, context);
    } catch (Exception e) {
      LOGGER.error("Unable to read custom resource request", e);
    }

    return null;
  }


  private void doHandleRequest(CustomResourceRequest request, Context context) {
    LOGGER.info("Received request: {}", request);

    String requestType = request.getRequestType();

    CustomResourceResponse response = new CustomResourceResponse();
    response.setPhysicalResourceId(DigestUtils.md5Hex(vbApiToken));
    response.setNoEcho(false);

    if (StringUtils.equalsIgnoreCase("Create", requestType)
        || StringUtils.equalsIgnoreCase("Update", requestType)) {
      validateToken(request, response);
    } else {
      LOGGER.info("Nothing to do for {} requests", requestType);
      response.setStatus(Status.SUCCESS);
      response.setReason("No-Op");
    }

    if (!sendResponse(request, response)) {
      throw new RuntimeException("Unable to send response.");
    } ;
  }


  private void validateToken(CustomResourceRequest request, CustomResourceResponse response) {
    LOGGER.info("Validation VoiceBase API token...");
    try {
      voicebaseClient.getResources(vbApiToken);
      response.setStatus(Status.SUCCESS);
      response.setReason("VoiceBase API token valid.");
      LOGGER.info("Token valid.");
    } catch (Exception e) {
      response.setStatus(Status.FAILED);
      response.setReason(e.getMessage());
      LOGGER.info("Token invalid.");
    }
  }



  protected boolean sendResponse(CustomResourceRequest request, CustomResourceResponse response) {
    response.setStackId(request.getStackId());
    response.setRequestId(request.getRequestId());
    response.setLogicalResourceId(request.getLogicalSourceId());

    try {
      URL url = new URL(request.getResponseURL());
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoOutput(true);
      connection.setRequestMethod("PUT");
      OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
      String output = OM.writeValueAsString(response);
      LOGGER.info("Returning CF response: {}", output);
      out.write(output);
      out.close();
      int responseCode = connection.getResponseCode();
      LOGGER.info("Response Code: " + responseCode);
      return true;
    } catch (Exception e) {
      LOGGER.error("Error sending response", e);
      return false;
    }

  }


  protected void configure(Map<String, String> env) {
    configureLogging(env);
    vbApiToken = getStringSetting(env, Lambda.ENV_API_TOKEN, null);

    String vbApiUrl = getStringSetting(env, Lambda.ENV_API_URL, Lambda.DEFAULT_V3_API_URL);
    String vbApiClientLogLevel =
        getStringSetting(env, Lambda.ENV_API_CLIENT_LOGLEVEL, Lambda.DEFAULT_API_CLIENT_LOG_LEVEL);

    voicebaseClient
        .setVoicebaseService(ServiceFactory.voiceBaseService(vbApiUrl, vbApiClientLogLevel));

  }


}

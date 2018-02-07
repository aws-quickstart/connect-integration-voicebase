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
package com.voicebase.gateways.lambda;

import static com.voicebase.gateways.awsconnect.ConfigUtil.getStringSetting;

import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.voicebase.gateways.awsconnect.BeanFactory;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.gateways.awsconnect.lambda.LambdaProcessor;
import com.voicebase.sdk.v3.ServiceFactory;
import com.voicebase.sdk.v3.VoiceBaseClient;

/**
 * Lambda function to be used as custom resource to validate the provided
 * Voicebase API token.
 *
 * TODO: Only stub right now.
 *
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class LambdaVoicebaseApiTokenValidator extends LambdaProcessor implements RequestHandler<Object, Void> {

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
  public Void handleRequest(Object input, Context context) {

    try {
      voicebaseClient.getResources(vbApiToken);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid VoiceBase token");
    }
    return null;
  }

  protected void configure(Map<String, String> env) {
    configureLogging(env);
    vbApiToken = getStringSetting(env, Lambda.ENV_API_TOKEN, null);

    String vbApiUrl = getStringSetting(env, Lambda.ENV_API_URL, Lambda.DEFAULT_V3_API_URL);
    String vbApiClientLogLevel = getStringSetting(env, Lambda.ENV_API_CLIENT_LOGLEVEL,
        Lambda.DEFAULT_API_CLIENT_LOG_LEVEL);

    voicebaseClient.setVoicebaseService(ServiceFactory.voiceBaseService(vbApiUrl, vbApiClientLogLevel));

  }

}

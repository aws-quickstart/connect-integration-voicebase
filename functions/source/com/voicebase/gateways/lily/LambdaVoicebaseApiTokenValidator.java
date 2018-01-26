package com.voicebase.gateways.lily;

import java.util.Map;

import org.apache.http.impl.client.HttpClientBuilder;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.sdk.MediaServiceV2;
import com.voicebase.sdk.VoiceBaseServiceV2;
import com.voicebase.sdk.VoiceBaseV2;
import com.voicebase.sdk.util.NoAuthHeaderHttpClientRedirectStrategy;
import com.voicebase.sdk.util.RetrofitToSlf4jLogger;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.client.Client;
import retrofit.converter.JacksonConverter;

/**
 * Copyright 2017-2018 VoiceBase, Inc. or its affiliates. All Rights Reserved.
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
 * @author volker@voicebase.com
 *
 */
public class LambdaVoicebaseApiTokenValidator extends LambdaProcessor implements RequestHandler<Object,Void> {

  @Override
  public Void handleRequest(Object input, Context context) {

    Map<String, String> env = System.getenv();

    doHandleRequest(env);
    return null;
  }

  private void doHandleRequest(Map<String, String> env) {
    String vbApiUrl = getStringSetting(env, Lambda.ENV_API_URL, Lambda.DEFAULT_V2_API_URL);
    String vbApiClientLogLevel = getStringSetting(env, Lambda.ENV_API_CLIENT_LOGLEVEL, Lambda.DEFAULT_API_CLIENT_LOG_LEVEL);
    String vbApiToken = getStringSetting(env, Lambda.ENV_API_TOKEN, null);
    
    VoiceBaseV2 voicebaseClient = new VoiceBaseV2();
    voicebaseClient.setVoicebaseService(createVoiceBaseServiceV2(vbApiUrl, vbApiClientLogLevel));

    try {
      voicebaseClient.getResources(vbApiToken);
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid VoiceBase token");
    }
    
  }
  
  private VoiceBaseServiceV2 createVoiceBaseServiceV2(String endpointUrl, String logLevel) {

    LogLevel clientLogLevel = LogLevel.valueOf(logLevel);
    RetrofitToSlf4jLogger log = new RetrofitToSlf4jLogger(MediaServiceV2.class);

    Client client = new ApacheClient(
        HttpClientBuilder.create().setRedirectStrategy(new NoAuthHeaderHttpClientRedirectStrategy()).build());

    RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(endpointUrl).setClient(client)
        .setConverter(new JacksonConverter(new ObjectMapper())).setLog(log).setLogLevel(clientLogLevel).build();

    return retrofit.create(VoiceBaseServiceV2.class);

  }

}

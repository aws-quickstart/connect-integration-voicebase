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

public class LambdaVoicebaseApiTokenValidator extends LambdaProcessor implements RequestHandler<Object,Void> {

  @Override
  public Void handleRequest(Object input, Context context) {

    Map<String, String> env = System.getenv();

    doHandleRequest(env);
    return null;
  }

  private void doHandleRequest(Map<String, String> env) {
    String vbApiUrl = getStringSetting(env, Lambda.ENV_API_URL, Lambda.DEFAULT_API_URL);
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

package com.voicebase.sdk;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.sdk.util.NoAuthHeaderHttpClientRedirectStrategy;
import com.voicebase.sdk.util.RetrofitToSlf4jLogger;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.converter.JacksonConverter;

@Configuration
public class VoicebaseClientConfiguration {

  @Inject
  private ObjectMapper objectMapper;

  @Bean
  public VoiceBaseV2 voicebaseV2Client() {
    return new VoiceBaseV2();
  }

  @Bean
  public MimetypesFileTypeMap mimeMap() {
    MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap();
    mimeMap.addMimeTypes("audio/mpeg mp3 mpeg3\naudio/ogg ogg\naudio/flac flac");
    return mimeMap;
  }

  @Bean
  public MediaServiceV2 mediaServiceV2(RestAdapter retrofitRestAdapter) {
    return retrofitRestAdapter.create(MediaServiceV2.class);

  }

  @Bean
  public VoiceBaseServiceV2 voicebaseService(RestAdapter retrofitRestAdapter) {
    return retrofitRestAdapter.create(VoiceBaseServiceV2.class);
  }

  
  @Bean
  public RestAdapter retrofitRestAdapter(@Value("${com.voicebase.api.v2.url}") String endpointUrl,
      @Value("${com.voicebase.api.v2.log-level:BASIC}") String logLevel) {
    
    LogLevel clientLogLevel = LogLevel.valueOf(logLevel);
    RetrofitToSlf4jLogger log = new RetrofitToSlf4jLogger(MediaServiceV2.class);

    ApacheClient client=new ApacheClient(
        HttpClientBuilder.create().setRedirectStrategy(new NoAuthHeaderHttpClientRedirectStrategy()).build());
    
    RestAdapter retrofit = new RestAdapter.Builder().setEndpoint(endpointUrl).setClient(client)
        .setConverter(new JacksonConverter(objectMapper)).setLog(log).setLogLevel(clientLogLevel).build();
    
    return retrofit;
    
  }
}

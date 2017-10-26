package com.voicebase.sdk;

import java.util.Map;

import retrofit.http.GET;
import retrofit.http.Header;

public interface VoiceBaseServiceV2 {
  @GET("/")
  public Map<String, ?> getResources(@Header("Authorization") String authorization);
}

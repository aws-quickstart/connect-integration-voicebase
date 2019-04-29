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
package com.voicebase.sdk.v3;

import com.voicebase.sdk.util.ApiErrorHandler;
import com.voicebase.sdk.util.NoAuthHeaderHttpClientRedirectStrategy;
import com.voicebase.sdk.util.RetrofitToSlf4jLogger;
import com.voicebase.v3client.JacksonFactory;
import javax.activation.MimetypesFileTypeMap;
import org.apache.http.impl.client.HttpClientBuilder;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.converter.JacksonConverter;

/** @author Volker Kueffel <volker@voicebase.com> */
public class ServiceFactory {

  public static final VoiceBaseClient voicebaseClient(String endpointUrl, String logLevel) {
    VoiceBaseClient voicebaseClient = new VoiceBaseClient();
    voicebaseClient.setMimeMap(mimetypesFileTypeMap());
    voicebaseClient.setMediaService(mediaService(endpointUrl, logLevel));
    voicebaseClient.setVoicebaseService(voiceBaseService(endpointUrl, logLevel));
    return voicebaseClient;
  }

  public static MediaService mediaService(String endpointUrl, String logLevel) {
    if (endpointUrl == null) {
      return null;
    }

    RetrofitToSlf4jLogger log = new RetrofitToSlf4jLogger(MediaService.class);

    RestAdapter.Builder retrofit =
        new RestAdapter.Builder()
            .setEndpoint(endpointUrl)
            .setClient(apacheClient())
            .setConverter(new JacksonConverter(JacksonFactory.objectMapper()))
            .setLog(log)
            .setErrorHandler(new ApiErrorHandler());

    if (logLevel != null) {
      LogLevel clientLogLevel = LogLevel.valueOf(logLevel);
      if (clientLogLevel != null) {
        retrofit.setLogLevel(clientLogLevel);
      }
    }

    MediaService service = retrofit.build().create(MediaService.class);

    return service;
  }

  public static VoiceBaseService voiceBaseService(String endpointUrl, String logLevel) {

    RetrofitToSlf4jLogger log = new RetrofitToSlf4jLogger(VoiceBaseService.class);

    RestAdapter.Builder retrofit =
        new RestAdapter.Builder()
            .setEndpoint(endpointUrl)
            .setClient(apacheClient())
            .setConverter(new JacksonConverter(JacksonFactory.objectMapper()))
            .setLog(log)
            .setErrorHandler(new ApiErrorHandler());

    if (logLevel != null) {
      LogLevel clientLogLevel = LogLevel.valueOf(logLevel);
      if (clientLogLevel != null) {
        retrofit.setLogLevel(clientLogLevel);
      }
    }

    return retrofit.build().create(VoiceBaseService.class);
  }

  private static ApacheClient apacheClient() {
    return new ApacheClient(
        HttpClientBuilder.create()
            .setRedirectStrategy(new NoAuthHeaderHttpClientRedirectStrategy())
            .build());
  }

  private static final MimetypesFileTypeMap mimetypesFileTypeMap() {
    MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap();
    mimeMap.addMimeTypes("audio/mpeg mp3 mpeg3\naudio/ogg ogg\naudio/flac flac");
    return mimeMap;
  }
}

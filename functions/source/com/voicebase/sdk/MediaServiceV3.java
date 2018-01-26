package com.voicebase.sdk;

import com.voicebase.api.model.VbConfiguration;
import com.voicebase.api.model.VbMedia;
import com.voicebase.api.model.VbMetadata;

import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

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
public interface MediaServiceV3 {

  @Multipart
  @POST("/media")
  public VbMedia processMedia(@Header("Authorization") String authorization,
      @Part("configuration") VbConfiguration configuration, @Part("metadata") VbMetadata metadata,
      @Part("media") TypedFile media);

  @Multipart
  @POST("/media")
  public VbMedia processMedia(@Header("Authorization") String authorization,
      @Part("configuration") VbConfiguration configuration, @Part("metadata") VbMetadata metadata,
      @Part("mediaUrl") String mediaUrl);

  @Multipart
  @POST("/media/{mediaId}")
  public VbMedia updateMedia(@Header("Authorization") String authorization, @Path("mediaId") String mediaId,
      @Part("configuration") VbConfiguration configuration, @Part("metadata") VbMetadata metadata,
      @Part("media") TypedFile media);

  @Multipart
  @POST("/media/{mediaId}")
  public VbMedia updateMedia(@Header("Authorization") String authorization, @Path("mediaId") String mediaId,
      @Part("configuration") VbConfiguration configuration, @Part("metadata") VbMetadata metadata,
      @Part("mediaUrl") String mediaUrl);
}

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

import com.voicebase.v3client.datamodel.VbConfiguration;
import com.voicebase.v3client.datamodel.VbMedia;
import com.voicebase.v3client.datamodel.VbMetadata;
import java.io.IOException;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.mime.TypedFile;

/** @author Volker Kueffel <volker@voicebase.com> */
public interface MediaService {

  @Multipart
  @POST("/media")
  public VbMedia processMedia(
      @Header("Authorization") String authorization,
      @Part("configuration") VbConfiguration configuration,
      @Part("metadata") VbMetadata metadata,
      @Part("media") TypedFile media)
      throws IOException;

  @Multipart
  @POST("/media")
  public VbMedia processMedia(
      @Header("Authorization") String authorization,
      @Part("configuration") VbConfiguration configuration,
      @Part("metadata") VbMetadata metadata,
      @Part("mediaUrl") String mediaUrl)
      throws IOException;

  @Multipart
  @POST("/media/{mediaId}")
  public VbMedia updateMedia(
      @Header("Authorization") String authorization,
      @Path("mediaId") String mediaId,
      @Part("configuration") VbConfiguration configuration,
      @Part("metadata") VbMetadata metadata,
      @Part("media") TypedFile media)
      throws IOException;

  @Multipart
  @POST("/media/{mediaId}")
  public VbMedia updateMedia(
      @Header("Authorization") String authorization,
      @Path("mediaId") String mediaId,
      @Part("configuration") VbConfiguration configuration,
      @Part("metadata") VbMetadata metadata,
      @Part("mediaUrl") String mediaUrl)
      throws IOException;
}

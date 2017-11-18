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

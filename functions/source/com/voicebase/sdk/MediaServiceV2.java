package com.voicebase.sdk;

import java.util.List;

import com.voicebase.sdk.processing.ConfigurationWrapper;
import com.voicebase.sdk.processing.ListMediaResult;
import com.voicebase.sdk.processing.MediaProcessingResult;
import com.voicebase.sdk.processing.MetadataWrapper;

import retrofit.client.Response;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

public interface MediaServiceV2 {

	@GET("/media")
	public ListMediaResult getMediaList(@Header("Authorization") String authorization, @Query("limit") Integer limit,
			@Query("sortOrder") SortOrder sortOrder, @Query("include") List<Includes> include,
			@Query("after") String mediaId, @Query("filter.created.gte") String createdAfterDateString);

	@Multipart
	@POST("/media")
	public MediaProcessingResult processMedia(@Header("Authorization") String authorization,
			@Part("configuration") ConfigurationWrapper configurationWrapper,
			@Part("metadata") MetadataWrapper metadataWrapper, @Part("media") TypedFile media);
	
	 @Multipart
	  @POST("/media")
	  public MediaProcessingResult processMedia(@Header("Authorization") String authorization,
	      @Part("configuration") ConfigurationWrapper configurationWrapper,
	      @Part("metadata") MetadataWrapper metadataWrapper, @Part("mediaUrl") String mediaUrl);

	@GET("/media/{mediaId}")
	public Response getMediaInfo(@Header("Authorization") String token, @Path("mediaId") String mediaId);

	@DELETE("/media/{mediaId}")
	public Response deleteMedia(@Header("Authorization") String token, @Path("mediaId") String mediaId);

	@Streaming
	@GET("/media/{mediaId}/streams/original")
	public Response getMedia(@Header("Authorization") String token, @Path("mediaId") String mediaId);

}

package com.voicebase.sdk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.voicebase.sdk.processing.ConfigurationWrapper;
import com.voicebase.sdk.processing.ListMediaResult;
import com.voicebase.sdk.processing.MediaProcessingRequest;
import com.voicebase.sdk.processing.MediaProcessingResult;
import com.voicebase.sdk.processing.MetadataWrapper;
import com.voicebase.sdk.util.IOUtil;

import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class VoiceBaseV2 {

  private static final Logger LOGGER = LoggerFactory.getLogger(VoiceBaseV2.class);

  @Inject
  private MimetypesFileTypeMap mimeMap;

  @Inject
  private DateFormat dateFormat;

  @Inject
  private MediaServiceV2 mediaService;
  
  @Inject
  private VoiceBaseServiceV2 voicebaseService;

  public void setMimeMap(MimetypesFileTypeMap mimeMap) {
    this.mimeMap = mimeMap;
  }

  public void setMediaService(MediaServiceV2 mediaService) {
    this.mediaService = mediaService;
  }

  public void setVoicebaseService(VoiceBaseServiceV2 voicebaseService) {
    this.voicebaseService = voicebaseService;
  }

  
  public Map<String, ?> getResources(String token) {
    return voicebaseService.getResources(authHeaderValue(token));
  }
  
  
  public String uploadMedia(String token, MediaProcessingRequest request) throws IOException {

    MediaProcessingResult result = null;
    
    LOGGER.debug("Voicebase upload request: {}", request);

    if (request.getMediaFile() != null) {
      TypedFile file = new TypedFile(mimeMap.getContentType(request.getMediaFile()), request.getMediaFile());
      result = mediaService.processMedia(authHeaderValue(token), new ConfigurationWrapper(request.getConfiguration()),
          new MetadataWrapper(request.getMetadata()), file);
    } else if (request.getMediaUrl() != null) {
      result = mediaService.processMedia(authHeaderValue(token), new ConfigurationWrapper(request.getConfiguration()),
          new MetadataWrapper(request.getMetadata()), request.getMediaUrl());
    }

    String mediaId = null;

    if (result != null) {
      mediaId = result.getMediaId();
    }

    LOGGER.trace("Voicebase response: {}", result);
    LOGGER.debug("Voicebase media ID: {}", mediaId);

    return mediaId;

  }

  public String uploadMedia(String token, MediaProcessingRequest request, int retryAttempts, long retryDelay)
      throws IOException {

    int attempt = 0;
    boolean success = false;
    String mediaId = null;

    do {
      attempt++;
      try {
        mediaId = uploadMedia(token, request);
        success = true;
      } catch (Exception e) {
        // retry
        if (attempt < 1 + retryAttempts) {
          try {
            LOGGER.warn("Error calling VB API, retrying in {}ms", retryDelay, e);
            Thread.sleep(retryDelay);
          } catch (InterruptedException e1) {
            throw new RuntimeException(e1);
          }
        } else {
          if (e instanceof IOException) {
            throw (IOException) e;
          } else {
            throw new IOException(e);
          }
        }
      }
    } while (!success);

    return mediaId;
  }

  public ListMediaResult getMediaList(String token, Integer limit, SortOrder sortOrder, List<Includes> includes,
      String lastMediaId, Date createdAfter) throws IOException {

    String createdAfterStr = null;
    if (createdAfter != null) {
      createdAfterStr = dateFormat.format(new DateTime(createdAfter, DateTimeZone.UTC).plusMillis(1).toDate());
    }

    ListMediaResult response = mediaService.getMediaList(authHeaderValue(token), limit, sortOrder, includes,
        lastMediaId, createdAfterStr);

    LOGGER.trace("Media list response: {}", response);

    return response;
  }

  public void deleteMedia(String token, String mediaId) throws IOException, IllegalArgumentException {
    Preconditions.checkArgument(mediaId != null, "Need mediaId to delete media file");
    mediaService.deleteMedia(authHeaderValue(token), mediaId);
  }

  public void getMediaInfo(String token, String mediaId) throws IOException, IllegalArgumentException {
    Preconditions.checkArgument(mediaId != null, "Need mediaId to fetch media information");
    mediaService.getMediaInfo(authHeaderValue(token), mediaId);
  }

  public File downloadMedia(String token, String mediaId, String fileExtension)
      throws IOException, IllegalArgumentException {
    Preconditions.checkArgument(mediaId != null, "Need mediaId to download media file");

    File media = null;
    Response response = mediaService.getMedia(authHeaderValue(token), mediaId);

    try (InputStream in = response.getBody().in()) {
      media = IOUtil.writeToTempFile(in, mediaId, fileExtension);
    }
    return media;
  }

  private String authHeaderValue(String token) {
    return "Bearer " + token;
  }

}

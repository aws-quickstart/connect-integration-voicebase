package com.voicebase.sdk;

import java.io.IOException;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voicebase.api.model.VbMedia;
import com.voicebase.sdk.v3.MediaProcessingRequest;

import retrofit.mime.TypedFile;

public class VoiceBaseV3 {

  private static final Logger LOGGER = LoggerFactory.getLogger(VoiceBaseV3.class);

  @Inject
  private MediaServiceV3 mediaService;

  @Inject
  private MimetypesFileTypeMap mimeMap;

  public void setMediaService(MediaServiceV3 mediaService) {
    this.mediaService = mediaService;
  }

  public void setMimeMap(MimetypesFileTypeMap mimeMap) {
    this.mimeMap = mimeMap;
  }

  public String uploadMedia(String token, MediaProcessingRequest request) {
    VbMedia result = null;
    if (request.getMediaFile() != null) {
      TypedFile file = new TypedFile(mimeMap.getContentType(request.getMediaFile()), request.getMediaFile());

      result = mediaService.processMedia(authHeaderValue(token), request.getConfiguration(), request.getMetadata(),
          file);
    } else {
      result = mediaService.processMedia(authHeaderValue(token), request.getConfiguration(), request.getMetadata(),
          request.getMediaUrl());
    }

    LOGGER.trace("Voicebase response: {}", result);

    String mediaId = null;

    if (result != null) {
      mediaId = result.getMediaId();
    }
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

  public boolean updateMedia(String token, String mediaId, MediaProcessingRequest request) {
    VbMedia result = null;
    if (request.getMediaFile() != null) {
      TypedFile file = new TypedFile(mimeMap.getContentType(request.getMediaFile()), request.getMediaFile());

      result = mediaService.updateMedia(authHeaderValue(token), mediaId, request.getConfiguration(),
          request.getMetadata(), file);
    } else {
      result = mediaService.updateMedia(authHeaderValue(token), mediaId, request.getConfiguration(),
          request.getMetadata(), request.getMediaUrl());
    }

    LOGGER.trace("Voicebase response: {}", result);

    if (result != null && StringUtils.equals(result.getMediaId(), mediaId)) {
      return true;
    }

    return false;
  }

  public String updateMedia(String token, String mediaId, MediaProcessingRequest request, int retryAttempts,
      long retryDelay) throws IOException {

    int attempt = 0;
    boolean success = false;

    do {
      attempt++;
      try {
        success = updateMedia(token, mediaId, request);
        if (!success) {
          throw new IOException("Call to Voiebase not successful.");
        }
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

  private String authHeaderValue(String token) {
    return "Bearer " + token;
  }

}

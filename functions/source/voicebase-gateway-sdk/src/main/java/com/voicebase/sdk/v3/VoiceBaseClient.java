/**
 * Copyright 2016-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
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
 */
package com.voicebase.sdk.v3;

import java.io.IOException;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voicebase.api.model.VbMedia;

import retrofit.mime.TypedFile;

/**
 *
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class VoiceBaseClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(VoiceBaseClient.class);

  @Inject
  private MediaService mediaService;

  @Inject
  private VoiceBaseService voicebaseService;


  @Inject
  private MimetypesFileTypeMap mimeMap;

  public void setMediaService(MediaService mediaService) {
    this.mediaService = mediaService;
  }

  public void setVoicebaseService(VoiceBaseService voicebaseService) {
    this.voicebaseService = voicebaseService;
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


  public Map<String, ?> getResources(String token) {
    return voicebaseService.getResources(authHeaderValue(token));
  }

  private String authHeaderValue(String token) {
    return "Bearer " + token;
  }

}

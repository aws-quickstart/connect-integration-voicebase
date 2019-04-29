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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.sdk.util.ApiException;
import com.voicebase.v3client.JacksonFactory;
import com.voicebase.v3client.datamodel.VbMedia;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.activation.MimetypesFileTypeMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit.mime.TypedFile;

/** @author Volker Kueffel <volker@voicebase.com> */
public class VoiceBaseClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(VoiceBaseClient.class);
  private static final ObjectMapper OM = JacksonFactory.objectMapper();

  private MediaService mediaService;

  private VoiceBaseService voicebaseService;

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

  public String uploadMedia(String token, MediaProcessingRequest request)
      throws IOException, ApiException, IllegalArgumentException {
    VbMedia result = null;

    LOGGER.debug("Sending request to VoiceBase API: {}", request);

    if (LOGGER.isTraceEnabled()) {
      try {
        LOGGER.trace("VB configuration: {}", OM.writeValueAsString(request.getConfiguration()));
      } catch (Exception e) {
        LOGGER.trace("Unable to serialize VB configuration");
      }
    }

    try {

      if (request.getMediaFile() != null) {
        TypedFile file =
            new TypedFile(mimeMap.getContentType(request.getMediaFile()), request.getMediaFile());

        result =
            mediaService.processMedia(
                authHeaderValue(token), request.getConfiguration(), request.getMetadata(), file);
      } else if (request.getMediaUrl() != null) {
        result =
            mediaService.processMedia(
                authHeaderValue(token),
                request.getConfiguration(),
                request.getMetadata(),
                request.getMediaUrl());
      } else {
        throw new IllegalArgumentException("Media information missing.");
      }

      LOGGER.trace("Voicebase response: {}", result);
    } catch (ApiException e) {
      if (e.getError() != null) {
        LOGGER.debug("VoiceBase API returned an error: {}", e.getError());
      }
      throw e;
    }

    String mediaId = null;

    if (result != null) {
      mediaId = result.getMediaId();
    }
    return mediaId;
  }

  public String uploadMedia(
      String token, MediaProcessingRequest request, int retryAttempts, long retryDelay)
      throws IOException, ApiException, IllegalArgumentException {

    return executeWithRetry(() -> uploadMedia(token, request), retryAttempts, retryDelay);
  }

  public boolean updateMedia(String token, String mediaId, MediaProcessingRequest request)
      throws IOException, ApiException, IllegalArgumentException {
    VbMedia result = null;
    if (request.getMediaFile() != null) {
      TypedFile file =
          new TypedFile(mimeMap.getContentType(request.getMediaFile()), request.getMediaFile());

      result =
          mediaService.updateMedia(
              authHeaderValue(token),
              mediaId,
              request.getConfiguration(),
              request.getMetadata(),
              file);
    } else {
      result =
          mediaService.updateMedia(
              authHeaderValue(token),
              mediaId,
              request.getConfiguration(),
              request.getMetadata(),
              request.getMediaUrl());
    }

    LOGGER.trace("Voicebase response: {}", result);

    if (result != null && StringUtils.equals(result.getMediaId(), mediaId)) {
      return true;
    }

    return false;
  }

  public String updateMedia(
      String token,
      String mediaId,
      MediaProcessingRequest request,
      int retryAttempts,
      long retryDelay)
      throws IOException, ApiException, IllegalArgumentException {

    boolean success =
        executeWithRetry(() -> updateMedia(token, mediaId, request), retryAttempts, retryDelay);

    return success ? mediaId : null;
  }

  private <T> T executeWithRetry(Callable<T> function, int retryAttempts, long retryDelay)
      throws IOException, ApiException, IllegalArgumentException {

    int attempt = 0;
    boolean keepTrying = true;
    T result = null;

    do {
      attempt++;
      try {
        result = function.call();
        keepTrying = false;
      } catch (ApiException | IllegalArgumentException e) {
        if (isRetryable(e)) {
          keepTrying = delayRetry(attempt, retryAttempts, retryDelay);
        } else {
          throw e;
        }
      } catch (Exception e) {
        if (isRetryable(e)) {
          keepTrying = delayRetry(attempt, retryAttempts, retryDelay);
        } else {
          throw new IOException(e);
        }
      }
    } while (keepTrying);

    return result;
  }

  boolean isRetryable(Exception e) {
    if (e instanceof ApiException) {
      return ((ApiException) e).isRetryable();
    } else if (e instanceof IllegalArgumentException) {
      return false;
    }
    return true;
  }

  private boolean delayRetry(int attempt, int retryAttempts, long retryDelay) throws IOException {
    if (attempt < 1 + retryAttempts) {
      try {
        LOGGER.warn("Error calling VB API, retrying in {}ms", retryDelay);
        Thread.sleep(retryDelay);
        return true;
      } catch (InterruptedException e1) {
        throw new RuntimeException(e1);
      }
    }
    return false;
  }

  public Map<String, ?> getResources(String token) {
    return voicebaseService.getResources(authHeaderValue(token));
  }

  private String authHeaderValue(String token) {
    return "Bearer " + token;
  }
}

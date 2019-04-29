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
package com.voicebase.sdk.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.v3client.JacksonFactory;
import com.voicebase.v3client.datamodel.VbErrorResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/** @author Volker Kueffel <volker@voicebase.com> */
public class ApiErrorHandler implements ErrorHandler {

  private static final ObjectMapper OM = JacksonFactory.objectMapper();

  /*
   * (non-Javadoc)
   *
   * @see retrofit.ErrorHandler#handleError(retrofit.RetrofitError)
   */
  @Override
  public Throwable handleError(RetrofitError error) {
    String message;
    VbErrorResponse errorResponse = null;
    switch (error.getKind()) {
      case NETWORK:
        message = "Network error: " + error.getMessage();
        break;
      case HTTP:
        message = error.getMessage();
        // maybe add VbErrorResponse object to exception
        if (error.getResponse() != null && error.getResponse().getBody() != null) {
          String body = null;
          try {
            body = IOUtil.readToString(error.getResponse().getBody().in());
            if (!StringUtils.isBlank(body)) {
              try {
                errorResponse = OM.readValue(body, VbErrorResponse.class);
              } catch (Exception e) {
                // can't parse API response, silently skip for now
              }
            }
          } catch (IOException e) {
            // can't get response body, skip
          }
          message += ": " + body;
        }
        break;
      case CONVERSION:
        message = "Conversion error: " + error.getMessage();
        break;
      case UNEXPECTED:
        message = "Unexpected error: " + error.getMessage();
        break;
      default:
        message = "Unknown error: " + error.getMessage();
        break;
    }

    ApiException apiError = new ApiException(message).withError(errorResponse);
    if (error.getResponse() != null) {
      apiError.withStatusCode(error.getResponse().getStatus());
    }
    return apiError;
  }
}

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
 * @author volker@voicebase.com
 *
 */
package com.voicebase.sdk.util;

import java.io.IOException;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;

/**
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class ApiErrorHandler implements ErrorHandler {

  /*
   * (non-Javadoc)
   * 
   * @see retrofit.ErrorHandler#handleError(retrofit.RetrofitError)
   */
  @Override
  public Throwable handleError(RetrofitError error) {
    String message;
    switch (error.getKind()) {
      case NETWORK:
        message = "Network error: " + error.getMessage();
        break;
      case HTTP:
        message = error.getMessage();
        // maybe add VbErrorResponse object to exception
        if (error.getResponse() != null && error.getResponse().getBody() != null) {
          try {
            message += ": " + IOUtil.readToString(error.getResponse().getBody().in());
          } catch (IOException e) {
          }
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

    return new ApiException(message);

  }

}

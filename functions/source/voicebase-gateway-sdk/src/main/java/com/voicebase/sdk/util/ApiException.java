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

import com.voicebase.v3client.datamodel.VbErrorResponse;

/** @author Volker Kueffel <volker@voicebase.com> */
public class ApiException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private int statusCode;
  private VbErrorResponse error;

  public ApiException() {
    super();
  }

  public ApiException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public ApiException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApiException(String message) {
    super(message);
  }

  public ApiException(Throwable cause) {
    super(cause);
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public ApiException withStatusCode(int statusCode) {
    setStatusCode(statusCode);
    return this;
  }

  public VbErrorResponse getError() {
    return error;
  }

  public void setError(VbErrorResponse error) {
    this.error = error;
  }

  public ApiException withError(VbErrorResponse error) {
    setError(error);
    return this;
  }

  public boolean isRetryable() {
    return statusCode != 400;
  }
}

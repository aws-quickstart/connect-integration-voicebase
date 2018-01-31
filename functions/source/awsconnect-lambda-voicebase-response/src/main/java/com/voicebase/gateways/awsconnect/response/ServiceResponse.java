/**
 * Copyright 2017-2018 VoiceBase, Inc. or its affiliates. All Rights Reserved.
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
package com.voicebase.gateways.awsconnect.response;

/**
 * Response object to be returned to the VoiceBase API callback servers.
 * 
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public final class ServiceResponse {

  public static final ServiceResponse SUCCESS = new ServiceResponse().withSuccess(true);
  public static final ServiceResponse FAILURE = new ServiceResponse().withSuccess(false);

  private boolean success;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public ServiceResponse withSuccess(boolean success) {
    setSuccess(success);
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (success ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ServiceResponse other = (ServiceResponse) obj;
    if (success != other.success)
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ServiceResponse [success=");
    builder.append(success);
    builder.append("]");
    return builder.toString();
  }

}
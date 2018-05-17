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
package com.voicebase.gateways.awsconnect.lambda;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class CustomResourceResponse {
  public static enum Status {
    SUCCESS, FAILED
  }

  @JsonProperty("Status")
  private Status status;
  @JsonProperty("Reason")
  private String reason;
  @JsonProperty("StackId")
  private String stackId;
  @JsonProperty("RequestId")
  private String requestId;
  @JsonProperty("PhysicalResourceId")
  private String physicalResourceId;
  @JsonProperty("LogicalResourceId")
  private String logicalResourceId;
  @JsonProperty("NoEcho")
  private boolean noEcho;
  @JsonProperty("Data")
  private Map<String, Object> data;

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public String getStackId() {
    return stackId;
  }

  public void setStackId(String stackId) {
    this.stackId = stackId;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getPhysicalResourceId() {
    return physicalResourceId;
  }

  public void setPhysicalResourceId(String physicalResourceId) {
    this.physicalResourceId = physicalResourceId;
  }

  public String getLogicalResourceId() {
    return logicalResourceId;
  }

  public void setLogicalResourceId(String logicalResourceId) {
    this.logicalResourceId = logicalResourceId;
  }

  public boolean isNoEcho() {
    return noEcho;
  }

  public void setNoEcho(boolean noEcho) {
    this.noEcho = noEcho;
  }

  public Map<String, Object> getData() {
    return data;
  }

  public void setData(Map<String, Object> data) {
    this.data = data;
  }


  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((data == null) ? 0 : data.hashCode());
    result = prime * result + ((logicalResourceId == null) ? 0 : logicalResourceId.hashCode());
    result = prime * result + (noEcho ? 1231 : 1237);
    result = prime * result + ((physicalResourceId == null) ? 0 : physicalResourceId.hashCode());
    result = prime * result + ((reason == null) ? 0 : reason.hashCode());
    result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
    result = prime * result + ((stackId == null) ? 0 : stackId.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
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
    CustomResourceResponse other = (CustomResourceResponse) obj;
    if (data == null) {
      if (other.data != null)
        return false;
    } else if (!data.equals(other.data))
      return false;
    if (logicalResourceId == null) {
      if (other.logicalResourceId != null)
        return false;
    } else if (!logicalResourceId.equals(other.logicalResourceId))
      return false;
    if (noEcho != other.noEcho)
      return false;
    if (physicalResourceId == null) {
      if (other.physicalResourceId != null)
        return false;
    } else if (!physicalResourceId.equals(other.physicalResourceId))
      return false;
    if (reason == null) {
      if (other.reason != null)
        return false;
    } else if (!reason.equals(other.reason))
      return false;
    if (requestId == null) {
      if (other.requestId != null)
        return false;
    } else if (!requestId.equals(other.requestId))
      return false;
    if (stackId == null) {
      if (other.stackId != null)
        return false;
    } else if (!stackId.equals(other.stackId))
      return false;
    if (status != other.status)
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("CustomResourceResponse [status=");
    builder.append(status);
    builder.append(", reason=");
    builder.append(reason);
    builder.append(", stackId=");
    builder.append(stackId);
    builder.append(", requestId=");
    builder.append(requestId);
    builder.append(", physicalResourceId=");
    builder.append(physicalResourceId);
    builder.append(", logicalResourceId=");
    builder.append(logicalResourceId);
    builder.append(", noEcho=");
    builder.append(noEcho);
    builder.append(", data=");
    builder.append(data);
    builder.append("]");
    return builder.toString();
  }

}

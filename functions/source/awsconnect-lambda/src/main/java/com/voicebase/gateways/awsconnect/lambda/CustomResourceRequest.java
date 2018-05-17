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
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.ALWAYS)
public class CustomResourceRequest {

  @JsonProperty("RequestType")
  private String requestType;
  @JsonProperty("ResponseURL")
  private String responseURL;
  @JsonProperty("StackId")
  private String stackId;
  @JsonProperty("RequestId")
  private String requestId;
  @JsonProperty("ResourceType")
  private String resourceType;
  @JsonProperty("LogicalResourceId")
  private String logicalSourceId;
  @JsonProperty("ResourceProperties")
  private Map<String, Object> resourceProperties;
  @JsonProperty("OldResourceProperties")
  private Map<String,Object> oldResourceProperties;
  
  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public String getResponseURL() {
    return responseURL;
  }

  public void setResponseURL(String responseURL) {
    this.responseURL = responseURL;
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

  public void setRequestId(String requestID) {
    this.requestId = requestID;
  }

  public String getResourceType() {
    return resourceType;
  }

  public void setResourceType(String resourceType) {
    this.resourceType = resourceType;
  }

  public String getLogicalSourceId() {
    return logicalSourceId;
  }

  public void setLogicalSourceId(String logicalSourceId) {
    this.logicalSourceId = logicalSourceId;
  }

  public Map<String, Object> getResourceProperties() {
    return resourceProperties;
  }

  public void setResourceProperties(Map<String, Object> resourceProperties) {
    this.resourceProperties = resourceProperties;
  }

  public Map<String, Object> getOldResourceProperties() {
    return oldResourceProperties;
  }

  public void setOldResourceProperties(Map<String, Object> oldResourceProperties) {
    this.oldResourceProperties = oldResourceProperties;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((logicalSourceId == null) ? 0 : logicalSourceId.hashCode());
    result =
        prime * result + ((oldResourceProperties == null) ? 0 : oldResourceProperties.hashCode());
    result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
    result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
    result = prime * result + ((resourceProperties == null) ? 0 : resourceProperties.hashCode());
    result = prime * result + ((resourceType == null) ? 0 : resourceType.hashCode());
    result = prime * result + ((responseURL == null) ? 0 : responseURL.hashCode());
    result = prime * result + ((stackId == null) ? 0 : stackId.hashCode());
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
    CustomResourceRequest other = (CustomResourceRequest) obj;
    if (logicalSourceId == null) {
      if (other.logicalSourceId != null)
        return false;
    } else if (!logicalSourceId.equals(other.logicalSourceId))
      return false;
    if (oldResourceProperties == null) {
      if (other.oldResourceProperties != null)
        return false;
    } else if (!oldResourceProperties.equals(other.oldResourceProperties))
      return false;
    if (requestId == null) {
      if (other.requestId != null)
        return false;
    } else if (!requestId.equals(other.requestId))
      return false;
    if (requestType == null) {
      if (other.requestType != null)
        return false;
    } else if (!requestType.equals(other.requestType))
      return false;
    if (resourceProperties == null) {
      if (other.resourceProperties != null)
        return false;
    } else if (!resourceProperties.equals(other.resourceProperties))
      return false;
    if (resourceType == null) {
      if (other.resourceType != null)
        return false;
    } else if (!resourceType.equals(other.resourceType))
      return false;
    if (responseURL == null) {
      if (other.responseURL != null)
        return false;
    } else if (!responseURL.equals(other.responseURL))
      return false;
    if (stackId == null) {
      if (other.stackId != null)
        return false;
    } else if (!stackId.equals(other.stackId))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("CustomResourceRequest [requestType=");
    builder.append(requestType);
    builder.append(", responseURL=");
    builder.append(responseURL);
    builder.append(", stackId=");
    builder.append(stackId);
    builder.append(", requestID=");
    builder.append(requestId);
    builder.append(", resourceType=");
    builder.append(resourceType);
    builder.append(", logicalSourceId=");
    builder.append(logicalSourceId);
    builder.append(", resourceProperties=");
    builder.append(resourceProperties);
    builder.append(", oldResourceProperties=");
    builder.append(oldResourceProperties);
    builder.append("]");
    return builder.toString();
  }


}


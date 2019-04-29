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
package com.voicebase.gateways.awsconnect;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Tools and constants for dealing with Amazon Connect.
 *
 * @author Volker Kueffel <volker@voicebase.com>
 */
public class AmazonConnect {

  // keys in Amazon Connect input map
  public static final String CTR_NODE_INSTANCE_ARN = "InstanceARN";
  public static final String CTR_NODE_CONTACT_ID = "ContactId";
  public static final String CTR_NODE_ATTRIBUTES = "Attributes";
  public static final String CTR_NODE_MEDIA = "Recording";
  public static final String CTR_NODE_MEDIA_LOCATION = "Location";

  /**
   * Extract instance ID of the Connect instance from a CTR.
   *
   * @param ctrAsMap CTR as map
   * @return Amazon Connect instance ID or null if not found.
   */
  public static final String getInstanceId(Map<String, Object> ctrAsMap) {
    if (ctrAsMap == null || ctrAsMap.isEmpty()) {
      return null;
    }

    String instanceId = null;
    Object connectInstanceArn = ctrAsMap.get(CTR_NODE_INSTANCE_ARN);

    if (connectInstanceArn != null) {
      String arn = connectInstanceArn.toString();
      try {
        instanceId = arn.substring(arn.lastIndexOf("/") + 1);
      } catch (Exception e) {
        // Ignore, can't find instance ID
      }
    }
    return instanceId;
  }

  /**
   * Extract Contact ID from a CTR.
   *
   * @param ctrAsMap CTR as map
   * @return contact ID or null if not found
   */
  public static final String getContactId(Map<String, Object> ctrAsMap) {
    if (ctrAsMap == null || ctrAsMap.isEmpty()) {
      return null;
    }
    Object externalId = ctrAsMap.get(CTR_NODE_CONTACT_ID);
    return externalId != null ? externalId.toString() : null;
  }

  /**
   * Extract the attributes from a CTR.
   *
   * @param ctrAsMap CTR as map
   * @return the attributes map or null if not found or not a map.
   */
  @SuppressWarnings("unchecked")
  public static final Map<String, Object> getAttributes(Map<String, Object> ctrAsMap) {
    if (ctrAsMap == null || ctrAsMap.isEmpty()) {
      return null;
    }
    Object attributes = ctrAsMap.get(CTR_NODE_ATTRIBUTES);
    if (attributes != null && attributes instanceof Map) {
      return (Map<String, Object>) attributes;
    }
    return null;
  }

  /**
   * Set a CTR attribute on the given map.
   *
   * @param ctrAsMap CTR to add attributes to
   * @param key attribute name
   * @param value attribute value
   * @return previous value of given attribute, null if previously unset
   */
  public static final Object setAttribute(Map<String, Object> ctrAsMap, String key, Object value) {
    if (StringUtils.isBlank(key)) {
      return null;
    }
    Map<String, Object> attributes = getAttributes(ctrAsMap);
    if (attributes == null) {
      attributes = new HashMap<>();
      ctrAsMap.put(CTR_NODE_ATTRIBUTES, attributes);
    }
    return attributes.put(key, value);
  }

  /**
   * Extract recording location from a CTR.
   *
   * @param ctrAsMap CTR as map
   * @return the recording location or null if not found
   */
  public static final String getRecordingLocation(Map<String, Object> ctrAsMap) {
    if (ctrAsMap == null || ctrAsMap.isEmpty()) {
      return null;
    }

    String recordingLocation = null;

    Object mediaData = ctrAsMap.get(AmazonConnect.CTR_NODE_MEDIA);
    if (mediaData != null && mediaData instanceof Map) {
      @SuppressWarnings("rawtypes")
      Object location = ((Map) mediaData).get(AmazonConnect.CTR_NODE_MEDIA_LOCATION);
      if (location != null) {
        recordingLocation = location.toString();
      }
    }

    return recordingLocation;
  }
}

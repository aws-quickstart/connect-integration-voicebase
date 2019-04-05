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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ImmutableConfiguration;
import org.apache.commons.configuration2.MapConfiguration;
import org.apache.commons.configuration2.SubsetConfiguration;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to extract VoiceBase settings from Amazon Connect attributes.
 *
 * @author Volker Kueffel <volker@voicebase.com>
 */
public final class VoiceBaseAttributeExtractor extends MapConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(VoiceBaseAttributeExtractor.class);

  public static VoiceBaseAttributeExtractor fromAwsInputData(Map<String, Object> awsInputData) {
    if (awsInputData == null) {
      return null;
    }
    Map<String, Object> vbAttr = AmazonConnect.getAttributes(awsInputData);
    if (vbAttr == null) {
      return null;
    }
    return new VoiceBaseAttributeExtractor(vbAttr);
  }

  /**
   * Extract string list from configuration.
   *
   * <p>Will either return a list of strings or null, never an empty list. All entries are trimmed
   * and empty entries are skipped.
   *
   * @param attr configuration
   * @param key configuration key
   * @return list of strings extracted from parameter or null.
   * @see Lambda#VB_CONFIG_LIST_SEPARATOR
   */
  public static List<String> getStringParameterList(ImmutableConfiguration attr, String key) {
    String param = getStringParameter(attr, key);
    if (param != null) {
      String[] entries = param.split(Lambda.VB_CONFIG_LIST_SEPARATOR);
      if (entries != null && entries.length > 0) {
        ArrayList<String> result = new ArrayList<>();
        for (String entry : entries) {
          if (!StringUtils.isBlank(entry)) {
            result.add(StringUtils.trim(entry));
          }
        }
        if (!result.isEmpty()) {
          return result;
        }
      }
    }
    return null;
  }

  /**
   * Extract a set of strings from configuration.
   *
   * <p>Will either return a set of strings or null, never an empty set. All entries are trimmed and
   * empty entries are skipped.
   *
   * @param attr configuration
   * @param key configuration key
   * @return set of strings extracted from parameter or null.
   * @see Lambda#VB_CONFIG_LIST_SEPARATOR
   */
  public static Set<String> getStringParameterSet(ImmutableConfiguration attr, String key) {
    List<String> params = getStringParameterList(attr, key);
    if (params != null && !params.isEmpty()) {
      return Sets.newLinkedHashSet(params);
    }
    return null;
  }

  /**
   * Get string parameter from configuration.
   *
   * <p>Result string is trimmed.
   *
   * @param attr configuration
   * @param key configuration key
   * @return parameter value or null if no such key or value is empty or the pre-defined null-string
   * @see Lambda#VB_CONFIG_NULL_STRING
   */
  public static String getStringParameter(ImmutableConfiguration attr, String key) {
    if (attr != null && attr.containsKey(key)) {
      String param = attr.getString(key, null);
      if (!StringUtils.isBlank(param)
          && !StringUtils.equalsIgnoreCase(param, Lambda.VB_CONFIG_NULL_STRING)) {
        return StringUtils.trimToNull(param);
      }
    }
    return null;
  }

  public static Boolean getBooleanParameter(ImmutableConfiguration attr, String key) {
    String boolStr = getStringParameter(attr, key);
    if (boolStr != null) {
      try {
        Boolean result =
            BooleanUtils.toBooleanObject(
                boolStr,
                Lambda.VB_CONFIG_BOOLEAN_TRUE_STRING,
                Lambda.VB_CONFIG_BOOLEAN_FALSE_STRING,
                Lambda.VB_CONFIG_NULL_STRING);
        return result;
      } catch (Exception e) {
        LOGGER.warn("Invalid value for {}: {}", key, boolStr);
      }
    }
    return null;
  }

  public static boolean getBooleanParameter(
      ImmutableConfiguration attr, String key, boolean defaultValue) {
    Boolean bool = getBooleanParameter(attr, key);
    if (bool == null) {
      return defaultValue;
    }
    return bool.booleanValue();
  }

  public static Integer getIntegerParameter(ImmutableConfiguration attr, String key) {
    String intStr = getStringParameter(attr, key);
    if (intStr != null) {
      try {
        Integer result = NumberUtils.createInteger(intStr);
        return result;
      } catch (Exception e) {
        LOGGER.warn("Invalid value for {}: {}", key, intStr);
      }
    }
    return null;
  }

  public static int getIntegerParameter(ImmutableConfiguration attr, String key, int defaultValue) {
    Integer intParam = getIntegerParameter(attr, key);
    if (intParam == null) {
      return defaultValue;
    }
    return intParam;
  }

  public static String getVoicebaseAttributeName(String... levels) {
    return createAttributeName(Lambda.VB_ATTR, levels);
  }

  public static String getVoicebaseXAttributeName(String... levels) {
    return createAttributeName(Lambda.X_VB_ATTR, levels);
  }

  private static String createAttributeName(String topLevel, String... levels) {
    if (levels == null) {
      return null;
    }
    List<String> allLevels = new ArrayList<>();
    allLevels.add(topLevel);
    allLevels.addAll(Lists.newArrayList(levels));
    return StringUtils.join(allLevels, Lambda.VB_CONFIG_DELIMITER);
  }

  public static String getS3RecordingLocation(Map<String, Object> dataAsMap) {
    if (dataAsMap == null) {
      return null;
    }

    String s3Location = AmazonConnect.getRecordingLocation(dataAsMap);

    try {
      if (s3Location == null) {
        Map<String, Object> attributes = AmazonConnect.getAttributes(dataAsMap);
        if (attributes != null) {
          Object locationFromVBAttribute =
              attributes.get(getVoicebaseAttributeName(Lambda.VB_ATTR_RECORDING_LOCATION));
          if (locationFromVBAttribute != null) {
            s3Location = locationFromVBAttribute.toString();
          }
        }
      }
    } catch (Exception e) {
      LOGGER.warn("Error extracting media location.", e);
    }
    return s3Location;
  }

  public static ZonedDateTime getIntegrationReceiveTime(Map<String, Object> ctrAsMap) {
    Map<String, Object> attributes = AmazonConnect.getAttributes(ctrAsMap);
    if (attributes != null) {
      Object receiveAttribute =
          attributes.get(
              VoiceBaseAttributeExtractor.getVoicebaseXAttributeName(
                  Lambda.X_VB_ATTR_INTEGRATION_RECEIVE_TIME));
      if (receiveAttribute != null) {
        try {
          return ZonedDateTime.parse(receiveAttribute.toString(), BeanFactory.dateTimeFormatter());
        } catch (Exception e) {
          // don't publish if we can't extract start time
          LOGGER.debug("Unable to extract VoiceBase receive time", e);
        }
      }
    }
    return null;
  }

  public static Long getTimeElapsedSinceReceived(Map<String, Object> ctrAsMap) {
    ZonedDateTime received = VoiceBaseAttributeExtractor.getIntegrationReceiveTime(ctrAsMap);
    if (received != null) {
      return Duration.between(received, ZonedDateTime.now()).getSeconds();
    }
    return null;
  }

  public VoiceBaseAttributeExtractor(Map<String, ?> map) {
    super(map);
    setThrowExceptionOnMissing(false);
  }

  @Override
  public Configuration subset(String prefix) {
    return new SubsetConfiguration(this, prefix, Lambda.VB_CONFIG_DELIMITER);
  }
}

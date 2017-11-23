package com.voicebase.gateways.lily;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

final class VoiceBaseAttributeExtractor extends MapConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(VoiceBaseAttributeExtractor.class);

  /**
   * Extract string list from configuration.
   * <p/>
   * Will either return a list of strings or null, never an empty list. Empty
   * list entries are skipped. All entries are trimmed.
   * 
   * @param attr
   *          configuration
   * @param key
   *          configuration key
   * 
   * @return list of strings extracted from parameter or null.
   * 
   * @see KinesisRecordProcessor#Constants.VB_CONFIG_LIST_SEPARATOR
   */
  static List<String> getStringParameterList(ImmutableConfiguration attr, String key) {
    String param = getStringParameter(attr, key);
    if (param != null) {
      String[] entries = param.split(Constants.VB_CONFIG_LIST_SEPARATOR);
      if (entries != null && entries.length > 0) {
        ArrayList<String> result = new ArrayList<>();
        for (String entry : entries) {
          if (!StringUtils.isEmpty(entry)) {
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

  static Set<String> getStringParameterSet(ImmutableConfiguration attr, String key) {
    List<String> params = getStringParameterList(attr, key);
    if (params != null && !params.isEmpty()) {
      return Sets.newHashSet(params);
    }
    return null;
  }

  /**
   * Get string parameter from configuration.
   * <p/>
   * Result string is trimmed.
   * 
   * @param attr
   *          configuration
   * @param key
   *          configuration key
   * 
   * @return parameter value or null if no such key or value is empty or the
   *         pre-defined null-string
   * 
   * @see KinesisRecordProcessor#Constants.VB_CONFIG_NULL_STRING
   */
  static String getStringParameter(ImmutableConfiguration attr, String key) {
    if (attr != null && attr.containsKey(key)) {
      String param = attr.getString(key, null);
      if (!StringUtils.isEmpty(param) && !StringUtils.equalsIgnoreCase(param, Constants.VB_CONFIG_NULL_STRING)) {
        return StringUtils.trimToNull(param);
      }
    }
    return null;
  }

  static Boolean getBooleanParameter(ImmutableConfiguration attr, String key) {
    String boolStr = getStringParameter(attr, key);
    if (boolStr != null) {
      try {
        Boolean result = BooleanUtils.toBooleanObject(boolStr, Constants.VB_CONFIG_BOOLEAN_TRUE_STRING,
            Constants.VB_CONFIG_BOOLEAN_FALSE_STRING, Constants.VB_CONFIG_NULL_STRING);
        return result;
      } catch (Exception e) {
        LOGGER.warn("Invalid value for {}: {}", key, boolStr);
      }
    }
    return null;
  }

  static String getVoicebaseAttributeName(String... levels) {
    if (levels == null) {
      return null;
    }
    List<String> allLevels = new ArrayList<>();
    allLevels.add(Constants.VB_ATTR);
    allLevels.addAll(Lists.newArrayList(levels));
    return StringUtils.join(allLevels, Constants.VB_CONFIG_DELIMITER);
  }
  
  @SuppressWarnings("unchecked")
  static String getS3RecordingLocation(Map<String, Object> dataAsMap) {
    if (dataAsMap == null) {
      return null;
    }

    String s3Location = null;

    try {
      Map<String, Object> mediaData = (Map<String, Object>) dataAsMap.get(Constants.KEY_MEDIA);

      if (mediaData != null) {
        s3Location = mediaData.get(Constants.KEY_MEDIA_LOCATION).toString();
      }
      if (s3Location == null) {
        Map<String, Object> attributes = (Map<String, Object>) dataAsMap.get(Constants.KEY_ATTRIBUTES);
        if (attributes != null) {
          s3Location = (String) attributes.get(getVoicebaseAttributeName(Constants.VB_ATTR_RECORDING_LOCATION));
        }
      }
    } catch (Exception e) {
      LOGGER.warn("Error extracting media location.", e);
    }
    return s3Location;
  }

  public VoiceBaseAttributeExtractor(Map<String, ?> map) {
    super(map);
  }

  public Configuration subset(String prefix) {
    return new SubsetConfiguration(this, prefix, Constants.VB_CONFIG_DELIMITER);
  }
}
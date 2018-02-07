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
 */
package com.voicebase.gateways.awsconnect;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

/**
 * Tool to extract typed values from the environment.
 *
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class ConfigUtil {

  protected static final Splitter CSV_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

  public static boolean getBooleanSetting(Map<String, String> env, String key, boolean defaultValue) {
    if (env == null) {
      return defaultValue;
    }
    String value = StringUtils.trimToNull(env.get(key));

    if (value == null) {
      return defaultValue;
    }

    return BooleanUtils.toBoolean(value);

  }

  public static String getStringSetting(Map<String, String> env, String key, String defaultValue) {
    if (env == null) {
      return defaultValue;
    }
    String value = StringUtils.trimToNull(env.get(key));

    if (value == null) {
      return defaultValue;
    }

    return value;
  }

  public static List<String> getStringListSetting(Map<String, String> env, String key, List<String> defaultValue) {
    String entry = getStringSetting(env, key, null);
    if (entry == null) {
      return defaultValue;
    }

    return CSV_SPLITTER.splitToList(entry);
  }

  public static long getLongSetting(Map<String, String> env, String key, long defaultValue) {
    if (env == null) {
      return defaultValue;
    }
    String value = StringUtils.trimToNull(env.get(key));

    if (value == null) {
      return defaultValue;
    }

    try {
      return Long.parseLong(value);
    } catch (Exception e) {
      return defaultValue;
    }

  }

  public static int getIntSetting(Map<String, String> env, String key, int defaultValue) {
    if (env == null) {
      return defaultValue;
    }
    String value = StringUtils.trimToNull(env.get(key));

    if (value == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(value);
    } catch (Exception e) {
      return defaultValue;
    }
  }

}

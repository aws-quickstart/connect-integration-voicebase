package com.voicebase.gateways.lily;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

public abstract class LambdaProcessor {

  protected static final Splitter CSV_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

  protected void configureLogging(Map<String, String> env) {
    if (env != null) {
      String logConfig = env.get(Lambda.ENV_LOG_CONFIG);
      LogConfigurer.configure(logConfig);
    }
  }

  protected boolean getBooleanSetting(Map<String, String> env, String key, boolean defaultValue) {
    if (env == null) {
      return defaultValue;
    }
    String value = StringUtils.trimToNull(env.get(key));

    if (value == null) {
      return defaultValue;
    }

    return BooleanUtils.toBoolean(value);

  }

  protected String getStringSetting(Map<String, String> env, String key, String defaultValue) {
    if (env == null) {
      return defaultValue;
    }
    String value = StringUtils.trimToNull(env.get(key));

    if (value == null) {
      return defaultValue;
    }

    return value;
  }

  protected List<String> getStringListSetting(Map<String, String> env, String key, List<String> defaultValue) {
    String entry = getStringSetting(env, key, null);
    if (entry == null) {
      return defaultValue;
    }

    return CSV_SPLITTER.splitToList(entry);
  }

  protected long getLongSetting(Map<String, String> env, String key, long defaultValue) {
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

  protected int getIntSetting(Map<String, String> env, String key, int defaultValue) {
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
package com.voicebase.gateways.lily;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import com.google.common.base.Splitter;

/**
 * Helper to dynamically configure the logger.
 * <p/>
 * Will currently only configure Log4j without even checking if that is the
 * current log system.
 * 
 * 
 * @author volker@voicebase.com
 *
 */
public class LogConfigurer {

  private static final Splitter CONFIG_SPLITTER = Splitter.on(";").omitEmptyStrings().trimResults();
  private static final Splitter LEVEL_SPLITTER = Splitter.on("=").omitEmptyStrings().trimResults().limit(2);
  private static final Splitter LOGGER_SPLITTER = Splitter.on(",").omitEmptyStrings().trimResults();

  public static void configure(String configString) {
    if (StringUtils.isEmpty(configString)) {
      return;
    }
    try {
      Map<String, Set<String>> config = extractLoggerConfig(configString);
      if (!config.isEmpty()) {
        for (Entry<String, Set<String>> levelConfig : config.entrySet()) {
          try {
            Level level = Level.valueOf(levelConfig.getKey());
            for (String logger : levelConfig.getValue()) {
              Configurator.setLevel(logger, level);
            }
          } catch (Exception e) {
            // unknown level, skip
          }
        }
      }
    } catch (Exception e) {
      // probably not Log4j, just don't die

    }
  }

  static Map<String, Set<String>> extractLoggerConfig(String configString) {
    HashMap<String, Set<String>> loggerConfig = new HashMap<>();
    if (!StringUtils.isEmpty(configString)) {
      List<String> configs = CONFIG_SPLITTER.splitToList(configString);
      if (!configs.isEmpty()) {
        for (String config : configs) {
          List<String> levelConfigs = LEVEL_SPLITTER.splitToList(config);
          if (!levelConfigs.isEmpty() && levelConfigs.size() > 1) {
            String level = StringUtils.upperCase(levelConfigs.get(0));
            List<String> loggers = LOGGER_SPLITTER.splitToList(levelConfigs.get(1));
            if (!loggers.isEmpty()) {
              Set<String> levelConfig = loggerConfig.get(level);
              if (levelConfig == null) {
                levelConfig = new HashSet<>();
                loggerConfig.put(level, levelConfig);
              }
              for (String logger : loggers) {
                levelConfig.add(logger);
              }
            }
          }
        }
      }
    }
    return loggerConfig;
  }

}

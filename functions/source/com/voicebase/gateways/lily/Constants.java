package com.voicebase.gateways.lily;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

class Constants {

  static final String KEY_ATTRIBUTES = "Attributes";
  static final String KEY_EXTERNAL_ID = "ContactId";
  static final String KEY_MEDIA = "Recording";
  static final String KEY_MEDIA_LOCATION = "Location";

  static final String VB_ATTR = "voicebase";
  static final String VB_ATTR_CLASSIFIER = "classifier";
  static final String VB_ATTR_CLASSIFIER_NAMES = "names";
  static final String VB_ATTR_KEYWORDS = "phraseSpotting";
  static final String VB_ATTR_KEYWORDS_GROUPS = "groups";
  static final String VB_ATTR_LANGUAGE = "language";
  static final String VB_ATTR_PCIREDACT = "pciRedaction";
  static final String VB_ATTR_PRIORIY = "priority";
  static final String VB_ATTR_RECORDING_LOCATION = "recordingLocation";
  static final String VB_ATTR_TRANSCRIPT = "transcript";
  static final String VB_ATTR_TRANSCRIPT_NUMBER_FORMAT = "formatNumbers";
  static final String VB_ATTR_TRANSCRIPT_SWEARWORD_FILTER = "filterSwearWords";
  static final String VB_ATTR_VOCABULARY = "vocabulary";
  static final String VB_ATTR_VOCABULARY_NAMES = "names";
  static final String VB_ATTR_VOCABULARY_TERMS = "terms";
  static final String VB_CONFIG_BOOLEAN_FALSE_STRING = "0";
  static final String VB_CONFIG_BOOLEAN_TRUE_STRING = "1";
  static final String VB_CONFIG_DELIMITER = "_";
  static final String VB_CONFIG_LIST_SEPARATOR = ",";
  static final String VB_CONFIG_NULL_STRING = "null";

  static final String DEFAULT_LEFT_SPEAKER_NAME = "Caller";
  static final String DEFAULT_RIGHT_SPEAKER_NAME = "Agent";

  static final TypeReference<Map<String, Object>> MSG_JAVA_TYPE = new TypeReference<Map<String, Object>>() {
  };
}

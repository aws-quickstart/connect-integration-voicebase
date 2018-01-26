package com.voicebase.gateways.lily;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
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
 * @author volker@voicebase.com
 *
 */
public class Constants {

  public static final String KEY_ATTRIBUTES = "Attributes";
  public static final String KEY_EXTERNAL_ID = "ContactId";
  public static final String KEY_MEDIA = "Recording";
  public static final String KEY_MEDIA_LOCATION = "Location";

  public static final String VB_ATTR = "voicebase";
  public static final String VB_ATTR_CLASSIFIER = "classifier";
  public static final String VB_ATTR_CLASSIFIER_NAMES = "names";
  public static final String VB_ATTR_KEYWORDS = "phraseSpotting";
  public static final String VB_ATTR_KEYWORDS_GROUPS = "groups";
  public static final String VB_ATTR_LANGUAGE = "language";
  public static final String VB_ATTR_PCIREDACT = "pciRedaction";
  public static final String VB_ATTR_PRIORIY = "priority";
  public static final String VB_ATTR_RECORDING_LOCATION = "recordingLocation";
  public static final String VB_ATTR_TRANSCRIPT = "transcript";
  public static final String VB_ATTR_TRANSCRIPT_NUMBER_FORMAT = "formatNumbers";
  public static final String VB_ATTR_TRANSCRIPT_SWEARWORD_FILTER = "filterSwearWords";
  public static final String VB_ATTR_KNOWLEDGE = "knowledge";
  public static final String VB_ATTR_KNOWLEDGE_DISCOVERY = "discover";
  public static final String VB_ATTR_VOCABULARY = "vocabulary";
  public static final String VB_ATTR_VOCABULARY_NAMES = "names";
  public static final String VB_ATTR_VOCABULARY_TERMS = "terms";
  public static final String VB_CONFIG_BOOLEAN_FALSE_STRING = "0";
  public static final String VB_CONFIG_BOOLEAN_TRUE_STRING = "1";
  public static final String VB_CONFIG_DELIMITER = "_";
  public static final String VB_CONFIG_LIST_SEPARATOR = ",";
  public static final String VB_CONFIG_NULL_STRING = "null";

  public static final String DEFAULT_LEFT_SPEAKER_NAME = "Caller";
  public static final String DEFAULT_RIGHT_SPEAKER_NAME = "Agent";

  public static final TypeReference<Map<String, Object>> MSG_JAVA_TYPE = new TypeReference<Map<String, Object>>() {
  };
}

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
package com.voicebase.gateways.awsconnect.lambda;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;

/**
 * Attribute names provided by Amazon Connect to configure VoiceBase API
 * requests, environment variables names and some defaults to be use in the
 * Lambda functions.
 * 
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class Lambda {

  // environment variables
  public static final String ENV_LOG_CONFIG = "VOICEBASE_LOG_CONFIG";
  public static final String ENV_API_CLIENT_LOGLEVEL = "VOICEBASE_API_CLIENT_LOGLEVEL";
  public static final String ENV_API_RETRY_ATTEMPTS = "VOICEBASE_API_RETRY_ATTEMPTS";
  public static final String ENV_API_RETRY_DELAY = "VOICEBASE_API_RETRY_DELAY";
  public static final String ENV_API_TOKEN = "VOICEBASE_API_TOKEN";
  public static final String ENV_API_URL = "VOICEBASE_API_URL";

  public static final String ENV_TRANSCRIPT_OUTPUT_STREAM = "VOICEBASE_TRANSCRIPT_OUTPUT_STREAM";
  public static final String ENV_CALLBACK_SOURCE_IPS = "VOICEBASE_CALLBACK_SOURCE_IPS";
  public static final String ENV_CALLBACK_SOURCE_IPS_VALIDATE = "VOICEBASE_CALLBACK_SOURCE_IPS_VALIDATE";

  public static final String ENV_CALLBACK_INCLUDES = "VOICEBASE_CALLBACK_INCLUDES";
  public static final String ENV_CALLBACK_METHOD = "VOICEBASE_CALLBACK_METHOD";
  public static final String ENV_CALLBACK_URL = "VOICEBASE_CALLBACK_URL";
  public static final String ENV_CALLBACK_ADDITIONAL_URLS = "VOICEBASE_CALLBACK_ADDITIONAL_URLS";
  public static final String ENV_CONFIGURE_SPEAKERS = "VOICEBASE_SPEAKERS_CONFIGURE";
  public static final String ENV_ENABLE_PREDICTIONS = "VOICEBASE_PREDICTIONS_ENABLE";
  public static final String ENV_ENABLE_KNOWLEDGE_DISCOVERY = "VOICEBASE_KNOWLEDGE_DISCOVERY_ENABLE";
  public static final String ENV_LEFT_SPEAKER = "VOICEBASE_SPEAKERS_LEFT";
  public static final String ENV_MEDIA_URL_TTL_MILLIS = "VOICEBASE_MEDIA_URL_TTL_MILLIS";
  public static final String ENV_RIGHT_SPEAKER = "VOICEBASE_SPEAKERS_RIGHT";

  // keys in Amazon Connect input map
  public static final String KEY_ATTRIBUTES = "Attributes";
  public static final String KEY_EXTERNAL_ID = "ContactId";
  public static final String KEY_MEDIA = "Recording";
  public static final String KEY_MEDIA_LOCATION = "Location";

  // VoiceBase attribute names in Amazon Connect provided attributes
  public static final String VB_ATTR = "voicebase";
  public static final String VB_ATTR_CLASSIFIER = "classifier";
  public static final String VB_ATTR_CLASSIFIER_NAMES = "names";
  public static final String VB_ATTR_KEYWORDS = "phraseSpotting";
  public static final String VB_ATTR_KEYWORDS_GROUPS = "groups";
  public static final String VB_ATTR_KNOWLEDGE = "knowledge";
  public static final String VB_ATTR_KNOWLEDGE_DISCOVERY = "discover";
  public static final String VB_ATTR_LANGUAGE = "language";
  public static final String VB_ATTR_PCIREDACT = "pciRedaction";
  public static final String VB_ATTR_PRIORIY = "priority";
  public static final String VB_ATTR_RECORDING_LOCATION = "recordingLocation";
  public static final String VB_ATTR_TRANSCRIPT = "transcript";
  public static final String VB_ATTR_TRANSCRIPT_NUMBER_FORMAT = "formatNumbers";
  public static final String VB_ATTR_TRANSCRIPT_SWEARWORD_FILTER = "filterSwearWords";
  public static final String VB_ATTR_VOCABULARY = "vocabulary";
  public static final String VB_ATTR_VOCABULARY_NAMES = "names";
  public static final String VB_ATTR_VOCABULARY_TERMS = "terms";
  public static final String VB_CONFIG_BOOLEAN_FALSE_STRING = "0";
  public static final String VB_CONFIG_BOOLEAN_TRUE_STRING = "1";
  public static final String VB_CONFIG_DELIMITER = "_";
  public static final String VB_CONFIG_LIST_SEPARATOR = ",";
  public static final String VB_CONFIG_NULL_STRING = "null";

  // defaults
  public static final String DEFAULT_V3_API_URL = "https://apis.voicebase.com/v3";
  public static final int DEFAULT_API_RETRY_ATTEMPTS = 3;
  public static final long DEFAULT_API_RETRY_DELAY = 100;
  public static final String DEFAULT_API_CLIENT_LOG_LEVEL = "BASIC";
  public static final String DEFAULT_CALLBACK_METHOD = "POST";
  public static final boolean DEFAULT_ENABLE_KNOWLEDGE_DISCOVERY = false;
  public static final List<String> DEFAULT_CALLBACK_INCLUDES_V3 = Lists.newArrayList("METADATA", "TRANSCRIPT",
      "SPOTTING", "KNOWLEDGE", "PREDICTION");
  public static final long DEFAULT_MEDIA_URL_TTL_MILLIS = 900000L; // 15min
  public static final List<String> DEFAULT_SOURCE_IPS = Lists.newArrayList("52.6.244.43", "52.6.208.178",
      "52.2.171.140");

  public static final boolean DEFAULT_SOURCE_IPS_VALIDATE = true;
  public static final String DEFAULT_LEFT_SPEAKER_NAME = "Caller";
  public static final String DEFAULT_RIGHT_SPEAKER_NAME = "Agent";

  // other
  public static final TypeReference<Map<String, Object>> MSG_JAVA_TYPE = new TypeReference<Map<String, Object>>() {
  };

}

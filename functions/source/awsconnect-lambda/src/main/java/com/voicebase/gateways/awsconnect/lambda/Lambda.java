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
package com.voicebase.gateways.awsconnect.lambda;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.voicebase.v3client.datamodel.VbIncludeTypeEnum;
import java.util.List;
import java.util.Map;

/**
 * Attribute names provided by Amazon Connect to configure VoiceBase API requests, environment
 * variables names and some defaults to be use in the Lambda functions.
 *
 * @author Volker Kueffel <volker@voicebase.com>
 */
public class Lambda {

  // environment variables
  public static final String ENV_LOG_CONFIG = "VOICEBASE_LOG_CONFIG";
  /** Environment variable name to enable monitoring */
  public static final String ENV_MONITORING_ENABLE = "VOICEBASE_MONITORING_ENABLE";

  public static final String ENV_MONITORING_METRIC_NAMESPACE = "VOICEBASE_MONITORING_NAMESPACE";
  public static final String ENV_MONITORING_METRIC_BUFFER_SIZE = "VOICEBASE_MONITORING_BUFFER_SIZE";
  public static final String ENV_MONITORING_METRIC_FLUSH_INTERVAL =
      "VOICEBASE_MONITORING_FLUSH_INTERVAL";
  public static final String ENV_MONITORING_METRIC_FLUSH_ON_FULL_BUFFER =
      "VOICEBASE_MONITORING_FLUSH_ON_FULL_BUFFER";

  public static final String ENV_API_CLIENT_LOGLEVEL = "VOICEBASE_API_CLIENT_LOGLEVEL";
  public static final String ENV_API_RETRY_ATTEMPTS = "VOICEBASE_API_RETRY_ATTEMPTS";
  public static final String ENV_API_RETRY_DELAY = "VOICEBASE_API_RETRY_DELAY";
  /** Environment variable name that holds the VoiceBase API token */
  public static final String ENV_API_TOKEN = "VOICEBASE_API_TOKEN";
  /** Environment variable name that holds the VoiceBase API base URL */
  public static final String ENV_API_URL = "VOICEBASE_API_URL";

  public static final String ENV_TRANSCRIPT_OUTPUT_STREAM = "VOICEBASE_TRANSCRIPT_OUTPUT_STREAM";
  public static final String ENV_TRANSCRIPT_OUTPUT_ADD_NEWLINE =
      "VOICEBASE_TRANSCRIPT_OUTPUT_ADD_NEWLINE";
  public static final String ENV_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE =
      "VOICEBASE_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE";

  public static final String ENV_CALLBACK_SOURCE_IPS = "VOICEBASE_CALLBACK_SOURCE_IPS";
  public static final String ENV_CALLBACK_SOURCE_IPS_VALIDATE =
      "VOICEBASE_CALLBACK_SOURCE_IPS_VALIDATE";

  public static final String ENV_CALLBACK_INCLUDES = "VOICEBASE_CALLBACK_INCLUDES";
  public static final String ENV_CALLBACK_METHOD = "VOICEBASE_CALLBACK_METHOD";
  public static final String ENV_CALLBACK_URL = "VOICEBASE_CALLBACK_URL";
  public static final String ENV_CALLBACK_ADDITIONAL_URLS = "VOICEBASE_CALLBACK_ADDITIONAL_URLS";
  public static final String ENV_CONFIGURE_SPEAKERS = "VOICEBASE_SPEAKERS_CONFIGURE";
  public static final String ENV_PREDICTIONS_ENABLE = "VOICEBASE_PREDICTIONS_ENABLE";
  public static final String ENV_KNOWLEDGE_DISCOVERY_ENABLE =
      "VOICEBASE_KNOWLEDGE_DISCOVERY_ENABLE";
  public static final String ENV_ADVANCED_PUNCTUATION_ENABLE =
      "VOICEBASE_ADVANCED_PUNCTUATION_ENABLE";
  /**
   * Property name for environment variable controlling whether to enable the categorization
   * feature. If false, the request attributes enableAllCategories and categoryNames will be
   * ignored.
   */
  public static final String ENV_CATEGORIZATION_ENABLE = "VOICEBASE_CATEGORIZATION_ENABLE";
  /**
   * Property name for environment variable controlling whether to enable the analytic indexing
   * feature. If false, the request attribute enableAnalyticIndexing will be ignored.
   */
  public static final String ENV_ANALYTIC_INDEXING_ENABLE = "VOICEBASE_ANALYTIC_INDEXING_ENABLE";

  public static final String ENV_LEFT_SPEAKER = "VOICEBASE_SPEAKERS_LEFT";
  public static final String ENV_MEDIA_URL_TTL_MILLIS = "VOICEBASE_MEDIA_URL_TTL_MILLIS";
  public static final String ENV_RIGHT_SPEAKER = "VOICEBASE_SPEAKERS_RIGHT";

  public static final String ENV_DELAYED_QUEUE_SQS_URL = "VOICEBASE_DELAYED_QUEUE_SQS_URL";
  public static final String ENV_DEAD_LETTER_QUEUE_SQS_URL = "VOICEBASE_DEAD_LETTER_QUEUE_SQS_URL";
  public static final String ENV_EXP_BACKOFF_INITIAL_DELAY = "VOICEBASE_EXP_BACKOFF_INITIAL_DELAY";
  public static final String ENV_EXP_BACKOFF_MAX_DELAY = "VOICEBASE_EXP_BACKOFF_MAX_DELAY";
  public static final String ENV_EXP_BACKOFF_FACTOR = "VOICEBASE_EXP_BACKOFF_FACTOR";

  public static final String ENV_MAX_REDELIVERIES = "VOICEBASE_MAX_REDELIVERIES";

  // VoiceBase attribute names in Amazon Connect provided attributes
  public static final String VB_ATTR = "voicebase";
  public static final String VB_ATTR_ENABLE = "enable";
  public static final String VB_ATTR_REDELIVERY_COUNT = "redeliveryCount";
  public static final String VB_ATTR_CLASSIFIER = "classifier";
  public static final String VB_ATTR_CLASSIFIER_NAMES = "names";
  public static final String VB_ATTR_KEYWORDS = "phraseSpotting";
  public static final String VB_ATTR_KEYWORDS_GROUPS = "groups";
  public static final String VB_ATTR_KNOWLEDGE = "knowledge";
  public static final String VB_ATTR_KNOWLEDGE_DISCOVERY = "discover";
  public static final String VB_ATTR_METRICS = "metrics";
  public static final String VB_ATTR_METRICS_GROUPS = "groups";
  public static final String VB_ATTR_LANGUAGE = "language";
  public static final String VB_ATTR_LANGUAGE_EXTENSIONS = "languageExtensions";
  public static final String VB_ATTR_PCIREDACT = "pciRedaction";
  public static final String VB_ATTR_NUMBERREDACT = "numberRedaction";
  public static final String VB_ATTR_PRIORIY = "priority";
  public static final String VB_ATTR_RECORDING_LOCATION = "recordingLocation";
  public static final String VB_ATTR_TRANSCRIPT = "transcript";
  public static final String VB_ATTR_TRANSCRIPT_NUMBER_FORMAT = "formatNumbers";
  public static final String VB_ATTR_TRANSCRIPT_SWEARWORD_FILTER = "filterSwearWords";
  public static final String VB_ATTR_VOCABULARY = "vocabulary";
  public static final String VB_ATTR_VOCABULARY_NAMES = "names";
  public static final String VB_ATTR_VOCABULARY_TERMS = "terms";
  public static final String VB_ATTR_REDACTORS = "redactors";
  public static final String VB_ATTR_DETECTORS = "detectors";

  /**
   * Indicates whether or not to enable indexing of the transcripts into the VoiceBase Analytic
   * database, queryable by VBQL
   */
  public static final String VB_ATTR_ENABLE_ANALYTICAL_INDEXING = "enableAnalyticIndexing";
  /** Indicates to perform categorization with all the active categories */
  public static final String VB_ATTR_ENABLE_ALL_CATEGORIES = "enableAllCategories";
  /** Indicates to perform categorization with only the specified categories */
  public static final String VB_ATTR_CATEGORY_NAMES = "categoryNames";

  public static final String X_VB_ATTR = "x-voicebase";
  public static final String X_VB_ATTR_TIMES_TO_FAIL_AUDIO_EXISTS = "timesToFailAudioExists";
  public static final String X_VB_ATTR_TIMES_TO_FAIL_VOICEBASE_API = "timesToFailVoiceBaseAPI";
  public static final String X_VB_ATTR_INTEGRATION_RECEIVE_TIME = "integrationReceiveTimestamp";

  public static final String VB_CONFIG_BOOLEAN_FALSE_STRING = "0";
  public static final String VB_CONFIG_BOOLEAN_TRUE_STRING = "1";
  public static final String VB_CONFIG_DELIMITER = "_";
  public static final String VB_CONFIG_LIST_SEPARATOR = ",";
  public static final String VB_CONFIG_NULL_STRING = "null";

  // defaults
  public static final boolean DEFAULT_ENABLE = true;
  public static final boolean DEFAULT_MONITORING_ENABLE = true;
  public static final String DEFAULT_V3_API_URL = "https://apis.voicebase.com/v3";
  public static final int DEFAULT_API_RETRY_ATTEMPTS = 3;
  public static final long DEFAULT_API_RETRY_DELAY = 100;
  public static final String DEFAULT_API_CLIENT_LOG_LEVEL = "BASIC";
  public static final String DEFAULT_CALLBACK_METHOD = "POST";
  public static final boolean DEFAULT_KNOWLEDGE_DISCOVERY_ENABLE = false;
  public static final boolean DEFAULT_ADVANCED_PUNCTUATION_ENABLE = true;
  public static final boolean DEFAULT_PCI_REDACTION_ENABLE = false;
  public static final boolean DEFAULT_NUMBER_REDACTION_ENABLE = false;
  public static final boolean DEFAULT_CATEGORIZATION_ENABLE = true;
  public static final boolean DEFAULT_ANALYTIC_INDEXING_ENABLE = true;
  public static final int DEFAULT_MAX_REDELIVERIES = 7;

  public static final List<String> DEFAULT_CALLBACK_INCLUDES_V3 =
      Lists.newArrayList(
          VbIncludeTypeEnum.METADATA.toString(),
          VbIncludeTypeEnum.TRANSCRIPT.toString(),
          VbIncludeTypeEnum.SPOTTING.toString(),
          VbIncludeTypeEnum.KNOWLEDGE.toString(),
          VbIncludeTypeEnum.PREDICTION.toString(),
          VbIncludeTypeEnum.METRICS.toString(),
          VbIncludeTypeEnum.CATEGORIES.toString());
  public static final long DEFAULT_MEDIA_URL_TTL_MILLIS = 900000L; // 15min
  public static final List<String> DEFAULT_SOURCE_IPS =
      Lists.newArrayList("52.6.244.43", "52.6.208.178", "52.2.171.140");

  public static final boolean DEFAULT_SOURCE_IPS_VALIDATE = true;
  public static final String DEFAULT_LEFT_SPEAKER_NAME = "Caller";
  public static final String DEFAULT_RIGHT_SPEAKER_NAME = "Agent";
  public static final boolean DEFAULT_TRANSCRIPT_OUTPUT_ADD_NEWLINE = false;
  public static final boolean DEFAULT_TRANSCRIPT_OUTPUT_TRIM_IF_TOO_LARGE = false;

  public static final String DEFAULT_MONITORING_METRIC_NAMESPACE = "voicebase/awsconnect";
  public static final int DEFAULT_MONITORING_METRIC_BUFFER_SIZE = 40;
  public static final int DEFAULT_MONITORING_METRIC_FLUSH_INTERVAL = 2500;
  public static final boolean DEFAULT_MONITORING_METRIC_FLUSH_ON_FULL_BUFFER = true;

  // other

  public static final TypeReference<Map<String, Object>> MSG_JAVA_TYPE =
      new TypeReference<Map<String, Object>>() {};
}

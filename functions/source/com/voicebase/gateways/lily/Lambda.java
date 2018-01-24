package com.voicebase.gateways.lily;

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
class Lambda {

  static final String ENV_LOG_CONFIG = "VOICEBASE_LOG_CONFIG";
  static final String ENV_API_CLIENT_LOGLEVEL = "VOICEBASE_API_CLIENT_LOGLEVEL";
  static final String ENV_API_RETRY_ATTEMPTS = "VOICEBASE_API_RETRY_ATTEMPTS";
  static final String ENV_API_RETRY_DELAY = "VOICEBASE_API_RETRY_DELAY";
  static final String ENV_API_TOKEN = "VOICEBASE_API_TOKEN";
  static final String ENV_API_URL = "VOICEBASE_API_URL";

  static final String DEFAULT_V2_API_URL = "https://apis.voicebase.com/v2-beta";
  static final String DEFAULT_V3_API_URL = "https://apis.voicebase.com/v3";

  static final int DEFAULT_API_RETRY_ATTEMPTS = 3;
  static final long DEFAULT_API_RETRY_DELAY = 100;
  static final String DEFAULT_API_CLIENT_LOG_LEVEL = "BASIC";
  static final String DEFAULT_CALLBACK_METHOD = "POST";
  static final boolean DEFAULT_ENABLE_KNOWLEDGE_DISCOVERY = false;

  static final String ENV_CALLBACK_INCLUDES = "VOICEBASE_CALLBACK_INCLUDES";
  static final String ENV_CALLBACK_METHOD = "VOICEBASE_CALLBACK_METHOD";
  static final String ENV_CALLBACK_URL = "VOICEBASE_CALLBACK_URL";
  static final String ENV_CALLBACK_ADDITIONAL_URLS = "VOICEBASE_CALLBACK_ADDITIONAL_URLS";
  static final String ENV_CONFIGURE_SPEAKERS = "VOICEBASE_SPEAKERS_CONFIGURE";
  static final String ENV_ENABLE_PREDICTIONS = "VOICEBASE_PREDICTIONS_ENABLE";
  static final String ENV_ENABLE_KNOWLEDGE_DISCOVERY = "VOICEBASE_KNOWLEDGE_DISCOVERY_ENABLE";
  static final String ENV_LEFT_SPEAKER = "VOICEBASE_SPEAKERS_LEFT";
  static final String ENV_MEDIA_URL_TTL_MILLIS = "VOICEBASE_MEDIA_URL_TTL_MILLIS";
  static final String ENV_RIGHT_SPEAKER = "VOICEBASE_SPEAKERS_RIGHT";

}

package com.voicebase.gateways.lily;

class Lambda {

  static final String ENV_API_CLIENT_LOGLEVEL = "VOICEBASE_API_CLIENT_LOGLEVEL";
  static final String ENV_API_RETRY_ATTEMPTS = "VOICEBASE_API_RETRY_ATTEMPTS";
  static final String ENV_API_RETRY_DELAY = "VOICEBASE_API_RETRY_DELAY";
  static final String ENV_API_TOKEN = "VOICEBASE_API_TOKEN";
  static final String ENV_API_URL = "VOICEBASE_API_URL";

  static final String DEFAULT_API_URL = "https://apis.voicebase.com/v2-beta";
  static final int DEFAULT_API_RETRY_ATTEMPTS = 3;
  static final long DEFAULT_API_RETRY_DELAY = 100;
  static final String DEFAULT_API_CLIENT_LOG_LEVEL = "BASIC";
  static final String DEFAULT_CALLBACK_METHOD = "POST";

  static final String ENV_CALLBACK_INCLUDES = "VOICEBASE_CALLBACK_INCLUDES";
  static final String ENV_CALLBACK_METHOD = "VOICEBASE_CALLBACK_METHOD";
  static final String ENV_CALLBACK_URL = "VOICEBASE_CALLBACK_URL";
  static final String ENV_CONFIGURE_SPEAKERS = "VOICEBASE_SPEAKERS_CONFIGURE";
  static final String ENV_ENABLE_PREDICTIONS = "VOICEBASE_PREDICTIONS_ENABLE";
  static final String ENV_LEFT_SPEAKER = "VOICEBASE_SPEAKERS_LEFT";
  static final String ENV_MEDIA_URL_TTL_MILLIS = "VOICEBASE_MEDIA_URL_TTL_MILLIS";
  static final String ENV_RIGHT_SPEAKER = "VOICEBASE_SPEAKERS_RIGHT";

}

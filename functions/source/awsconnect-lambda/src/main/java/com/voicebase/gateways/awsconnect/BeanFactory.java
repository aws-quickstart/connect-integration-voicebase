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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.activation.MimetypesFileTypeMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import com.voicebase.sdk.v3.VoiceBaseClient;

/**
 *
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class BeanFactory {

  public static final MimetypesFileTypeMap mimetypesFileTypeMap() {
    MimetypesFileTypeMap mimeMap = new MimetypesFileTypeMap();
    mimeMap.addMimeTypes("audio/mpeg mp3 mpeg3\naudio/ogg ogg\naudio/flac flac");
    return mimeMap;
  }

  public static final SimpleDateFormat dateFormatter() {
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    df.setTimeZone(TimeZone.getTimeZone("UTC"));
    return df;
  }

  public static final ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(dateFormatter());
    return objectMapper;
  }

  public static final VoiceBaseClient voicebaseClient() {
    VoiceBaseClient voicebaseClient = new VoiceBaseClient();
    voicebaseClient.setMimeMap(mimetypesFileTypeMap());
    return voicebaseClient;
  }

  public static final RequestSourceValidator requestSourceValidator(Map<String, String> env) {
    boolean validate = ConfigUtil.getBooleanSetting(env, Lambda.ENV_CALLBACK_SOURCE_IPS_VALIDATE,
        Lambda.DEFAULT_SOURCE_IPS_VALIDATE);
    List<String> callbackIps = ConfigUtil.getStringListSetting(env, Lambda.ENV_CALLBACK_SOURCE_IPS,
        Lambda.DEFAULT_SOURCE_IPS);
    return new RequestSourceValidator(callbackIps, validate);
  }
}

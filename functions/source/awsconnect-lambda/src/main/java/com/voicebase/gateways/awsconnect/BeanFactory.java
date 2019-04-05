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
package com.voicebase.gateways.awsconnect;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.voicebase.gateways.awsconnect.lambda.Lambda;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/** @author Volker Kueffel <volker@voicebase.com> */
public class BeanFactory {

  private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
  private static ObjectMapper OM;
  private static DateTimeFormatter DF;

  public static final SimpleDateFormat dateFormatter() {
    SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);
    df.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
    return df;
  }

  /**
   * Create a new date/time formatter.
   *
   * @return new date/time formatter
   */
  public static final DateTimeFormatter newDateTimeFormatter() {
    return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).withZone(ZoneOffset.UTC);
  }

  /**
   * Get a shared instance of the DateTimeFormatter to use.
   *
   * @return shared date/time formatter
   */
  public static final DateTimeFormatter dateTimeFormatter() {
    if (DF == null) {
      DF = newDateTimeFormatter();
    }
    return DF;
  }

  /**
   * Creates a new general purpose object mapper.
   *
   * <p>If parsing the VoiceBase API object model use the object mapper shipped with the object
   * model.
   *
   * @return
   * @see {@link JacksonFactory#newObjectMapper()}
   */
  public static final ObjectMapper newObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setDateFormat(dateFormatter());
    objectMapper.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }

  /**
   * Get a shared instance of the ObjectMapper to use.
   *
   * @return a shared object mapper instance
   */
  public static ObjectMapper objectMapper() {
    if (OM == null) {
      OM = newObjectMapper();
    }
    return OM;
  }

  public static final RequestSourceValidator requestSourceValidator(Map<String, String> env) {
    boolean validate =
        ConfigUtil.getBooleanSetting(
            env, Lambda.ENV_CALLBACK_SOURCE_IPS_VALIDATE, Lambda.DEFAULT_SOURCE_IPS_VALIDATE);
    List<String> callbackIps =
        ConfigUtil.getStringListSetting(
            env, Lambda.ENV_CALLBACK_SOURCE_IPS, Lambda.DEFAULT_SOURCE_IPS);
    return new RequestSourceValidator(callbackIps, validate);
  }
}

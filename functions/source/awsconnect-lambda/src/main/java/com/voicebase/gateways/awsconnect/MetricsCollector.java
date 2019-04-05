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

import java.util.Map;
import java.util.concurrent.TimeUnit;

/** @author Volker Kueffel <volker@voicebase.com> */
public interface MetricsCollector {

  void addCount(String metricName, Number value);

  void addCount(String metricName, Number value, Map<String, String> dimensions);

  void addCount(String metricName, Number value, String... dimensions);

  /**
   * Add timing metric.
   *
   * @param metricName name of metric
   * @param value metric value, significant only guaranteed for long values, allows a Number here
   *     for convenience
   * @param timeUnit time unit
   */
  void addTiming(String metricName, Number value, TimeUnit timeUnit);

  /**
   * Add timing metric.
   *
   * @param metricName name of metric
   * @param value metric value, significant only guaranteed for long values, allows a Number here
   *     for convenience
   * @param timeUnit time unit
   * @param dimensions metric dimensions
   */
  void addTiming(
      String metricName, Number value, TimeUnit timeUnit, Map<String, String> dimensions);

  void addTiming(String metricName, Number value, TimeUnit timeUnit, String... dimensions);

  /** If the implementation uses a buffer, flush it. */
  void flush();
}

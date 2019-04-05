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
package com.voicebase.gateways.awsconnect.forward;

public class ExponentialBackoffSettings {
  public static final int DEFAULT_INITIAL_DELAY_SECS = 15;
  public static final double DEFAULT_EXPONENTIAL_FACTOR = 2.0;
  /**
   * Maximum allowed delay for messages queued to SQS. Note that SQS only allows messages to be
   * delayed up to 15 minutes (900 secs).
   */
  public static final int MAXIMUM_ALLOWED_DELAY_SECS = 900;

  private int initialDelayInSeconds = DEFAULT_INITIAL_DELAY_SECS;
  private int maximumAllowedDelayInSeconds = MAXIMUM_ALLOWED_DELAY_SECS;
  private double exponentialFactor = DEFAULT_EXPONENTIAL_FACTOR;

  /** @return the initialDelayInSeconds */
  public int getInitialDelayInSeconds() {
    return initialDelayInSeconds;
  }

  /** @param initialDelayInSeconds the initialDelayInSeconds to set */
  public void setInitialDelayInSeconds(int initialDelayInSeconds) {
    this.initialDelayInSeconds = initialDelayInSeconds;
  }

  /** @return the maximumAllowedDelayInSeconds */
  public int getMaximumAllowedDelayInSeconds() {
    return maximumAllowedDelayInSeconds;
  }

  /** @param maximumAllowedDelayInSeconds the maximumAllowedDelayInSeconds to set */
  public void setMaximumAllowedDelayInSeconds(int maximumAllowedDelayInSeconds) {
    this.maximumAllowedDelayInSeconds = maximumAllowedDelayInSeconds;
  }

  /** @return the exponentialFactor */
  public double getExponentialFactor() {
    return exponentialFactor;
  }

  /** @param exponentialFactor the exponentialFactor to set */
  public void setExponentialFactor(double exponentialFactor) {
    this.exponentialFactor = exponentialFactor;
  }
}

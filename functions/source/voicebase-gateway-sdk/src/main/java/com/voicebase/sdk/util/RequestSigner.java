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
 */
package com.voicebase.sdk.util;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class RequestSigner {
  private static final Logger LOGGER = LoggerFactory.getLogger(RequestSigner.class);
  private static final String HMAC_ALGORITHM = "HmacSHA256";

  private String secret;
  // Should make this configuable
  private long signatureLifetimeSeconds = 60 * 60;

  private final boolean algorithmAvailable;

  public RequestSigner(String secret) {
    boolean works = false;
    try {
      if (StringUtils.isBlank(secret)) {
        throw new NullPointerException("API key must be provided to validate requests");
      }
      this.secret = secret;
      // check if algorithm is available
      @SuppressWarnings("unused")
      Mac sha256HMAC = Mac.getInstance(HMAC_ALGORITHM);
      works = true;
      LOGGER.info("{} algorithm available. ", HMAC_ALGORITHM);
    } catch (NoSuchAlgorithmException e) {
      LOGGER.warn("{} algorithm not available; cannot verify request parameters.", HMAC_ALGORITHM);
    } catch (NullPointerException e) {
      LOGGER.warn("{}; cannot verify request parameters.", e.getMessage());
    }
    algorithmAvailable = works;

  }

  public long getSignatureLifetimeSeconds() {
    return signatureLifetimeSeconds;
  }

  public void setSignatureLifetimeSeconds(long signatureLifetimeSeconds) {
    this.signatureLifetimeSeconds = signatureLifetimeSeconds;
    LOGGER.debug("Signatures valid for {}s", signatureLifetimeSeconds);
  }

  public RequestSigner withSignatureLifetimeMillis(long signatureLifetimeMillis) {
    setSignatureLifetimeSeconds(signatureLifetimeMillis);
    return this;
  }

  public boolean canValidate() {
    return algorithmAvailable;
  }

  /**
   * 
   * @param timestamp
   *          Seconds since 1970-01-01
   * @param token
   * @param signature
   * @return
   */
  public boolean validate(long timestamp, String token, String signature) {
    long signatureValidUntil = (timestamp + signatureLifetimeSeconds) * 1000;
    if (signatureLifetimeSeconds > 0 && signatureValidUntil <= System.currentTimeMillis()) {
      LOGGER.debug(
          "Request timestamp too old, only accept if timestamp is at most {}s in the past. Timestamp: {}, min acceptable {}",
          signatureLifetimeSeconds, timestamp, signatureValidUntil);
      return false;
    }

    String hmac = calculateSignature(timestamp, token);
    boolean validated = hmac.equalsIgnoreCase(signature);

    LOGGER.trace("Request parameters validated: {}", validated);

    return validated;
  }

  public String calculateSignature(long timestamp, String token) {
    String hmac = null;
    if (algorithmAvailable) {
      try {
        Mac sha256HMAC = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec key = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        sha256HMAC.init(key);

        String input = String.valueOf(timestamp) + token;
        hmac = Hex.encodeHexString(sha256HMAC.doFinal(input.getBytes()));
      } catch (Exception e) {
        LOGGER.debug("Error calculating signature.", e);
      }
    }
    return hmac;
  }

  public SignatureParameters createSignature() {

    long timestamp = new Date().getTime();
    String token = RandomStringUtils.randomAlphanumeric(50);
    String signature = calculateSignature(timestamp, token);
    return new SignatureParameters().withTimestamp(timestamp).withToken(token).withSignature(signature);
  }

  public static final class SignatureParameters {
    private long timestamp;
    private String token;
    private String signature;

    public long getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(long timestamp) {
      this.timestamp = timestamp;
    }

    public SignatureParameters withTimestamp(long timestamp) {
      setTimestamp(timestamp);
      return this;
    }

    public String getToken() {
      return token;
    }

    public void setToken(String token) {
      this.token = token;
    }

    public SignatureParameters withToken(String token) {
      setToken(token);
      return this;
    }

    public String getSignature() {
      return signature;
    }

    public void setSignature(String signature) {
      this.signature = signature;
    }

    public SignatureParameters withSignature(String signature) {
      setSignature(signature);
      return this;
    }
  }

}

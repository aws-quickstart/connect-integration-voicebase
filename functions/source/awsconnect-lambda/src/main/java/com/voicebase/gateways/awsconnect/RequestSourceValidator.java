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
package com.voicebase.gateways.awsconnect;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validator for incoming requests.
 * 
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class RequestSourceValidator {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestSourceValidator.class);

  private final boolean validate;
  private final Set<String> ipWhitelist = new HashSet<>();

  public RequestSourceValidator(Collection<String> ipWhitelist, boolean validate) {
    if (ipWhitelist != null && !ipWhitelist.isEmpty()) {
      this.ipWhitelist.addAll(ipWhitelist);
    }
    this.validate = validate;
  }

  public boolean validate(String sourceIp) {
    if (!validate) {
      return true;
    }

    if (!ipWhitelist.isEmpty()) {
      try {
        LOGGER.debug("Incoming request from {}", sourceIp);
        if (StringUtils.isEmpty(sourceIp) || !ipWhitelist.contains(sourceIp)) {
          LOGGER.warn("Request from {} not authorized, rejecting.", sourceIp);
          return false;
        }
      } catch (Exception e) {
        LOGGER.error("Unable to validate request source.", e);
        return false;
      }
    }

    return true;
  }

}

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
package com.voicebase.gateways.awsconnect.forward;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author Volker Kueffel <volker@voicebase.com>
 *
 */
public class CallbackProvider {

  private static Logger LOGGER = LoggerFactory.getLogger(CallbackProvider.class);

  private String callbackUrl;
  private String callbackMethod;
  private Set<String> includes;
  private Set<String> additionalCallbackUrls;


  public void setIncludes(String[] includes) {
    this.includes = new HashSet<>();
    if (includes != null) {
      for (int i = 0; i < includes.length; i++) {
        this.includes.add(includes[i]);
      }
    }
  }

  public void setIncludes(Iterable<String> includes) {
    this.includes = new HashSet<>();
    if (includes != null) {
      for (String include : includes) {
        if (!StringUtils.isEmpty(include)) {
          this.includes.add(include);
        }
      }

    }
  }

  public boolean hasIncludes() {
    return includes != null && !includes.isEmpty();
  }

  public void setAdditionalCallbackUrls(Iterable<String> additionalCallbackUrls) {
    if (this.additionalCallbackUrls == null) {
      this.additionalCallbackUrls = new HashSet<>();
    } else {
      this.additionalCallbackUrls.clear();
    }
    if (additionalCallbackUrls != null) {
      for (String additionalUrl : additionalCallbackUrls) {
        this.additionalCallbackUrls.add(additionalUrl);
      }
    }
  }

  public Set<String> getAdditionalCallbackUrls() {
    return this.additionalCallbackUrls;
  }

  public boolean hasAdditionalCallbackUrls() {
    return additionalCallbackUrls != null && !additionalCallbackUrls.isEmpty();
  }

  public String getCallbackUrl() {
    return callbackUrl;
  }

  public void setCallbackUrl(String callbackUrl) {
    this.callbackUrl = callbackUrl;
    LOGGER.info("Generating callback URLs with base {}", this.callbackUrl);
  }

  public String getCallbackMethod() {
    return callbackMethod;
  }

  public void setCallbackMethod(String callbackMethod) {
    this.callbackMethod = callbackMethod;
  }

  public Set<String> getIncludes() {
    return includes;
  }

  public void setIncludes(Set<String> includes) {
    this.includes = includes;
  }


}

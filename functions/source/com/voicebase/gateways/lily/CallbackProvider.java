package com.voicebase.gateways.lily;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voicebase.sdk.processing.Callback;
import com.voicebase.sdk.util.RequestSigner;
import com.voicebase.sdk.util.RequestSigner.SignatureParameters;

/**
 * Generator for Voicebase API callback URLs.
 * 
 * @author volker@voicebase.com
 *
 */
public class CallbackProvider {

  private static Logger LOGGER = LoggerFactory.getLogger(CallbackProvider.class);

  private String callbackUrl;
  private String callbackMethod;
  private Set<String> includes;
  private boolean urlHasParameters = false;
  private Set<String> additionalCallbackUrls;

  private RequestSigner requestSigner;

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
    urlHasParameters = callbackUrl.contains("?");
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

  public RequestSigner getRequestSigner() {
    return requestSigner;
  }

  public void setRequestSigner(RequestSigner requestSigner) {
    this.requestSigner = requestSigner;
  }

  public Callback newCallback() {
    Callback callback = new Callback();
    callback.setMethod(callbackMethod);
    callback.setInclude(includes);

    StringBuilder urlBuilder = new StringBuilder(64).append(callbackUrl);
    if (requestSigner != null && requestSigner.canValidate()) {
      if (urlHasParameters) {
        urlBuilder.append("&");
      } else {
        urlBuilder.append("?");
      }
      SignatureParameters params = requestSigner.createSignature();
      urlBuilder.append("timestamp=").append(params.getTimestamp()).append("&");
      urlBuilder.append("token=").append(params.getToken()).append("&");
      urlBuilder.append("signature=").append(params.getSignature());
    }
    callback.setUrl(urlBuilder.toString());
    return callback;
  }

  public Set<Callback> getAdditionalCallbacks() {
    HashSet<Callback> callbacks = new HashSet<>();
    if (additionalCallbackUrls != null) {
      for (String additionalUrl : additionalCallbackUrls) {
        Callback callback = new Callback();
        callback.setUrl(additionalUrl);
        callback.setMethod(callbackMethod);
        callback.setInclude(includes);
      }
    }
    return callbacks;
  }

}

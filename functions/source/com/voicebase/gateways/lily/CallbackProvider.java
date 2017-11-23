package com.voicebase.gateways.lily;

import java.util.HashSet;
import java.util.Set;

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

  private RequestSigner requestSigner;

  public void setIncludes(String[] includes) {
    this.includes = new HashSet<>();
    if (includes != null) {
      for (int i = 0; i < includes.length; i++) {
        this.includes.add(includes[i]);
      }
    }
  }
  
  
  public void setIncludes(Iterable<String>includes) {
    this.includes = new HashSet<>();
    if (includes != null) {
      for (String include : includes) {
        this.includes.add(include);
      }
      
    }
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
}

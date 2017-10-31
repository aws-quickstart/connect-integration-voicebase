package com.voicebase.gateways.lily;

public final class ServiceResponse {

  public static final ServiceResponse SUCCESS = new ServiceResponse().withSuccess(true);
  public static final ServiceResponse FAILURE = new ServiceResponse().withSuccess(false);

  private boolean success;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public ServiceResponse withSuccess(boolean success) {
    setSuccess(success);
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (success ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ServiceResponse other = (ServiceResponse) obj;
    if (success != other.success)
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ServiceResponse [success=");
    builder.append(success);
    builder.append("]");
    return builder.toString();
  }

}
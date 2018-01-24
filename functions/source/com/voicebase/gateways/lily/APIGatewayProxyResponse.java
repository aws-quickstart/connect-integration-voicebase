package com.voicebase.gateways.lily;

import java.util.Map;

public class APIGatewayProxyResponse {
  private Map<String, String> headers;
  private int statusCode = 200;
  private String body;
  private boolean isBase64Encoded = false;

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public APIGatewayProxyResponse withStatusCode(int statusCode) {
    setStatusCode(statusCode);
    return this;
  }

  public String getBody() {
    return body;
  }

  public void setBody(String body) {
    this.body = body;
  }

  public APIGatewayProxyResponse withBody(String body) {
    setBody(body);
    return this;
  }

  public boolean isBase64Encoded() {
    return isBase64Encoded;
  }

  public void setBase64Encoded(boolean isBase64Encoded) {
    this.isBase64Encoded = isBase64Encoded;
  }

  public APIGatewayProxyResponse withBase64Encoded(boolean isBase64Encoded) {
    setBase64Encoded(isBase64Encoded);
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((body == null) ? 0 : body.hashCode());
    result = prime * result + ((headers == null) ? 0 : headers.hashCode());
    result = prime * result + (isBase64Encoded ? 1231 : 1237);
    result = prime * result + statusCode;
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
    APIGatewayProxyResponse other = (APIGatewayProxyResponse) obj;
    if (body == null) {
      if (other.body != null)
        return false;
    } else if (!body.equals(other.body))
      return false;
    if (headers == null) {
      if (other.headers != null)
        return false;
    } else if (!headers.equals(other.headers))
      return false;
    if (isBase64Encoded != other.isBase64Encoded)
      return false;
    if (statusCode != other.statusCode)
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("APIGatewayProxyResponse [headers=");
    builder.append(headers);
    builder.append(", statusCode=");
    builder.append(statusCode);
    builder.append(", body=");
    builder.append(body);
    builder.append(", isBase64Encoded=");
    builder.append(isBase64Encoded);
    builder.append("]");
    return builder.toString();
  }

}

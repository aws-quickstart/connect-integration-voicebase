package com.voicebase.sdk.v3;

import java.io.File;

import com.voicebase.api.model.VbConfiguration;
import com.voicebase.api.model.VbMetadata;

public class MediaProcessingRequest {

  private VbConfiguration configuration;
  private VbMetadata metadata;
  private File mediaFile;
  private String mediaUrl;

  public VbConfiguration getConfiguration() {
    return configuration;
  }

  public void setConfiguration(VbConfiguration configuration) {
    this.configuration = configuration;
  }
  
  public MediaProcessingRequest withConfiguration(VbConfiguration configuration) {
      setConfiguration(configuration);
      return this;
  }

  public VbMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(VbMetadata metadata) {
    this.metadata = metadata;
  }

  public MediaProcessingRequest withMetadata(VbMetadata metadata) {
    setMetadata(metadata);
    return this;
  }
  
  public File getMediaFile() {
    return mediaFile;
  }

  public void setMediaFile(File mediaFile) {
    this.mediaFile = mediaFile;
  }

  public String getMediaUrl() {
    return mediaUrl;
  }

  public void setMediaUrl(String mediaUrl) {
    this.mediaUrl = mediaUrl;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
    result = prime * result + ((mediaFile == null) ? 0 : mediaFile.hashCode());
    result = prime * result + ((mediaUrl == null) ? 0 : mediaUrl.hashCode());
    result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
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
    MediaProcessingRequest other = (MediaProcessingRequest) obj;
    if (configuration == null) {
      if (other.configuration != null)
        return false;
    } else if (!configuration.equals(other.configuration))
      return false;
    if (mediaFile == null) {
      if (other.mediaFile != null)
        return false;
    } else if (!mediaFile.equals(other.mediaFile))
      return false;
    if (mediaUrl == null) {
      if (other.mediaUrl != null)
        return false;
    } else if (!mediaUrl.equals(other.mediaUrl))
      return false;
    if (metadata == null) {
      if (other.metadata != null)
        return false;
    } else if (!metadata.equals(other.metadata))
      return false;
    return true;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("MediaProcessingRequest [configuration=");
    builder.append(configuration);
    builder.append(", metadata=");
    builder.append(metadata);
    builder.append(", mediaFile=");
    builder.append(mediaFile);
    builder.append(", mediaUrl=");
    builder.append(mediaUrl);
    builder.append("]");
    return builder.toString();
  }

}

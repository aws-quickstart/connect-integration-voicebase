package com.voicebase.sdk.processing;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class MediaBase {

	@JsonProperty("metadata")
	protected MediaMetadata metadata;
	@JsonProperty("mediaId")
	protected String mediaId;
	@JsonProperty("status")
	protected String status;
	@JsonProperty("dateCreated")
	protected Date dateCreated;


	@JsonProperty("metadata")
	public MediaMetadata getMetadata() {
		return metadata;
	}

	@JsonProperty("metadata")
	public void setMetadata(MediaMetadata metadata) {
		this.metadata = metadata;
	}

	@JsonProperty("mediaId")
	public String getMediaId() {
		return mediaId;
	}

	@JsonProperty("mediaId")
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("dateCreated")
	public Date getDateCreated() {
		return dateCreated;
	}

	@JsonProperty("dateCreated")
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((mediaId == null) ? 0 : mediaId.hashCode());
		result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		MediaBase other = (MediaBase) obj;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (mediaId == null) {
			if (other.mediaId != null)
				return false;
		} else if (!mediaId.equals(other.mediaId))
			return false;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MediaBase [metadata=");
		builder.append(metadata);
		builder.append(", mediaId=");
		builder.append(mediaId);
		builder.append(", status=");
		builder.append(status);
		builder.append(", dateCreated=");
		builder.append(dateCreated);
		builder.append("]");
		return builder.toString();
	}

}
package com.voicebase.sdk.processing;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author volker@voicebase.com
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MediaProcessingResult {
	
	@JsonProperty("_links")
	private Map<String, Link> links;

	@JsonProperty("metadata")
	private Metadata metadata;

	@JsonProperty("status")
	private String status;

	@JsonProperty("mediaId")
	private String mediaId;

	public Map<String, Link> getLinks() {
		return links;
	}

	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((links == null) ? 0 : links.hashCode());
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
		MediaProcessingResult other = (MediaProcessingResult) obj;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
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
		builder.append("MediaProcessingResult [links=");
		builder.append(links);
		builder.append(", metadata=");
		builder.append(metadata);
		builder.append(", status=");
		builder.append(status);
		builder.append(", mediaId=");
		builder.append(mediaId);
		builder.append("]");
		return builder.toString();
	}

}

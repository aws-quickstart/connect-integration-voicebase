package com.voicebase.sdk.processing;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListMediaResult {
	@JsonProperty("_links")
	private Map<String, Link> links;

	@JsonProperty("media")
	private List<ReportMedia> mediaList;

	@JsonProperty("media")
	public List<ReportMedia> getMediaList() {
		return mediaList;
	}

	@JsonProperty("media")
	public void setMediaList(List<ReportMedia> mediaList) {
		this.mediaList = mediaList;
	}

	@JsonProperty("_links")
	public Map<String, Link> getLinks() {
		return links;
	}

	@JsonProperty("_links")
	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		result = prime * result + ((mediaList == null) ? 0 : mediaList.hashCode());
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
		ListMediaResult other = (ListMediaResult) obj;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		if (mediaList == null) {
			if (other.mediaList != null)
				return false;
		} else if (!mediaList.equals(other.mediaList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListMediaResult [links=");
		builder.append(links);
		builder.append(", mediaList=");
		builder.append(mediaList);
		builder.append("]");
		return builder.toString();
	}

}

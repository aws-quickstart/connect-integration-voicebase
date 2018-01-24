package com.voicebase.sdk.processing;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportMedia extends MediaBase {
	@JsonProperty("transcripts")
	private Map<String, Transcript> transcripts;
	@JsonProperty("keywords")
	private Map<String, Keywords> keywords;
	@JsonProperty("topics")
	private Map<String, Topics> topics;

	public Map<String, Transcript> getTranscripts() {
		return transcripts;
	}

	public void setTranscripts(Map<String, Transcript> transcripts) {
		this.transcripts = transcripts;
	}

	public Map<String, Keywords> getKeywords() {
		return keywords;
	}

	public void setKeywords(Map<String, Keywords> keywords) {
		this.keywords = keywords;
	}

	public Map<String, Topics> getTopics() {
		return topics;
	}

	public void setTopics(Map<String, Topics> topics) {
		this.topics = topics;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((keywords == null) ? 0 : keywords.hashCode());
		result = prime * result + ((topics == null) ? 0 : topics.hashCode());
		result = prime * result + ((transcripts == null) ? 0 : transcripts.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportMedia other = (ReportMedia) obj;
		if (keywords == null) {
			if (other.keywords != null)
				return false;
		} else if (!keywords.equals(other.keywords))
			return false;
		if (topics == null) {
			if (other.topics != null)
				return false;
		} else if (!topics.equals(other.topics))
			return false;
		if (transcripts == null) {
			if (other.transcripts != null)
				return false;
		} else if (!transcripts.equals(other.transcripts))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReportMedia [transcripts=");
		builder.append(transcripts);
		builder.append(", keywords=");
		builder.append(keywords);
		builder.append(", topics=");
		builder.append(topics);
		builder.append(", getMetadata()=");
		builder.append(getMetadata());
		builder.append(", getMediaId()=");
		builder.append(getMediaId());
		builder.append(", getStatus()=");
		builder.append(getStatus());
		builder.append(", getDateCreated()=");
		builder.append(getDateCreated());
		builder.append("]");
		return builder.toString();
	}

}

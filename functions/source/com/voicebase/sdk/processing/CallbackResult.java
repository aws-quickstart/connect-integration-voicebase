
package com.voicebase.sdk.processing;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "_links",
    "callback",
    "media",
    "transcripts"
})
public class CallbackResult {

    @JsonProperty("_links")
    private Map<String,Link> links;
    @JsonProperty("callback")
    private Status status;
    @JsonProperty("media")
    private Media media;
    @JsonProperty("transcripts")
    private Transcript transcript;
    

    @JsonProperty("_links")
    public Map<String, Link> getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    @JsonProperty("callback")
    public Status getStatus() {
        return status;
    }

    @JsonProperty("callback")
    public void setStatus(Status callback) {
        this.status = callback;
    }

    @JsonProperty("media")
    public Media getMedia() {
        return media;
    }

    @JsonProperty("media")
    public void setMedia(Media media) {
        this.media = media;
    }

    @JsonProperty("transcripts")
    public Transcript getTranscript() {
		return transcript;
	}

    @JsonProperty("transcripts")
	public void setTranscript(Transcript transcript) {
		this.transcript = transcript;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		result = prime * result + ((media == null) ? 0 : media.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((transcript == null) ? 0 : transcript.hashCode());
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
		CallbackResult other = (CallbackResult) obj;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		if (media == null) {
			if (other.media != null)
				return false;
		} else if (!media.equals(other.media))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (transcript == null) {
			if (other.transcript != null)
				return false;
		} else if (!transcript.equals(other.transcript))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CallbackResult [links=");
		builder.append(links);
		builder.append(", status=");
		builder.append(status);
		builder.append(", media=");
		builder.append(media);
		builder.append(", transcript=");
		builder.append(transcript);
		builder.append("]");
		return builder.toString();
	}



}


package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "metadata", "transcripts", "mediaId", "status" })
public class Media extends MediaBase {

	@JsonProperty("transcripts")
	private Transcript transcripts;
	@JsonProperty("transcripts")
	public Transcript getTranscripts() {
		return transcripts;
	}

	@JsonProperty("transcripts")
	public void setTranscripts(Transcript transcripts) {
		this.transcripts = transcripts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
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
		Media other = (Media) obj;
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
		builder.append("Media [transcripts=");
		builder.append(transcripts);
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

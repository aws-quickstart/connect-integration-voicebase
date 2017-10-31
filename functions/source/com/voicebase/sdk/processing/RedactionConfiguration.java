package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class RedactionConfiguration {

	@JsonProperty("transcripts")
	private String transcriptRedaction;
	@JsonProperty("audio")
	private AudioRedaction audioRedaction;

	public String getTranscriptRedaction() {
		return transcriptRedaction;
	}

	public void setTranscriptRedaction(String transcripts) {
		this.transcriptRedaction = transcripts;
	}

	public RedactionConfiguration withTranscriptRedaction(String transcripts) {
		setTranscriptRedaction(transcripts);
		return this;
	}

	public AudioRedaction getAudioRedaction() {
		return audioRedaction;
	}

	public void setAudioRedaction(AudioRedaction audio) {
		this.audioRedaction = audio;
	}

	public RedactionConfiguration withAudioRedaction(AudioRedaction audio) {
		setAudioRedaction(audio);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((audioRedaction == null) ? 0 : audioRedaction.hashCode());
		result = prime * result + ((transcriptRedaction == null) ? 0 : transcriptRedaction.hashCode());
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
		RedactionConfiguration other = (RedactionConfiguration) obj;
		if (audioRedaction == null) {
			if (other.audioRedaction != null)
				return false;
		} else if (!audioRedaction.equals(other.audioRedaction))
			return false;
		if (transcriptRedaction == null) {
			if (other.transcriptRedaction != null)
				return false;
		} else if (!transcriptRedaction.equals(other.transcriptRedaction))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RedactionConfiguration [transcriptRedaction=");
		builder.append(transcriptRedaction);
		builder.append(", audio=");
		builder.append(audioRedaction);
		builder.append("]");
		return builder.toString();
	}

}

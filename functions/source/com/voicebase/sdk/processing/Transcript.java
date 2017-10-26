
package com.voicebase.sdk.processing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "srt",
    "text",
    "confidence",
    "words"
})
public class Transcript {

    @JsonProperty("srt")
    private String srt;
    @JsonProperty("text")
    private String text;
    @JsonProperty("confidence")
    private double confidence;
    @JsonProperty("words")
    private List<Word> words;

    @JsonProperty("srt")
    public String getSrt() {
        return srt;
    }

    @JsonProperty("srt")
    public void setSrt(String srt) {
        this.srt = srt;
    }


    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

	public List<Word> getWords() {
		return words;
	}

	public void setWords(List<Word> words) {
		this.words = words;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(confidence);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((srt == null) ? 0 : srt.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
		result = prime * result + ((words == null) ? 0 : words.hashCode());
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
		Transcript other = (Transcript) obj;
		if (Double.doubleToLongBits(confidence) != Double.doubleToLongBits(other.confidence))
			return false;
		if (srt == null) {
			if (other.srt != null)
				return false;
		} else if (!srt.equals(other.srt))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (words == null) {
			if (other.words != null)
				return false;
		} else if (!words.equals(other.words))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transcript [srt=");
		builder.append(srt);
		builder.append(", text=");
		builder.append(text);
		builder.append(", confidence=");
		builder.append(confidence);
		builder.append(", words=");
		builder.append(words);
		builder.append("]");
		return builder.toString();
	}

}

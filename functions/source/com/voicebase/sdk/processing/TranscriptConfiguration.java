package com.voicebase.sdk.processing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class TranscriptConfiguration {

	@JsonProperty("formatNumbers")
	private Boolean formatNumbers;

	@JsonProperty("swearFilter")
	private Boolean filterSwearWords;

	@JsonProperty("vocabularies")
	private List<Vocabulary> vocabularies = null;

	public Boolean getFormatNumbers() {
		return formatNumbers;
	}

	public void setFormatNumbers(Boolean numberFormat) {
		this.formatNumbers = numberFormat;
	}

	public TranscriptConfiguration withFormatNumbers(Boolean numberFormat) {
		setFormatNumbers(numberFormat);
		return this;
	}

	public Boolean getFilterSwearWords() {
		return filterSwearWords;
	}

	public void setFilterSwearWords(Boolean swearFilter) {
		this.filterSwearWords = swearFilter;
	}

	public TranscriptConfiguration withFilterSwearWords(Boolean swearFilter) {
		setFilterSwearWords(swearFilter);
		return this;
	}

	public List<Vocabulary> getVocabularies() {
		return vocabularies;
	}

	public void setVocabularies(List<Vocabulary> vocabularies) {
		this.vocabularies = vocabularies;
	}

	public TranscriptConfiguration withVocabularies(List<Vocabulary> vocabularies) {
		setVocabularies(vocabularies);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((filterSwearWords == null) ? 0 : filterSwearWords.hashCode());
		result = prime * result + ((formatNumbers == null) ? 0 : formatNumbers.hashCode());
		result = prime * result + ((vocabularies == null) ? 0 : vocabularies.hashCode());
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
		TranscriptConfiguration other = (TranscriptConfiguration) obj;
		if (filterSwearWords == null) {
			if (other.filterSwearWords != null)
				return false;
		} else if (!filterSwearWords.equals(other.filterSwearWords))
			return false;
		if (formatNumbers == null) {
			if (other.formatNumbers != null)
				return false;
		} else if (!formatNumbers.equals(other.formatNumbers))
			return false;
		if (vocabularies == null) {
			if (other.vocabularies != null)
				return false;
		} else if (!vocabularies.equals(other.vocabularies))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TranscriptConfiguration [formatNumbers=");
		builder.append(formatNumbers);
		builder.append(", filterSwearWords=");
		builder.append(filterSwearWords);
		builder.append(", vocabularies=");
		builder.append(vocabularies);
		builder.append("]");
		return builder.toString();
	}
}

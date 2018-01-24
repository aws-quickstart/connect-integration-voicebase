package com.voicebase.sdk.processing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Keywords {
	@JsonProperty
	private List<Keyword> words;
	@JsonProperty
	private List<Object> groups;

	public List<Keyword> getWords() {
		return words;
	}

	public void setWords(List<Keyword> words) {
		this.words = words;
	}

	public List<Object> getGroups() {
		return groups;
	}

	public void setGroups(List<Object> groups) {
		this.groups = groups;
	}
}

package com.voicebase.sdk.processing;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class Vocabulary {
	
	@JsonProperty("name")
	private String name=null;
	
	@JsonProperty("terms")
	private List<String>terms=null;

	public List<String> getTerms() {
		return terms;
	}

	public void setTerms(List<String> terms) {
		this.terms = terms;
	}
	
	public Vocabulary withTerms(List<String> terms) {
		setTerms(terms);
		return this;
	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vocabulary withName(String name) {
		setName(name);
		return this;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((terms == null) ? 0 : terms.hashCode());
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
		Vocabulary other = (Vocabulary) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (terms == null) {
			if (other.terms != null)
				return false;
		} else if (!terms.equals(other.terms))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Vocabulary [name=");
		builder.append(name);
		builder.append(", terms=");
		builder.append(terms);
		builder.append("]");
		return builder.toString();
	}
	
	
}

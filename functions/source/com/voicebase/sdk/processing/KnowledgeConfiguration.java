package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author volker@voicebase.com
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class KnowledgeConfiguration {
	private boolean semantic=true;

	public boolean isSemantic() {
		return semantic;
	}

	public void setSemantic(boolean semantic) {
		this.semantic = semantic;
	}
	
	public KnowledgeConfiguration withSemantic(boolean semantic) {
		setSemantic(semantic);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (semantic ? 1231 : 1237);
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
		KnowledgeConfiguration other = (KnowledgeConfiguration) obj;
		if (semantic != other.semantic)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KnowledgeConfiguration [semantic=");
		builder.append(semantic);
		builder.append("]");
		return builder.toString();
	}
}

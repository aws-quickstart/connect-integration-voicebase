package com.voicebase.sdk.processing;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author volker@voicebase.com
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class KeywordConfiguration extends KnowledgeConfiguration {
	private Set<String> groups;

	public Set<String> getGroups() {
		return groups;
	}

	public void setGroups(Set<String> groups) {
		this.groups = groups;
	}
	
	public KeywordConfiguration withGroups(Set<String> groups) {
		setGroups(groups);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
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
		KeywordConfiguration other = (KeywordConfiguration) obj;
		if (groups == null) {
			if (other.groups != null)
				return false;
		} else if (!groups.equals(other.groups))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KeywordConfiguration [groups=");
		builder.append(groups);
		builder.append(", isSemantic()=");
		builder.append(isSemantic());
		builder.append("]");
		return builder.toString();
	}
	
	
	
}

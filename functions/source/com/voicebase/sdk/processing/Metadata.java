package com.voicebase.sdk.processing;

import java.util.Map;
import java.util.TreeMap;

import com.voicebase.sdk.Identifier;

public class Metadata {
	private Identifier external;
	private final Map<String, Object> extended = new TreeMap<>();

	public Identifier getExternal() {
		return external;
	}

	public void setExternal(Identifier external) {
		this.external = external;
	}

	public Metadata withExternal(Identifier external) {
		setExternal(external);
		return this;
	}

	public Map<String, Object> getExtended() {
		return extended;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((extended == null) ? 0 : extended.hashCode());
		result = prime * result + ((external == null) ? 0 : external.hashCode());
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
		Metadata other = (Metadata) obj;
		if (extended == null) {
			if (other.extended != null)
				return false;
		} else if (!extended.equals(other.extended))
			return false;
		if (external == null) {
			if (other.external != null)
				return false;
		} else if (!external.equals(other.external))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Metadata [external=");
		builder.append(external);
		builder.append(", extended=");
		builder.append(extended);
		builder.append("]");
		return builder.toString();
	}

}
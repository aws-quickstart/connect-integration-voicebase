package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * API requests need the Metadata object to be wrapped in another object with
 * one property called "metadata". To avoid having that complexity on the object
 * model, the client class wraps the object into this one before sending it out
 * over HTTP.
 * 
 * @author volker@voicebase.com
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class MetadataWrapper {
	private Metadata metadata;

	public MetadataWrapper() {
	}

	public MetadataWrapper(Metadata metadata) {
		this.metadata = metadata;
	}

	public Metadata getMetadata() {
		return metadata;
	}

	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metadata == null) ? 0 : metadata.hashCode());
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
		MetadataWrapper other = (MetadataWrapper) obj;
		if (metadata == null) {
			if (other.metadata != null)
				return false;
		} else if (!metadata.equals(other.metadata))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetadataWrapper [metadata=");
		builder.append(metadata);
		builder.append("]");
		return builder.toString();
	}

}

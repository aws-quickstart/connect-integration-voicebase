package com.voicebase.sdk.processing;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class PublishConfiguration {

	@JsonProperty("callbacks")
	private final List<Callback> callbacks = new ArrayList<>();

	public List<Callback> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(List<Callback> callbacks) {
		this.callbacks.clear();
		if (callbacks != null) {
			this.callbacks.addAll(callbacks);
		}
	}

	public PublishConfiguration addCallback(Callback callback) {
		if (callback != null) {
			callbacks.add(callback);
		}
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callbacks == null) ? 0 : callbacks.hashCode());
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
		PublishConfiguration other = (PublishConfiguration) obj;
		if (callbacks == null) {
			if (other.callbacks != null)
				return false;
		} else if (!callbacks.equals(other.callbacks))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PublishConfiguration [callbacks=");
		builder.append(callbacks);
		builder.append("]");
		return builder.toString();
	}

}

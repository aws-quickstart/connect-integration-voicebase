package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * API requests need the Configuration object to be wrapped in another object
 * with one property called "configuration". To avoid having that complexity on
 * the object model, the client class wraps the object into this one before
 * sending it out over HTTP.
 * 
 * @author volker@voicebase.com
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class ConfigurationWrapper {
	private Configuration configuration;

	public ConfigurationWrapper() {
		super();
	}

	public ConfigurationWrapper(Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((configuration == null) ? 0 : configuration.hashCode());
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
		ConfigurationWrapper other = (ConfigurationWrapper) obj;
		if (configuration == null) {
			if (other.configuration != null)
				return false;
		} else if (!configuration.equals(other.configuration))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConfigurationWrapper [configuration=");
		builder.append(configuration);
		builder.append("]");
		return builder.toString();
	}
}

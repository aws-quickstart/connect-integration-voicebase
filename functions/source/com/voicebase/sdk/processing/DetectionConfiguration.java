package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author volker@voicebase.com
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class DetectionConfiguration {
	@JsonProperty("model")
	private String model;
	@JsonProperty("redact")
	private RedactionConfiguration redactionConfiguration;

	public String getModel() {
		return model;
	}

	@JsonSetter
	public void setModel(String model) {
		this.model=model;
	}
	
	public DetectionConfiguration withModel(String model) {
		setModel(model);
		return this;
	}
	
	public void setModel(DetectionModel model) {
		if (model==null) {
			setModel((String)null);
		} else {
			setModel(model.name());
		}
	}
	
	public DetectionConfiguration withModel(DetectionModel model) {
		setModel(model);
		return this;
	}

	public RedactionConfiguration getRedactionConfiguration() {
		return redactionConfiguration;
	}

	public void setRedactionConfiguration(RedactionConfiguration redact) {
		this.redactionConfiguration = redact;
	}
	
	public DetectionConfiguration withRedactionConfiguration(RedactionConfiguration redact) {
		setRedactionConfiguration(redact);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((model == null) ? 0 : model.hashCode());
		result = prime * result + ((redactionConfiguration == null) ? 0 : redactionConfiguration.hashCode());
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
		DetectionConfiguration other = (DetectionConfiguration) obj;
		if (model != other.model)
			return false;
		if (redactionConfiguration == null) {
			if (other.redactionConfiguration != null)
				return false;
		} else if (!redactionConfiguration.equals(other.redactionConfiguration))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DetectionConfiguration [model=");
		builder.append(model);
		builder.append(", redactionConfiguration=");
		builder.append(redactionConfiguration);
		builder.append("]");
		return builder.toString();
	}

}

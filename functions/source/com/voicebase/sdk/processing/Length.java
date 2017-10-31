
package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "milliseconds", "descriptive" })
public class Length {

	@JsonProperty("milliseconds")
	private Integer milliseconds;
	@JsonProperty("descriptive")
	private String descriptive;

	@JsonProperty("milliseconds")
	public Integer getMilliseconds() {
		return milliseconds;
	}

	@JsonProperty("milliseconds")
	public void setMilliseconds(Integer milliseconds) {
		this.milliseconds = milliseconds;
	}

	@JsonProperty("descriptive")
	public String getDescriptive() {
		return descriptive;
	}

	@JsonProperty("descriptive")
	public void setDescriptive(String descriptive) {
		this.descriptive = descriptive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descriptive == null) ? 0 : descriptive.hashCode());
		result = prime * result + ((milliseconds == null) ? 0 : milliseconds.hashCode());
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
		Length other = (Length) obj;
		if (descriptive == null) {
			if (other.descriptive != null)
				return false;
		} else if (!descriptive.equals(other.descriptive))
			return false;
		if (milliseconds == null) {
			if (other.milliseconds != null)
				return false;
		} else if (!milliseconds.equals(other.milliseconds))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Length [milliseconds=");
		builder.append(milliseconds);
		builder.append(", descriptive=");
		builder.append(descriptive);
		builder.append("]");
		return builder.toString();
	}

}

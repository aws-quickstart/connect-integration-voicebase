package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.ALWAYS)
public class AudioRedaction {
	@JsonProperty("tone")
	private int tone;
	@JsonProperty("gain")
	private double gain;

	public int getTone() {
		return tone;
	}

	public void setTone(int tone) {
		this.tone = tone;
	}

	public AudioRedaction withTone(int tone) {
		setTone(tone);
		return this;
	}

	public double getGain() {
		return gain;
	}

	public void setGain(double gain) {
		this.gain = gain;
	}

	public AudioRedaction withGain(double gain) {
		setGain(gain);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(gain);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + tone;
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
		AudioRedaction other = (AudioRedaction) obj;
		if (Double.doubleToLongBits(gain) != Double.doubleToLongBits(other.gain))
			return false;
		if (tone != other.tone)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AudioRedaction [tone=");
		builder.append(tone);
		builder.append(", gain=");
		builder.append(gain);
		builder.append("]");
		return builder.toString();
	}
}

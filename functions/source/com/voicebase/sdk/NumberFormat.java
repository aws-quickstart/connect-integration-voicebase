package com.voicebase.sdk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NumberFormat {
	DIGITS,
	DASHED
	;
	
	/**
	 * Get NumberFormat from case insensitive string representation.
	 * 
	 * @param value
	 *            number format as string
	 * 
	 * @return null if input is null, matching NumberFormat otherwise.
	 * 
	 * @throws IllegalArgumentException
	 *             if string doesn't match any defined NumberFormat
	 */
	@JsonCreator
	public static NumberFormat fromValue(String value) throws IllegalArgumentException {
		if (value==null) {
			return null;
		}
		
		return NumberFormat.valueOf(value.toUpperCase());
	}
	
	@JsonValue
	public String value() {
		return name().toLowerCase();
	}
}

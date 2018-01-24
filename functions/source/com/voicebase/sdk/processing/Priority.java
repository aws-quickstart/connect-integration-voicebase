package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Priority {
	HIGH, NORMAL, LOW;


	/**
	 * Get Priority from case insensitive string representation.
	 * 
	 * @param value
	 *            priority as string
	 * 
	 * @return null if input is null, matching priority otherwise.
	 * 
	 * @throws IllegalArgumentException
	 *             if string doesn't match any defined priority
	 */
	@JsonCreator
	public static Priority fromValue(String value) throws IllegalArgumentException {
		if (value == null) {
			return null;
		}

		return Priority.valueOf(value.toUpperCase());
	}

}

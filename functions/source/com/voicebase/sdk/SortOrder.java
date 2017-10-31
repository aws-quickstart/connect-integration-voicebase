package com.voicebase.sdk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author volker@voicebase.com
 *
 */
public enum SortOrder {
	ASC, DESC;

	@JsonCreator
	public static SortOrder fromValue(String value) {
		if (value == null) {
			return null;
		}

		return SortOrder.valueOf(value.toUpperCase());
	}

	@JsonValue
	public String toString() {
		return name().toLowerCase();
	}
}

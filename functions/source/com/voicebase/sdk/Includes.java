package com.voicebase.sdk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Includes {
	METADATA,
	KEYWORDS,
	TOPICS,
	TRANSCRIPTS
	;
	
	@JsonCreator
	public static Includes fromValue(String value) {
		if (value == null) {
			return null;
		}

		return Includes.valueOf(value.toUpperCase());
	}

	@JsonValue
	public String toString() {
		return name().toLowerCase();
	}
}

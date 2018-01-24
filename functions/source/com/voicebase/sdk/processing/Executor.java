package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Executor {
	V2;
	
	@JsonCreator
	public static Executor fromValue(String value) {
		if (value==null) {
			return null;
		}
		
		return Executor.valueOf(value.toUpperCase());
	}
	
	@JsonValue
	public String value() {
		return name().toLowerCase();
	}
}

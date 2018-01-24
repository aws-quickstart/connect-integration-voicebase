package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DetectionModel {
	PCI,
	SSN,
	NUMBER
	;
	
	@JsonCreator
	public static DetectionModel fromValue(String value) {
		if (value==null) {
			return null;
		}
		
		return DetectionModel.valueOf(value.toUpperCase());
	}
}

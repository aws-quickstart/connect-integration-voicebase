package com.voicebase.sdk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RestAdapter.Log;

/**
 * Simple logger adapter to send Retrofit log entries to SLF4J.
 * 
 * Logs all Retrofit messages in DEBUG mode.
 * 
 * @author volker@voicebase.com
 *
 */
public class RetrofitToSlf4jLogger implements Log {

	private final Logger logger;

	public RetrofitToSlf4jLogger(Class<?> slf4jLogCategory) {
		this(slf4jLogCategory.getName());
	}

	public RetrofitToSlf4jLogger(String slf4jLogCategory) {
		logger = LoggerFactory.getLogger(slf4jLogCategory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see retrofit.RestAdapter.Log#log(java.lang.String)
	 */
	@Override
	public void log(String message) {
		logger.debug(message);
	}

}

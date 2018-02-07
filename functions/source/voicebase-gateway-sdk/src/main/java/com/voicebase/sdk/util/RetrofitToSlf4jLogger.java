/**
 * Copyright 2016-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not
 * use this file except in compliance with the License. A copy of the License is
 * located at
 *
 *      http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file
 * accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.voicebase.sdk.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import retrofit.RestAdapter.Log;

/**
 *
 * @author Volker Kueffel <volker@voicebase.com>
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

package com.voicebase.sdk.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Copyright 2017-2018 VoiceBase, Inc. or its affiliates. All Rights Reserved.
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
 * @author volker@voicebase.com
 *
 */
public class NoAuthHeaderHttpClientRedirectStrategy extends DefaultRedirectStrategy {

	private static final Logger LOGGER = LoggerFactory.getLogger(NoAuthHeaderHttpClientRedirectStrategy.class);

	private static final String AUTH_HEADER = "Authorization";

	private static class HeaderFilteringHttpGet extends HttpGet {
		public HeaderFilteringHttpGet(URI uri) {
			super(uri);
		}

		@Override
		public void setHeaders(Header[] headers) {
			super.setHeaders(filter(headers));
		}
	}

	private static class HeaderFilteringHttpHead extends HttpHead {

		public HeaderFilteringHttpHead(URI uri) {
			super(uri);
		}

		@Override
		public void setHeaders(Header[] headers) {
			super.setHeaders(filter(headers));
		}

	}

	private static final Header[] filter(Header[] in) {
		LOGGER.debug("Filtering headers...");
		Header[] newHeaders = null;
		if (in != null && in.length > 0) {
			List<Header> headers = new ArrayList<>();
			for (Header header : in) {
				if (!AUTH_HEADER.equalsIgnoreCase(header.getName())) {
					headers.add(header);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("{}: {}", header.getName(), header.getValue());
					}
				} else {
					LOGGER.debug("Removing '{}' header", AUTH_HEADER);
				}

			}

			if (headers.size() > 0) {
				newHeaders = headers.toArray(new Header[headers.size()]);
			}
		}
		return newHeaders;
	}

	@Override
	public HttpUriRequest getRedirect(HttpRequest request, HttpResponse response, HttpContext context)
			throws ProtocolException {

		LOGGER.debug("Redirecting...");
		URI uri = getLocationURI(request, response, context);
		String method = request.getRequestLine().getMethod();

		HttpRequestBase newRequest;
		if (method.equalsIgnoreCase(HttpHead.METHOD_NAME)) {
			newRequest = new HeaderFilteringHttpHead(uri);
		} else {
			newRequest = new HeaderFilteringHttpGet(uri);
		}

		return newRequest;

	}

}

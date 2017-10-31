package com.voicebase.sdk.processing;

import java.util.Set;
import java.util.TreeSet;

public class Callback {

	public static final String INCLUDE_METADATA = "metadata";
	public static final String INCLUDE_TOPICS = "topics";
	public static final String INCLUDE_TRANSCRIPTS = "transcripts";
	public static final String INCLUDE_KEYWORDS = "keywords";

	private String url;
	private String method;
	private final Set<String> include = new TreeSet<>();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Set<String> getInclude() {
		return include;
	}

	public void setInclude(Set<String> include) {
		this.include.clear();
		if (include != null) {
			this.include.addAll(include);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((include == null) ? 0 : include.hashCode());
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		Callback other = (Callback) obj;
		if (include == null) {
			if (other.include != null)
				return false;
		} else if (!include.equals(other.include))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Callback [url=");
		builder.append(url);
		builder.append(", method=");
		builder.append(method);
		builder.append(", include=");
		builder.append(include);
		builder.append("]");
		return builder.toString();
	}

}

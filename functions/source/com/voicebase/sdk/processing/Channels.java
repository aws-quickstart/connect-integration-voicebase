package com.voicebase.sdk.processing;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author volker@voicebase.com
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class Channels {

	@JsonProperty("left")
	private Speaker left;
	@JsonProperty("right")
	private Speaker right;

	public Speaker getLeft() {
		return left;
	}

	public void setLeft(Speaker left) {
		this.left = left;
	}

	public Channels withLeft(Speaker left) {
		setLeft(left);
		return this;
	}

	public Speaker getRight() {
		return right;
	}

	public void setRight(Speaker right) {
		this.right = right;
	}

	public Channels withRight(Speaker right) {
		setRight(right);
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		Channels other = (Channels) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Channels [left=");
		builder.append(left);
		builder.append(", right=");
		builder.append(right);
		builder.append("]");
		return builder.toString();
	}

}

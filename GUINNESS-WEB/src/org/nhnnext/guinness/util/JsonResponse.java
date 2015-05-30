package org.nhnnext.guinness.util;

public class JsonResponse {
	private boolean success;
	private String message;
	private String location;
	private Object json;

	public JsonResponse() {
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public String getLocation() {
		return location;
	}

	public Object getJson() {
		return json;
	}

	public JsonResponse setSuccess(boolean success) {
		this.success = success;
		return this;
	}

	public JsonResponse setMessage(String message) {
		this.message = message;
		return this;
	}

	public JsonResponse setLocation(String location) {
		this.location = location;
		return this;
	}

	public JsonResponse setJson(Object json) {
		this.json = json;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((json == null) ? 0 : json.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + (success ? 1231 : 1237);
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
		JsonResponse other = (JsonResponse) obj;
		if (json == null) {
			if (other.json != null)
				return false;
		} else if (!json.equals(other.json))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (success != other.success)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JsonResponse [success=" + success + ", message=" + message
				+ ", location=" + location + ", json=" + json + "]";
	}
	
}

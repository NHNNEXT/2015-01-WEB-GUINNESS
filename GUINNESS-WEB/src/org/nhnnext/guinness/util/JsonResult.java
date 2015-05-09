package org.nhnnext.guinness.util;

import java.util.List;

public class JsonResult<T> {
	private boolean success;
	private String message;
	private String locationWhenFail;
	private T object;
	private List<T> listValues;
	
	public JsonResult() {
	}
	
	public JsonResult<T> setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	
	public JsonResult<T> setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public JsonResult<T> setLocationWhenFail(String locationWhenFail) {
		this.locationWhenFail = locationWhenFail;
		return this;
	}
	
	public JsonResult<T> setListValues(List<T> listValues) {
		this.listValues = listValues;
		return this;
	}
	
	public JsonResult<T> setObject(T object) {
		this.object = object;
		return this;
	}
	
	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public String getLocationWhenFail() {
		return locationWhenFail;
	}

	public T getObject() {
		return object;
	}

	public List<T> getListValues() {
		return listValues;
	}

	public void addValue(T value) {
		listValues.add(value);
	}

	@Override
	public String toString() {
		return "ResponseResult [success=" + success + ", message=" + message
				+ ", locationWhenFail=" + locationWhenFail + ", object="
				+ object + ", listValues=" + listValues + "]";
	}
}

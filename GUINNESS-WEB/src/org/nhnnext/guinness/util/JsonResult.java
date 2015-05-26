package org.nhnnext.guinness.util;

import java.util.List;
import java.util.Map;

public class JsonResult<T> {
	private boolean success;
	private String message;
	private String locationWhenFail;
	private Object object;
	private List<T> objectValues;
	private List<Map<String, Object>> mapValues;
	private Map<String, List<Map<String, Object>>> listValues;

	public JsonResult() {
	}
	
	public JsonResult setSuccess(boolean success) {
		this.success = success;
		return this;
	}
	
	public JsonResult setMessage(String message) {
		this.message = message;
		return this;
	}
	
	public JsonResult setLocationWhenFail(String locationWhenFail) {
		this.locationWhenFail = locationWhenFail;
		return this;
	}
	
	public JsonResult setObject(Object object) {
		this.object = object;
		return this;
	}

	public JsonResult setObjectValues(List<T> objectValues) {
		this.objectValues = objectValues;
		return this;
	}
	
	public JsonResult setMapValues(List<Map<String, Object>> list) {
		this.mapValues = list;
		return this;
	}
	
	public JsonResult setListValues(Map<String, List<Map<String, Object>>> listValues) {
		this.listValues = listValues;
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

	public Object getObject() {
		return object;
	}
	
	public List<T> getObjectValues() {
		return objectValues;
	}
	
	public List<Map<String, Object>> getMapValues() {
		return mapValues;
	}
	
	public Map<String, List<Map<String, Object>>> getListValues() {
		return listValues;
	}

	@Override
	public String toString() {
		return "JsonResult [success=" + success + ", message=" + message
				+ ", locationWhenFail=" + locationWhenFail + ", object="
				+ object + ", objectValues=" + objectValues + ", mapValues="
				+ mapValues + "]";
	}
}

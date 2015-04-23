package org.nhnnext.guinness.controller;

import java.util.HashMap;
import java.util.Map;

public class JsonResult {
	private boolean success;
	private String errorMessage;
	private Map<String, Object> values = new HashMap<String, Object>();
	
	public JsonResult() {
		success = true;
	}
	
	public JsonResult(boolean success, Map<String, Object> values) {
		this(success, null, values);
	}
	
	public JsonResult(boolean success, String errorMessage, Map<String, Object> values) {
		this.success = success;
		this.errorMessage = errorMessage;
		this.values = values;
	}
	
	public void putValue(String key, Object value) {
		values.put(key, value);
	}
	
}

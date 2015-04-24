package org.nhnnext.guinness.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResult<T> {
	private boolean success;
	private String errorMessage;
	private Map<String, Object> mapValues = new HashMap<String, Object>();
	private List<T> listValues;

	public JsonResult() {
		success = true;
	}

	public JsonResult(boolean success, Map<String, Object> values) {
		this(success, null, values);
	}

	public JsonResult(boolean success, List<T> listValues) {
		this(success, null, listValues);
	}

	public JsonResult(boolean success, String errorMessage) {
		this(success, errorMessage, null, null);
	}

	public JsonResult(boolean success, String errorMessage, Map<String, Object> mapValues) {
		this(success, errorMessage, mapValues, null);
	}

	public JsonResult(boolean success, String errorMessage, List<T> listValues) {
		this(success, errorMessage, null, listValues);
	}

	public JsonResult(boolean success, String errorMessage, Map<String, Object> mapValues, List<T> listValues) {
		this.success = success;
		this.errorMessage = errorMessage;
		this.mapValues = mapValues;
		this.listValues = listValues;
	}

	public void putValue(String key, Object value) {
		mapValues.put(key, value);
	}

	public void addValue(T value) {
		listValues.add(value);
	}

	public boolean isSuccess() {
		return success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public Map<String, Object> getValues() {
		return mapValues;
	}

	public List<T> getListValues() {
		return listValues;
	}

}

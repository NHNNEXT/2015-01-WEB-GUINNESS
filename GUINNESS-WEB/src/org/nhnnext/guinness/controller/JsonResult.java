package org.nhnnext.guinness.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResult<T> {
	private boolean success;
	private String locationWhenFail;
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

	public JsonResult(boolean success, String locationWhenFail) {
		this(success, locationWhenFail, null, null);
	}

	public JsonResult(boolean success, String locationWhenFail, Map<String, Object> mapValues) {
		this(success, locationWhenFail, mapValues, null);
	}

	public JsonResult(boolean success, String locationWhenFail, List<T> listValues) {
		this(success, locationWhenFail, null, listValues);
	}

	public JsonResult(boolean success, String locationWhenFail, Map<String, Object> mapValues, List<T> listValues) {
		this.success = success;
		this.locationWhenFail = locationWhenFail;
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

	public String getLocationWhenFail() {
		return locationWhenFail;
	}

	public Map<String, Object> getValues() {
		return mapValues;
	}

	public List<T> getListValues() {
		return listValues;
	}

}

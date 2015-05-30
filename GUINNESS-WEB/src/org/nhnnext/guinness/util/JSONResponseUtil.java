package org.nhnnext.guinness.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JSONResponseUtil {
	/**
     * JSON View 화면 처리를 위해 JSON변환 후 ResponseEntity로 반환.
     * @param obj
     * @return
     */
    public static ResponseEntity<Object> getJSONResponse(Object obj, HttpStatus status){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
        return new ResponseEntity<Object>(obj, responseHeaders, status);
    }
}

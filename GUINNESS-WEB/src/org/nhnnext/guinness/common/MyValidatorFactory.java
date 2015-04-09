package org.nhnnext.guinness.common;

import javax.validation.Validation;
import javax.validation.Validator;

//TODO http://www.slipp.net/questions/360 글 참고해 수정
public class MyValidatorFactory {
	public static Validator createValidator() {
		return Validation.buildDefaultValidatorFactory().getValidator();
	}
}

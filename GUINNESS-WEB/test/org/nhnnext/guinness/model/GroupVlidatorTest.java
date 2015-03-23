package org.nhnnext.guinness.model;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class GroupVlidatorTest {
	private static Validator validator;

	@BeforeClass
	public static void setUp() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void groupIdIsNull() {
		Group group = new Group(null,"a","CaptainUserId",1);
		Set<ConstraintViolation<Group>> constraintViolations = validator.validate(group);
		assertEquals(2, constraintViolations.size());
		
		Iterator<ConstraintViolation<Group>> violations = constraintViolations.iterator();
		while(violations.hasNext()){
			ConstraintViolation<Group> each = violations.next();
			System.out.println(each.getMessage());
		}
		
		
	}

}

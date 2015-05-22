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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupVlidatorTest {
	private static final Logger logger = LoggerFactory.getLogger(GroupVlidatorTest.class);
	private static Validator validator;

	@BeforeClass
	public static void setUp() throws Exception {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void groupIdIsNull() {
		Group group = new Group(null,"a","CaptainUserId","F", null);
		Set<ConstraintViolation<Group>> constraintViolations = validator.validate(group);
		assertEquals(2, constraintViolations.size());
		
		Iterator<ConstraintViolation<Group>> violations = constraintViolations.iterator();
		while(violations.hasNext()){
			ConstraintViolation<Group> each = violations.next();
			logger.debug(each.getMessage());
		}
		
		
	}

}

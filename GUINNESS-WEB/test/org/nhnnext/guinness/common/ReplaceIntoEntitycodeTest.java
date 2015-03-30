package org.nhnnext.guinness.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReplaceIntoEntitycodeTest {

	@Test
	public void testEntitycode() {
		String test = "<button>test</button>";
		test = ReplaceIntoEntitycode.intoEntitycode(test);
		assertEquals("&lt;button&gt;test&lt;/button&gt;",test);
	}

}

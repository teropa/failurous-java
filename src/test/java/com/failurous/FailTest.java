package com.failurous;

import static org.junit.Assert.*;

import org.junit.Test;

public class FailTest {

	@Test
	public void sectionShouldBeIncludedAfterItHasBeenAddedByName() {
		Fail fail = new Fail("test");
		FailSection section = fail.addSection("test section");
		
		assertEquals(1, fail.getData().size());
		assertSame(section, fail.getData().get(0));
	}
}

package com.failurous;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

import org.junit.Test;

public class FailSectionTest {

	@Test
	public void shouldBeAnArrayListWithFieldsCorrespondingToTheAPI() {
		FailSection section = new FailSection("test section");
		section.addField("test field", "test value");
		
		assertEquals(2, section.size());
		assertEquals("test section", section.get(0));
		assertThat(section.get(1), is(List.class));
		assertEquals(1, ((List<?>)section.get(1)).size());
	}
}

package com.failurous.fail;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Map;

import org.junit.Test;

import com.failurous.fail.FailField;


public class FailFieldTest {

	@Test
	public void shouldConstructWithOptions() {
		FailField field = new FailField("reason", "too much fail",
				"use_in_checksum", "true",
				"something_else", "4");
		assertEquals(2, field.getOptions().size());
		assertEquals("true", field.getOptions().get("use_in_checksum"));
		assertEquals("4", field.getOptions().get("something_else"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldFailWhenGivenANonEvenNumberOfOptions() {
		new FailField("reason", "too much fail", "i have no value");
	}
	
	@Test
	public void shouldBeAListWithThreeItemsAfterConstruction() {
		FailField field = new FailField("reason", "too much fail");
		assertEquals(3, field.size());
		assertEquals("reason", field.get(0));
		assertEquals("too much fail", field.get(1));
		assertThat(field.get(2), is(Map.class));
	}
	
}

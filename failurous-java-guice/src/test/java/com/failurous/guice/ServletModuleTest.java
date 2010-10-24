package com.failurous.guice;

import org.junit.Test;

import com.failurous.FailurousServletFilter;
import com.google.inject.Guice;
import com.google.inject.servlet.ServletModule;

public class ServletModuleTest {

	@Test
	public void shouldBeAbleToBindClientFilter() {
		Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				install(new FailurousModule());
				filter("/*").through(FailurousServletFilter.class);
			}
		});
	}
	
}

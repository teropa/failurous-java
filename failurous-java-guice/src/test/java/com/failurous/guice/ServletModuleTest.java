package com.failurous.guice;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.failurous.FailurousServletFilter;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.ServletModule;

public class ServletModuleTest {

	@Test
	public void shouldInjectInstanceOfGuiceFilterImpl() {
		Injector injector = getInjector();
		FailurousServletFilter filter = injector.getInstance(FailurousServletFilter.class);
		assertThat(filter, is(FailurousServletFilterGuiceImpl.class));
	}
	
	private Injector getInjector() {
		return Guice.createInjector(new ServletModule() {
			@Override
			protected void configureServlets() {
				install(new FailurousModule());
				filter("/*").through(FailurousServletFilter.class);
			}
		});
	}
	
}

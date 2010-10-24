package com.failurous.guice.aop;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.failurous.FailSender;
import com.failurous.aop.ReportExceptions;
import com.failurous.fail.Fail;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;

public class FailReportInterceptorTest {

	private FailSender mockSender;
	
	@Inject
	private HopelessService service;
	
	@Before
	public void setUp() {
		mockSender = mock(FailSender.class);
		Guice.createInjector(new AbstractModule() {
			@Override
			protected void configure() {
				bind(FailSender.class).toInstance(mockSender);
				install(new FailurousAOPModule());
				bind(HopelessService.class);
			}
		}).injectMembers(this);
	}
	
	@Test
	public void shouldInterceptAnnotatedMethods() {
		try {
			service.failingMethod();
		} catch (IllegalStateException e) {
		}
		verify(mockSender).send(isA(Fail.class));
	}
	
	public static class HopelessService {
	
		@ReportExceptions
		public void failingMethod() {
			throw new IllegalStateException("oh noes");
		}
		
	}
	
}

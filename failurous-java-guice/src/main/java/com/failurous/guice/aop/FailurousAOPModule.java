package com.failurous.guice.aop;

import com.failurous.aop.ReportExceptions;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class FailurousAOPModule extends AbstractModule {
	
	@Override
	protected void configure() {
		ExceptionReportInterceptor reportInterceptor = new ExceptionReportInterceptor();
		requestInjection(reportInterceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(ReportExceptions.class), reportInterceptor);
	}

}

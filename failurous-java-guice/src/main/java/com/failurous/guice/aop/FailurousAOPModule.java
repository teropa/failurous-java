package com.failurous.guice.aop;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class FailurousAOPModule extends AbstractModule {
	
	@Override
	protected void configure() {
		FailReportInterceptor reportInterceptor = new FailReportInterceptor();
		requestInjection(reportInterceptor);
		bindInterceptor(Matchers.any(), Matchers.annotatedWith(ReportFails.class), reportInterceptor);
	}

}

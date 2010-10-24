package com.failurous.guice;

import com.failurous.FailurousServletFilter;
import com.failurous.FailSender;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class FailurousModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(FailSender.class).toProvider(FailSenderProvider.class).in(Scopes.SINGLETON);
		bind(FailurousServletFilter.class).in(Scopes.SINGLETON);
	}

}

package com.failurous.guice;

import com.failurous.FailSender;
import com.failurous.FailurousServletFilter;
import com.failurous.guice.aop.FailurousAOPModule;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class FailurousModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(FailSender.class).toProvider(FailSenderProvider.class).in(Scopes.SINGLETON);
		bind(FailurousServletFilter.class).to(FailurousServletFilterGuiceImpl.class).in(Scopes.SINGLETON);
		install(new FailurousAOPModule());
	}

}

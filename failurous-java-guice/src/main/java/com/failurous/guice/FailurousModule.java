package com.failurous.guice;

import com.failurous.FailSender;
import com.failurous.FailSenderFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scopes;

public class FailurousModule extends AbstractModule {
	
	@Override
	protected void configure() {
		bind(FailSender.class).toProvider(new Provider<FailSender>() {
			public FailSender get() {
				return FailSenderFactory.getSender();
			}
		}).in(Scopes.SINGLETON);
	}

}

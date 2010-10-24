package com.failurous.guice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.failurous.FailSender;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class FailurousModuleTest {

	@Inject
	private FailSender sender;
	
	@Test
	public void shouldInjectSenderWithAnnotatedConfig() {
		Injector injector = Guice.createInjector(new FailurousModule(), new AnnotatedConfigModule());
		injector.injectMembers(this);
		assertNotNull(sender);
		assertEquals("http://localhost:3001/", sender.getServerAddress());
		assertEquals("annotated api key", sender.getAPIKey());
	}
	
	@Test
	public void shouldInjectSenderWithFileBasedConfig() {
		Injector injector = Guice.createInjector(new FailurousModule());
		injector.injectMembers(this);
		assertNotNull(sender);
		assertEquals("http://localhost:3000/", sender.getServerAddress());
		assertEquals("configured api key", sender.getAPIKey());
	}
	
	@Test
	public void shouldBeAbleToMixAnnotatedAndFileBasedConfig() {
		Injector injector = Guice.createInjector(new FailurousModule(), new AnnotatedAPIKeyConfigModule());
		injector.injectMembers(this);
		assertNotNull(sender);
		assertEquals("http://localhost:3000/", sender.getServerAddress());
		assertEquals("annotated api key", sender.getAPIKey());
	}
	
	private class AnnotatedConfigModule extends AbstractModule {
		@Override
		protected void configure() {
			bindConstant().annotatedWith(FailurousServerAddress.class).to("http://localhost:3001/");
			bindConstant().annotatedWith(FailurousAPIKey.class).to("annotated api key");
		}
	}

	private class AnnotatedAPIKeyConfigModule extends AbstractModule {
		@Override
		protected void configure() {
			bindConstant().annotatedWith(FailurousAPIKey.class).to("annotated api key");
		}
	}

	
}

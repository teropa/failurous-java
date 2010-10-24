package com.failurous.guice;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.failurous.FailSender;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class FailurousModuleTest {

	@Inject
	private FailSender sender;
	
	@Before
	public void setUp() {
		Injector injector = Guice.createInjector(new FailurousModule());
		injector.injectMembers(this);
	}
	
	@Test
	public void shouldInjectSender() {
		assertNotNull(sender);
	}
}

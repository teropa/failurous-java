package com.failurous.guice;

import com.failurous.FailSender;
import com.failurous.FailurousServletFilter;
import com.google.inject.Inject;

/**
 * An implementation of {@link FailurousServletFilter} which uses
 * a {@link FailSender} bound with Guice.
 * 
 * @author teroparv
 *
 */
public class FailurousServletFilterGuiceImpl extends FailurousServletFilter {

	private final FailSender sender;
	
	@Inject
	public FailurousServletFilterGuiceImpl(FailSender sender) {
		this.sender = sender;
	}
	
	@Override
	protected FailSender getSender() {
		return this.sender;
	}

}

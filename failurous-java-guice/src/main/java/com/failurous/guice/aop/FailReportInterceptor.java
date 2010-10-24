package com.failurous.guice.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.failurous.FailSender;
import com.failurous.fail.ExceptionFail;
import com.google.inject.Inject;

public class FailReportInterceptor implements MethodInterceptor {
	
	private FailSender sender;
	
	@Inject
	public void init(FailSender sender) {
		this.sender = sender;
	}
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {
			return invocation.proceed();
		} catch (Throwable t) {
			sender.send(new ExceptionFail(t));
			throw t;
		}
	}

}


package com.failurous.spring.aop;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.failurous.FailSender;
import com.failurous.fail.ExceptionFail;

@Aspect
@Component
public class ExceptionReportInterceptor {

	private FailSender failSender;
	
	@Autowired
	public void setFailSender(FailSender sender) {
		this.failSender = sender;
	}
	
	@AfterThrowing(pointcut="@annotation(com.failurous.aop.ReportExceptions)", throwing="t")
	public void reportException(Throwable t) {
		failSender.send(new ExceptionFail(t));
	}
	
}

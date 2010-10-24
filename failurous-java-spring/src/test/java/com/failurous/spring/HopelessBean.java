package com.failurous.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.failurous.FailSender;
import com.failurous.aop.ReportExceptions;

@Service
public class HopelessBean {
	
	private FailSender failSender;
	
	@Autowired
	public void init(FailSender sender) {
		this.failSender = sender;
	}
	
	public boolean hasFailSender() {
		return failSender != null;
	}
	
	@ReportExceptions
	public String testMethod() {
		throw new IllegalArgumentException("epic fail");
	}
	
}

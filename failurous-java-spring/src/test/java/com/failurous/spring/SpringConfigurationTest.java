package com.failurous.spring;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnit44Runner;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.failurous.FailSender;
import com.failurous.fail.Fail;
import com.failurous.spring.aop.ExceptionReportInterceptor;

@RunWith(MockitoJUnitRunner.class)
public class SpringConfigurationTest {

	@Mock
	private FailSender mockSender;
	
	@Test
	public void shouldMakeFailSenderAvailableThroughClasspathScan() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/applicationContext-scan-autoproxy.xml");
		HopelessBean testBean = (HopelessBean)ctx.getBean(HopelessBean.class);
		assertNotNull(testBean);
		assertTrue(testBean.hasFailSender());
	}
	
	@Test
	public void shouldMakeFailSenderAvailableThroughExplicitConfiguration() {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/applicationContext-explicit-autoproxy.xml");
		HopelessBean testBean = (HopelessBean)ctx.getBean(HopelessBean.class);
		assertNotNull(testBean);
		assertTrue(testBean.hasFailSender());		
	}
	
	@Test
	public void shouldAddExceptionReportInterceptorThroughAutoProxyAndClasspathScan() {
		testFailSent("classpath:/applicationContext-scan-autoproxy.xml");
	}
	
	@Test
	public void shouldAddExceptionReportInterceptorThroughAutoProxyAndExplicitConfiguration() {
		testFailSent("classpath:/applicationContext-explicit-autoproxy.xml");
	}
	
	@Test
	public void shouldAddExceptionReportInterceptorThroughDeclaredAdviceAndClasspathScan() {
		testFailSent("classpath:/applicationContext-scan-advice.xml");
	}
	
	@Test
	public void shouldAddExceptionReportInterceptorThroughDeclaredAdviceAndExplicitConfiguration() {
		testFailSent("classpath:/applicationContext-explicit-advice.xml");
	}
	
	private void testFailSent(String contextXml) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext(contextXml);
		HopelessBean testBean = (HopelessBean)ctx.getBean(HopelessBean.class);
		ExceptionReportInterceptor interceptor = (ExceptionReportInterceptor)ctx.getBean(ExceptionReportInterceptor.class);
		interceptor.setFailSender(mockSender);
		
		try {
			testBean.testMethod();
		} catch (IllegalArgumentException ie) {
		}
		
		verify(mockSender).send(isA(Fail.class));
	}

}

package com.failurous;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.failurous.fail.ExceptionDuringWebRequestFail;

public class FailurousServletFilter implements Filter {
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		try {
			chain.doFilter(req, res);
		} catch (IOException e) {
			sendReport(e, req);
			throw e;
		} catch (ServletException e) {
			sendReport(e, req);
			throw e;			
		} catch (RuntimeException e) {
			sendReport(e, req);
			throw e;
		}
	}
	
	private void sendReport(Throwable t, ServletRequest request) {
		getSender().send(new ExceptionDuringWebRequestFail(t, (HttpServletRequest)request));
	}

	protected FailSender getSender() {
		return FailSenderFactory.getSender();
	}

	public void init(FilterConfig cfg) throws ServletException {
	}

	public void destroy() {		
	}
	
}

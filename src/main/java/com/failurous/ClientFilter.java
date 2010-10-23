package com.failurous;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ClientFilter implements Filter {
	
	private String serverAddress;
	private String apiKey;
	
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
		FailSender sender = FailSenderFactory.getSender(serverAddress, apiKey);
		sender.sendReport(constructReport(t, (HttpServletRequest)request));
	}

	private FailNotification constructReport(Throwable t, HttpServletRequest request) {
		FailNotification report = new FailNotification(t.getMessage(), t.getStackTrace()[0].toString());
		report.addSection(getSummary(t, request));
		report.addSection(getDetails(t));
		report.addSection(getRequestInfo(request));
		report.addSection(getSessionInfo(request));
		return report;
	}

	private FailNotificationSection getSummary(Throwable t, HttpServletRequest request) {
		FailNotificationSection summary = new FailNotificationSection("summary");
		summary.addField(constructField("type", t.getClass().getCanonicalName(), "use_in_checksum", "true"));
		summary.addField(constructField("message", t.getMessage()));
		summary.addField(constructField("request_url", request.getRequestURL().toString()));
		return summary;
	}

	private FailNotificationSection getDetails(Throwable t) {
		FailNotificationSection details = new FailNotificationSection("details");
		details.addField(constructField("stacktrace", getStackTraceString(t)));
		return details;
	}

	private FailNotificationSection getRequestInfo(HttpServletRequest request) {
		FailNotificationSection requestInfo = new FailNotificationSection("request");
		requestInfo.addField(constructField("url", request.getRequestURL().toString()));
		requestInfo.addField(constructField("remote_address", request.getRemoteAddr()));
		for (String header : stringList(request.getHeaderNames())) {
			requestInfo.addField(constructField(header, request.getHeader(header)));	
		}
		return requestInfo;
	}
	
	private FailNotificationSection getSessionInfo(HttpServletRequest request) {
		FailNotificationSection session = new FailNotificationSection("session");
		if (request.getSession(false) != null) {
			for (String key : stringList(request.getSession().getAttributeNames())) {
				Object value = request.getSession().getAttribute(key);
				session.addField(constructField(key, value.toString()));
			}
		}
		return session;
	}	

	private FailNotificationField constructField(String name, String value, String... options) {
		FailNotificationField field = new FailNotificationField(name, value);
		for (int i=0 ; i<options.length ; i += 2) {
			field.setOption(options[i], options[i+1]);
		}		
		return field;
	}
	
	private String getStackTraceString(Throwable t) {
		StringWriter res = new StringWriter();
		t.printStackTrace(new PrintWriter(res));
		res.flush();
		return res.toString();
	}
	
	@SuppressWarnings({"unchecked","rawtypes"})
	private List<String> stringList(Enumeration e) {
		return Collections.list(e);
	}

	public void init(FilterConfig cfg) throws ServletException {
		this.serverAddress = cfg.getInitParameter("serverAddress");
		this.apiKey = cfg.getInitParameter("apiKey");
	}

	public void destroy() {		
	}
	
}

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
	
	private ReportSender sender;
	
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
		sender.sendReport(constructReport(t, (HttpServletRequest)request));
	}

	private Report constructReport(Throwable t, HttpServletRequest request) {
		Report report = new Report();
		report.title = t.getMessage();
		report.location = t.getStackTrace()[0].toString();
		report.data.add(getSummary(t, request));
		report.data.add(getDetails(t));
		report.data.add(getRequestInfo(request));
		report.data.add(getSessionInfo(request));
		return report;
	}

	private ReportSection getSummary(Throwable t, HttpServletRequest request) {
		ReportSection summary = new ReportSection("summary");
		summary.addField(constructField("type", t.getClass().getCanonicalName(), "use_in_checksum", "true"));
		summary.addField(constructField("message", t.getMessage()));
		summary.addField(constructField("request_url", request.getRequestURL().toString()));
		return summary;
	}

	private ReportSection getDetails(Throwable t) {
		ReportSection details = new ReportSection("details");
		details.addField(constructField("stacktrace", getStackTraceString(t)));
		return details;
	}

	private ReportSection getRequestInfo(HttpServletRequest request) {
		ReportSection requestInfo = new ReportSection("request");
		requestInfo.addField(constructField("url", request.getRequestURL().toString()));
		requestInfo.addField(constructField("remote_address", request.getRemoteAddr()));
		List<String> headers = stringList(request.getHeaderNames());
		for (String header : headers) {
			requestInfo.addField(constructField(header, request.getHeader(header)));	
		}
		return requestInfo;
	}
	
	private ReportSection getSessionInfo(HttpServletRequest request) {
		ReportSection session = new ReportSection("session");
		if (request.getSession(false) != null) {
			List<String> keys = stringList(request.getSession().getAttributeNames());
			for (String key : keys) {
				Object value = request.getSession().getAttribute(key);
				session.addField(constructField(key, value.toString()));
			}
		}
		return session;
	}	

	private ReportField constructField(String name, String value, String... options) {
		ReportField field = new ReportField(name, value);
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
		String serverAddress = cfg.getInitParameter("serverAddress");
		String apiKey = cfg.getInitParameter("apiKey");
		
		if (!serverAddress.startsWith("http")) {
			serverAddress = "http://" + serverAddress;
		}
		if (!serverAddress.endsWith("/")) {
			serverAddress += "/";
		}
		
		this.sender = new ReportSender(serverAddress + "api/projects/" + apiKey + "/fails");
	}

	public void destroy() {		
	}
	
}

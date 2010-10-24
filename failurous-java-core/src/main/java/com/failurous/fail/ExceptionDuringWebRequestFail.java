package com.failurous.fail;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class ExceptionDuringWebRequestFail extends ExceptionFail {

	public ExceptionDuringWebRequestFail(Throwable cause, HttpServletRequest request) {
		super(cause);
		addSection(getRequestInfo(request));
		addSection(getSessionInfo(request));
	}

	private FailSection getRequestInfo(HttpServletRequest request) {
		FailSection requestInfo = new FailSection("request");
		requestInfo.addField("url", request.getRequestURL().toString());
		requestInfo.addField("remote_address", request.getRemoteAddr());
		for (String header : stringList(request.getHeaderNames())) {
			requestInfo.addField(header, request.getHeader(header));	
		}
		return requestInfo;
	}
	
	private FailSection getSessionInfo(HttpServletRequest request) {
		FailSection session = new FailSection("session");
		if (request.getSession(false) != null) {
			for (String key : stringList(request.getSession().getAttributeNames())) {
				Object value = request.getSession().getAttribute(key);
				session.addField(key, value.toString());
			}
		}
		return session;
	}	
		
	@SuppressWarnings({"unchecked","rawtypes"})
	private List<String> stringList(Enumeration e) {
		return Collections.list(e);
	}

}

package com.failurous.fail;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class WebRequestFail extends Fail {

	public WebRequestFail(String message, HttpServletRequest request) {
		super(message);
		addSection(getRequestInfo(request));
		addSection(getSessionInfo(request));
	}

	public static FailSection getRequestInfo(HttpServletRequest request) {
		FailSection requestInfo = new FailSection("request");
		requestInfo.addField("url", request.getRequestURL().toString());
		requestInfo.addField("remote_address", request.getRemoteAddr());
		for (String header : stringList(request.getHeaderNames())) {
			requestInfo.addField(header, request.getHeader(header));	
		}
		return requestInfo;
	}
	
	public static FailSection getSessionInfo(HttpServletRequest request) {
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
	private static List<String> stringList(Enumeration e) {
		return Collections.list(e);
	}
}

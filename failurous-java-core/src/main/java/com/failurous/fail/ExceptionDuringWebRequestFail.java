package com.failurous.fail;

import javax.servlet.http.HttpServletRequest;

public class ExceptionDuringWebRequestFail extends ExceptionFail {

	public ExceptionDuringWebRequestFail(Throwable cause, HttpServletRequest request) {
		super(cause);
		addSection(WebRequestFail.getRequestInfo(request));
		addSection(WebRequestFail.getSessionInfo(request));
	}

}

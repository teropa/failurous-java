package com.failurous.fail;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionFail extends Fail {

	public ExceptionFail(Throwable cause) {
		super(cause.getMessage(), cause.getStackTrace()[0].toString());
		addSection(getSummary(cause));
		addSection(getDetails(cause));
	}

	protected FailSection getSummary(Throwable cause) {
		FailSection summary = new FailSection("summary");
		summary.addField("type", cause.getClass().getCanonicalName(), "use_in_checksum", "true");
		summary.addField("message", cause.getMessage());
		return summary;
	}

	protected FailSection getDetails(Throwable cause) {
		FailSection details = new FailSection("details");
		details.addField("stacktrace", getStackTraceString(cause));
		return details;
	}
	
	protected String getStackTraceString(Throwable cause) {
		StringWriter res = new StringWriter();
		cause.printStackTrace(new PrintWriter(res));
		res.flush();
		return res.toString();
	}
	
}

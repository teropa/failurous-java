package com.failurous.gwt.server;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ExceptionReportingRemoteServiceServlet extends RemoteServiceServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	public String processCall(String payload) throws SerializationException {
		try {
			return super.processCall(payload);
		} catch (SerializationException se) {
			throw se;
		} catch (RuntimeException re) {
			throw re;
		}
	}

}

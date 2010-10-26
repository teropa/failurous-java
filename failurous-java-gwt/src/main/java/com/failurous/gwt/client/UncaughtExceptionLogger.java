package com.failurous.gwt.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;

public class UncaughtExceptionLogger {

	private static Logger LOGGER = Logger.getLogger("UncaughtExceptionLogger");
	
	public static void install() {
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			public void onUncaughtException(Throwable t) {
				LOGGER.log(Level.SEVERE, t.getMessage(), t);
			}
		});
	}
	
}

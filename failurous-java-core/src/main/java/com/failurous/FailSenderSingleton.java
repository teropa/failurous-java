package com.failurous;

import java.util.Properties;

import com.failurous.config.FailurousConfig;

public class FailSenderSingleton extends FailSender {

	private volatile static FailSenderSingleton instance;

	public static FailSender get() {
		if (instance == null) {
			synchronized(FailSenderSingleton.class) {
				if (instance == null) {
					Properties config = FailurousConfig.load();
					instance = new FailSenderSingleton();
					instance.init(
							config.getProperty("failurous.server.address"),
							config.getProperty("failurous.api.key"));
				}
			}
		}
		return instance;
	}

}

package com.failurous;

import com.failurous.config.FailurousConfig;

public class FailSenderSingleton extends FailSender {

	private volatile static FailSenderSingleton instance;

	public static FailSender get() {
		if (instance == null) {
			synchronized(FailSenderSingleton.class) {
				if (instance == null) {
					FailurousConfig config = FailurousConfig.load();
					instance = new FailSenderSingleton();
					instance.init(config.getServerAddress(), config.getAPIKey());
				}
			}
		}
		return instance;
	}

}

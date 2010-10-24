package com.failurous;

import java.util.Properties;

import com.failurous.config.FailurousConfig;

public class FailSenderFactory {

	private volatile static FailSender sender;

	public static FailSender getSender() {
		if (sender == null) {
			synchronized(FailSender.class) {
				if (sender == null) {
					Properties config = FailurousConfig.load();
					sender = new FailSender(config.getProperty("serverAddress"), config.getProperty("apiKey"));
				}
			}
		}
		return sender;
	}

}

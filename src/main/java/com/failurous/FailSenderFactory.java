package com.failurous;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FailSenderFactory {

	private volatile static FailSender sender;

	public static FailSender getSender() {
		if (sender == null) {
			synchronized(FailSender.class) {
				if (sender == null) {
					Properties config = loadConfig();
					sender = new FailSender(config.getProperty("serverAddress"), config.getProperty("apiKey"));
				}
			}
		}
		return sender;
	}

	private static Properties loadConfig() {
		try {
			Properties config = new Properties();
			config.load(getConfigIn());
			return config;
		} catch (IOException ioe) {
			throw new IllegalArgumentException("Could not load failurous.properties", ioe);
		}
	}

	private static InputStream getConfigIn() {
		InputStream configIn = FailSender.class.getResourceAsStream("/failurous.properties");
		if (configIn == null) {
			throw new IllegalArgumentException("failurous.properties not found in classpath");
		}
		return configIn;
	}
}

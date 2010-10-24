package com.failurous.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.failurous.FailSender;

public class FailurousConfig {

	public static Properties load() {
		try {
			Properties config = new Properties();
			config.load(getConfigIn());
			return config;
		} catch (IOException ioe) {
			throw new FailurousConfigException("Could not load failurous.properties", ioe);
		}
	}

	private static InputStream getConfigIn() {
		InputStream configIn = FailSender.class.getResourceAsStream("/failurous.properties");
		if (configIn == null) {
			throw new FailurousConfigException("failurous.properties not found in classpath");
		}
		return configIn;
	}

}

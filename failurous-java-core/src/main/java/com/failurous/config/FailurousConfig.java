package com.failurous.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.failurous.FailSender;

/**
 * Provides access to the `failurous.properties` file which is expected
 * to be in the root directory of the classpath.
 * 
 * @author teroparv
 *
 */
public class FailurousConfig {

	public static FailurousConfig load() {
		try {
			Properties config = new Properties();
			config.load(getConfigIn());
			return new FailurousConfig(config);
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

	private final Properties store;
	
	private FailurousConfig(Properties store) {
		this.store = store;
	}
	
	public String getServerAddress() {
		return store.getProperty("failurous.server.address");
	}
	
	public String getAPIKey() {
		return store.getProperty("failurous.api.key");
	}
}

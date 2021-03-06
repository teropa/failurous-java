package com.failurous.guice;

import com.failurous.FailSender;
import com.failurous.config.FailurousConfig;
import com.failurous.config.FailurousConfigException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class FailSenderProvider implements Provider<FailSender> {
	
	private String serverAddress;
	private String apiKey;
	
	@Inject(optional=true)
	public void configureServerAddress(@FailurousServerAddress String serverAddress) {
		this.serverAddress = serverAddress;
	}

	@Inject(optional=true)
	public void configureAPIKey(@FailurousAPIKey String apiKey) {
		this.apiKey = apiKey;
	}

	public FailSender get() {
		ensureConfig();
		FailSender sender = new FailSender();
		sender.init(serverAddress, apiKey);
		return sender;
	}

	private void ensureConfig() {
		if (this.serverAddress != null && this.apiKey != null) 
			return;
		try {
			FailurousConfig config = FailurousConfig.load();
			if (this.serverAddress == null)
				this.serverAddress = config.getServerAddress();
			if (this.apiKey == null) {
				this.apiKey = config.getAPIKey();
			}
		} catch (FailurousConfigException f) {
			throw new FailurousConfigException("Could not configure failurous. "+
					"Either bind the configuration constants @FailurousServerAddress "+
					"and @FailurousAPIKey, or add the configuration file "+
					"failurous.properties into your classpath", f);
		}
	}
	
}

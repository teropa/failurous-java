package com.failurous;

import java.util.concurrent.ConcurrentHashMap;

public class FailSenderFactory {

	private static final ConcurrentHashMap<String, FailSender> SENDERS = new ConcurrentHashMap<String, FailSender>();
	
	public static FailSender getSender(String serverAddress, String apiKey) {
		final String key = serverAddress + apiKey;
		SENDERS.putIfAbsent(key, new FailSender(serverAddress, apiKey));
		return SENDERS.get(key);
	}
	
}

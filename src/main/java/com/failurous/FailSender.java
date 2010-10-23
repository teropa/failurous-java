package com.failurous;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

public class FailSender {

	private final BlockingQueue<Fail> failQueue = new LinkedBlockingQueue<Fail>();
	private final ObjectMapper failMapper = new ObjectMapper();
	private final Executor senderExecutor = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
	
	public FailSender(String serverAddress, String apiKey) {
		serverAddress = normalizeServerAddress(serverAddress);
		senderExecutor.execute(new Sender(serverAddress + "api/projects/" + apiKey + "/fails"));
	}

	private String normalizeServerAddress(String serverAddress) {
		if (!serverAddress.startsWith("http")) {
			serverAddress = "http://" + serverAddress;
		}
		if (!serverAddress.endsWith("/")) {
			serverAddress += "/";
		}
		return serverAddress;
	}
	
	public void send(Fail fail) {
		try {
			failQueue.put(fail);
		} catch (InterruptedException ie) {
			send(fail);
		}
	}
	
	private class Sender implements Runnable {

		private final HttpClient httpClient = new DefaultHttpClient();
		private final String endpointUrl;
		
		public Sender(String endpointUrl) {
			this.endpointUrl = endpointUrl;
		}
		
		public void run() {
			while (true) {
				try {
					List<Fail> batch = new ArrayList<Fail>();
					batch.add(failQueue.take());
					failQueue.drainTo(batch);
					send(batch);
				} catch (InterruptedException ie) {
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		
		private void send(List<Fail> fails) {
			try {
				String serialized = failMapper.writeValueAsString(fails);
				StringEntity entity = new StringEntity(serialized);
				entity.setContentType("application/json");
				
				HttpPost post = new HttpPost(endpointUrl);
				post.setEntity(entity);
				
				HttpResponse resp = httpClient.execute(post);
				resp.getEntity().consumeContent();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		
	}
	
	private class DaemonThreadFactory implements ThreadFactory {
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setName("failurous");
			thread.setDaemon(true);
			return thread;
		}
	}

}

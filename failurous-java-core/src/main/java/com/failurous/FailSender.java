package com.failurous;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import com.failurous.fail.Fail;

public class FailSender {

	private static Fail SHUTDOWN = new Fail("__shutdown__");
	
	private final BlockingQueue<Fail> failQueue = new LinkedBlockingQueue<Fail>();
	private final ObjectMapper failMapper = new ObjectMapper();
	private final ExecutorService senderExecutor = Executors.newSingleThreadExecutor(new DaemonThreadFactory());
	
	private String serverAddress;
	private String apiKey;
	
	public void init(String serverAddress, String apiKey) {
		this.serverAddress = normalizeServerAddress(serverAddress);
		this.apiKey = apiKey;
		senderExecutor.execute(makeSender());
	}

	private Sender makeSender() {
		return new Sender(serverAddress + "api/projects/" + apiKey + "/fails");
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public String getAPIKey() {
		return apiKey;
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
	
	public void shutdown(long timeout) throws InterruptedException {
		send(SHUTDOWN);
		senderExecutor.shutdown();
		senderExecutor.awaitTermination(timeout, TimeUnit.MILLISECONDS);
	}
	
	private class Sender implements Runnable {

		private final HttpClient httpClient = new DefaultHttpClient();
		private final String endpointUrl;
		private final AtomicBoolean running = new AtomicBoolean(true);
		
		public Sender(String endpointUrl) {
			this.endpointUrl = endpointUrl;
		}
		
		public void run() {
			while (running.get()) {
				try {
					List<Fail> batch = new ArrayList<Fail>();
					batch.add(failQueue.take());
					failQueue.drainTo(batch);
					
					if (batch.remove(SHUTDOWN)) {
						running.set(false);
					}
					
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

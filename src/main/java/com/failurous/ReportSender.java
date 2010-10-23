package com.failurous;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ReportSender {

	private final BlockingQueue<String> reportQueue = new LinkedBlockingQueue<String>();
	private final Executor senderExecutor = Executors.newFixedThreadPool(1);
	
	public ReportSender(String endpointUrl) {
		senderExecutor.execute(new Sender(endpointUrl));
	}
	
	public void sendReport(String report) {
		try {
			reportQueue.put(report);
		} catch (InterruptedException ie) {
			sendReport(report);
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
					String nextReport = reportQueue.take();
					send(nextReport);
				} catch (InterruptedException ie) {
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		
		private void send(String report) {
			try {
				HttpPost post = new HttpPost(endpointUrl);
				StringEntity entity = new StringEntity(report);
				entity.setContentType("application/json");
				post.setEntity(entity);
				HttpResponse resp = httpClient.execute(post);
				resp.getEntity().consumeContent();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}		
		
	}
	
}

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

public class ReportSender {

	private final BlockingQueue<Report> reportQueue = new LinkedBlockingQueue<Report>();
	private final ObjectMapper reportMapper = new ObjectMapper();
	private final Executor senderExecutor = Executors.newFixedThreadPool(1, new DaemonThreadFactory());
	
	public ReportSender(String endpointUrl) {
		senderExecutor.execute(new Sender(endpointUrl));
	}
	
	public void sendReport(Report report) {
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
					List<Report> batch = new ArrayList<Report>();
					batch.add(reportQueue.take());
					reportQueue.drainTo(batch);
					send(batch);
				} catch (InterruptedException ie) {
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
		}
		
		private void send(List<Report> reports) {
			try {
				String serialized = reportMapper.writeValueAsString(reports);
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
			thread.setDaemon(true);
			return thread;
		}
	}

}

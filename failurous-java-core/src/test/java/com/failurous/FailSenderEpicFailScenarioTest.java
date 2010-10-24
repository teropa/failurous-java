package com.failurous;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.failurous.fail.Fail;

public class FailSenderEpicFailScenarioTest {

	private static final int SERVERPORT = 8889;
	private static final int N = 1000000;
	
	private MockFailurousServer server;
	
	@Before
	public void startServer() throws Exception {
		server = new MockFailurousServer();
		server.start();
	}
	
	@After
	public void stopServer() throws Exception {
		server.stop();
	}
	
	@Test
	public void shouldBeAbleToSendALargeNumberOfFailsAtATime() throws Exception {
		FailSender sender = new FailSender("http://localhost:"+SERVERPORT, "whatever");
		sendFails(sender, N);
		sender.shutdown(20000);
		assertEquals(N, server.nReceived.get());
	}

	private void sendFails(final FailSender sender, int n) throws Exception {
		ExecutorService spammer = Executors.newCachedThreadPool();
		for (int i=0 ; i<n ; i++) {
			spammer.execute(makeSpamDispatch(sender));
		}
		spammer.shutdown();
		spammer.awaitTermination(10, TimeUnit.SECONDS);
	}

	private Runnable makeSpamDispatch(final FailSender sender) {
		return new Runnable() {
			public void run() {
				sender.send(new Fail("Epic fail"));
			}
		};
	}
	
	private class MockFailurousServer extends Server {
		
		private ObjectMapper mapper = new ObjectMapper();
		AtomicInteger nReceived = new AtomicInteger(0);
		
		public MockFailurousServer() {
			super(SERVERPORT);
			super.setHandler(new AbstractHandler() {
				public void handle(String target, Request req, HttpServletRequest httpReq, HttpServletResponse httpRes) throws IOException, ServletException {
					ArrayList<?> cntnt = mapper.readValue(httpReq.getInputStream(), ArrayList.class);
					nReceived.addAndGet(cntnt.size());
					req.setHandled(true);
				}
			});
		}
	}
	
}

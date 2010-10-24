package com.failurous.gwt.server;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import com.failurous.FailSender;
import com.failurous.FailSenderSingleton;
import com.failurous.fail.ExceptionDuringWebRequestFail;
import com.failurous.fail.WebRequestFail;
import com.google.gwt.logging.server.RemoteLoggingServiceImpl;
import com.google.gwt.logging.server.RemoteLoggingServiceUtil;
import com.google.gwt.logging.server.StackTraceDeobfuscator;
import com.google.gwt.logging.server.RemoteLoggingServiceUtil.RemoteLoggingException;
import com.google.gwt.logging.shared.RemoteLoggingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * An implementation of {@link RemoteLoggingServiceImpl} which reports 
 * received log messages to Failurous.
 * 
 * @author teroparv
 *
 */
public class FailurousRemoteLoggingServiceImpl extends RemoteServiceServlet implements RemoteLoggingService {
	private static final long serialVersionUID = 1L;

	private FailSender sender = FailSenderSingleton.get();

	// No deobfuscator by default
	private static StackTraceDeobfuscator deobfuscator = null;

	private static Logger logger = Logger.getLogger(RemoteServiceServlet.class.getName());

	private static String loggerNameOverride = null;

	/**
	 * Logs a Log Record which has been serialized using GWT RPC on the server.
	 * @return either an error message, or null if logging is successful.
	 */
	public final String logOnServer(LogRecord lr) {
		String strongName = getPermutationStrongName();
		try {
			RemoteLoggingServiceUtil.logOnServer(lr, strongName, deobfuscator, loggerNameOverride);
			if (isFail(lr)) {
				sendReport(lr);
			}
		} catch (RemoteLoggingException e) {
			logger.log(Level.SEVERE, "Remote logging failed", e);
			return "Remote logging failed, check stack trace for details.";
		}
		return null;
	}

	private boolean isFail(LogRecord lr) {
		return lr.getLevel() == Level.SEVERE || lr.getLevel() == Level.WARNING;
	}

	private void sendReport(LogRecord lr) {
		if (lr.getThrown() != null) {
			sender.send(new ExceptionDuringWebRequestFail(lr.getThrown(), getThreadLocalRequest()));
		} else {
			sender.send(new WebRequestFail(lr.getMessage(), getThreadLocalRequest()));
		}
	}

	/**
	 * By default, messages are logged to a logger that has the same name as
	 * the logger that created them on the client. If you want to log all messages
	 * from the client to a logger with another name, you can set the override
	 * using this method.
	 */
	public void setLoggerNameOverride(String override) {
		loggerNameOverride = override;
	}

	/**
	 * By default, this service does not do any deobfuscation. In order to do
	 * server side deobfuscation, you must copy the symbolMaps files to a
	 * directory visible to the server and set the directory using this method.
	 * @param symbolMapsDir
	 */
	public void setSymbolMapsDirectory(String symbolMapsDir) {
		if (deobfuscator == null) {
			deobfuscator = new StackTraceDeobfuscator(symbolMapsDir);
		} else {
			deobfuscator.setSymbolMapsDirectory(symbolMapsDir);
		}
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		String symbolMapsDir = config.getInitParameter("symbolMapsDirectory");
		if (symbolMapsDir != null) {
			symbolMapsDir = config.getServletContext().getRealPath(symbolMapsDir);
			setSymbolMapsDirectory(symbolMapsDir);
		}
	}

}

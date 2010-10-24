# Failurous Java Client GWT Integration

This module contains helper libraries for integrating GWT applications with Failurous

## Installation

Please see the [failurous-java README](http://github.com/teropa/failurous-java#readme) for general installation instructions.

Add the following dependency to your Maven project:

    <dependency>
      <groupId>com.failurous</groupId>
      <artifactId>failurous-java-gwt</artifactId>
      <version>0.1-SNAPSHOT</version>
    </dependency>

## Reporting client-side exceptions

Exceptions thrown in GWT applications running in users' web browsers can be reported to
Failurous. For this, we use the new logging API in GWT 2.1.

This feature is experimental, since GWT 2.1 itself is currently in the release candidate phase
and the API may change.

First, add the GWT logging module to your GWT application descriptor if you haven't already done so.
Also enable the remote log handler, so that log messages are sent to the server:

    <inherits name='com.google.gwt.logging.Logging'/>
    <set-property name='gwt.logging.simpleRemoteHandler' value='ENABLED'/>
    
Next, add the Failurous remote log handler to your `web.xml`:

	<servlet>
		<servlet-name>remoteLogger</servlet-name>
		<servlet-class>com.failurous.gwt.server.FailurousRemoteLoggingServiceImpl</servlet-class>
	</servlet>

	<servlet-mapping>
		<servlet-name>remoteLogger</servlet-name>
		<url-pattern>/myApplication/remote_logging</url-pattern>
	</servlet-mapping>	

Substitute `myApplication` above with the name of your GWT compilation unit.

Finally, you can use the [Java Logging API](http://download.oracle.com/javase/6/docs/api/java/util/logging/package-summary.html)
in your GWT app to log messages. Everything will be appended to your server logs, and log messages
of level WARNING or SEVERE will be reported to Failurous:

    Logger myLogger = Logger.getLogger(getClass().getName());
    
    ...
    
    myLogger.log(Level.SEVERE, "Something bad happened");
    
    try {
      // do stuff
    } catch (Exception e) {
      myLogger.log(Level.SEVERE, "Something bad happened while doing stuff", e);
    }
    

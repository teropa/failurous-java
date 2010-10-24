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
    
### Reporting Uncaught Exceptions

You can also enable reporting for uncaught exceptions (i.e. exceptions that would otherwise
cause JavaScript errors for users at runtime), by installing a GWT uncaught exception handler
when your module loads:

    public class MyApplication implements EntryPoint {

      private static final Logger LOGGER = Logger.getLogger("MyApplication");
  
      public void onModuleLoad() {
        GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
          public void onUncaughtException(Throwable t) {
            LOGGER.log(Level.SEVERE, "Uncaught exception", t);
          }
        });
        DeferredCommand.addCommand(new Command() {
          public void execute() {
            continueModuleLoad();			
          }
        });
      }

      private void continueModuleLoad() {
        // Normal application initialization code
      }

    }
 
Note that when exceptions are caught this way, they do not escape out of the GWT application,
and thus you can't debug them with normal JavaScript debuggers. You may want to consider
rethrowing the exceptions from `onUncaughtException()` to get around this.

### Deobfuscating stack traces

In the typical GWT setup the production code is obfuscated, and thus does not give very
useful stack traces. However, GWT does provide a [mechanism for deobfuscating stack traces](http://google-web-toolkit.googlecode.com/svn/javadoc/2.1/com/google/gwt/logging/server/StackTraceDeobfuscator.html).
By using this mechanism we can both have our production code obfuscated, and get Failurous
to show human-readable stack traces.

There's a couple of things we need to do to enable this feature:

#### 1. Invoke the GWT compiler with `-extra`

Add the `-extra` argument to the GWT compiler, and set it to point to a directory that's
accessible by the server at runtime (a good candidate would be `WEB-INF/gwt-extra`). If
you're using the [GWT Maven plugin](http://mojo.codehaus.org/gwt-maven-plugin/),
here's one way to do it:

    <plugin>
      <groupId>org.codehaus.mojo</groupId>
      <artifactId>gwt-maven-plugin</artifactId>
      <executions>
        <execution>
          <configuration>
            <module>com.mycompany.MyApplication</module>
            <extra>${project.build.directory}/${project.build.finalName}/WEB-INF/gwt-extra</extra>
          </configuration>
          <goals>
            <goal>compile</goal>
          </goals>
        </execution>
      </executions>
    </plugin>

With the above configuration, GWT will produce the symbol maps we need to the directory
`target/myapplication-myversion/WEB-INF/gwt-extra/myApplication/symbolMaps`.

#### 2. Point the Failurous remote exception logger to the GWT symbol maps

In `web.xml`, add a servlet init parameter to `FailurousRemoteLoggingServiceImpl`, which
points to the `symbolMaps` directory under the directory we configured above:

    <servlet>
      <servlet-name>remoteLogger</servlet-name>
      <servlet-class>com.failurous.gwt.server.FailurousRemoteLoggingServiceImpl</servlet-class>
      <init-param>
        <param-name>symbolMapsDirectory</param-name>
        <param-value>WEB-INF/gwt-extra/myApplication/symbolMaps</param-value>
      </init-param>
    </servlet>

Substitute `myApplication` above with the name of your GWT compilation unit.

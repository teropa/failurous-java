# Failurous Java client

Java client libraries for the [Failurous](http://failurous.r10.railsrumble.com/) exception tracking application.

## Installation

_Note:_ If you're using Guice, Spring, and/or GWT, we have some extra goodies for you.
See the *Integrations* section below.

### 1. Configure pom.xml

First, add the Sonatype Nexus repository to your POM:

    <repository>
      <id>sonatype-nexus</id>
      <url>https://oss.sonatype.org/content/groups/public</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
		
Next, add a dependency to the Failurous Java client:

    <dependency>
      <groupId>com.failurous</groupId>
      <artifactId>failurous-java</artifactId>
      <version>0.1-SNAPSHOT</version>
    </dependency>
		
### 2. Configure Failurous

Add a file called `failurous.properties` to the root of your classpath. (In Maven projects this is usually `src/main/resources`.)
Set its contents as follows:

    failurous.server.address = <YOUR-FAILUROUS-INSTALLATION>
    failurous.api.key = <API-FAILUROUS-PROJECT-API-KEY>
    
You'll find your API key in the Failurous web application, by clicking the "API key & Settings" link on your project page.

## Reporting the exceptions that users see 

For many, the most important fails to know about are the ones users see. To report them,
add the Failurous servlet filter to `web.xml`:   

	<filter>
		<filter-name>failurous</filter-name>
		<filter-class>com.failurous.FailurousServletFilter</filter-class>
	</filter>
	
	<filter-mapping>
		<filter-name>failurous</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
		
The filter will catch any exceptions your app throws. It will deliver them to the Failurous
server, where you can see them through the web interface.		

Notice that the position of the `filter-mapping` element affects which exceptions Failurous will report.
If you have other filters, and they are defined before the failurous filter, any exceptions they cause
will not be reported.

## Reporting exceptions thrown deep down in your app

You may also want to report exceptions that happen in lower levels of your applications - those
that are not thrown in the user's face but you nevertheless want to know about.
In these situations, you can invoke the `FailSenderSingleton` service directly.

You can also use the `ExceptionFail` convenience class to include the usual exception
information (stack trace, etc.):

    try {
      myFailingMethod();
    } catch (MyException e) {
      FailSenderSingleton.get().send(new ExceptionFail(e));
      throw e;
    }
       
## Sending custom notifications

You can also report custom notifications about anything you feel your app needs to let 
you know about. 

To do this, build an instance of `Fail` and send it with the `FailSenderSingleton` API:

    FailSender sender = FailSenderSingleton.get();
    sender.send(new Fail("50 invalid login attempts by "+loginName, "LoginService.login()"));

More information can be included by adding sections and fields:

    Fail myFail = new Fail("50 invalid login attempts", "LoginService.login()");
    
    FailSection summary = myFail.addSection("Summary");
    summary.addField("User", loginName);
    
    FailSenderSingleton.get().send(myFail);
    
## Integrations

* [Google Guice integration](http://github.com/teropa/failurous-java/tree/master/failurous-java-guice/)
* [Spring integration](http://github.com/teropa/failurous-java/tree/master/failurous-java-spring/)
* [GWT](http://github.com/teropa/failurous-java/tree/master/failurous-java-gwt/)

## Support & Bug Reports

\#failurous at Freenode

[Failurous-java Lighthouse](http://failurous.lighthouseapp.com/projects/62311-failurous-java)

## License

Copyright (c) 2010 Mikko Nylén, Tero Parviainen & Antti Forsell

See LICENSE


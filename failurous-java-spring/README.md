# Failurous Java Client Spring Integration

This module makes it easy to integrate Failurous with Spring 3.

## Installation

Please see the [failurous-java README](http://github.com/teropa/failurous-java#readme) for general installation instructions.

### 1. Maven

Add the following dependency to your Maven project:

    <dependency>
      <groupId>com.failurous</groupId>
      <artifactId>failurous-java-spring</artifactId>
      <version>0.1-SNAPSHOT</version>
    </dependency>

### 2. Spring

Firstly, you'll need to have the `failurous.properties` file in your classpath, as described in the 
[failurous-java README](http://github.com/teropa/failurous-java#readme).

#### Classpath scanning

All the beans provided by Failurous can be picked up from the classpath automatically if you
are using [Spring classpath scanning](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/beans.html#beans-classpath-scanning).
Just add the failurous package to the scanned packages:

    <context:component-scan base-package="com.failurous.spring, my.other.package"/>

#### XML configuration

If you don't want to use classpath scanning for Failurous, you need to register the `FailurousConfiguration` bean
in your Spring configuration XML:

    <bean id="failurousConfiguration" class="com.failurous.spring.FailurousConfiguration"/>
    

## Reporting exceptions occurring in web requests

See the [failurous-java README](http://github.com/teropa/failurous-java#readme).

## Reporting exceptions occurring elsewhere

Failurous provides an AspectJ aspect, which can be used to automatically report exceptions thrown
from all methods annotated with `@ReportExceptions`:

    @ReportExceptions
    public String myMethod(String myArg) {
      throw new IllegalArgumentException("This will be sent to Failurous");
    }

Unless you're using classpath scanning as describe above, you'll need to let Spring know about the
bean in your XML configuration:

    <bean id="exceptionReportInterceptor" class="com.failurous.spring.aop.ExceptionReportInterceptor"/>
    
To activate the aspect, you'll need to do one of the following:

### Autoproxying

Enable [Spring's AOP auto-proxying capabilities](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/aop-api.html#aop-autoproxy):

    <aop:aspectj-autoproxy/>
    
### Explicit 

To explicitly enable the aspect, you'll just need to let Spring know about it:

    <aop:config>
      <aop:aspect ref="exceptionReportInterceptor">
        <aop:after-throwing
          pointcut="@annotation(com.failurous.aop.ReportExceptions)"
          method="reportException"
          throwing="t"/>
      </aop:aspect>  		
    </aop:config>

## Custom notifications

See [failurous-java README](http://github.com/teropa/failurous-java#readme)

In Spring the `FailSender` instance is available to your code via injection:

    public class MyClass {
    
      private @Autowired FailSender failSender;
      
      public void myMethod() {
        failSender.send(new Fail("my failure notification"));
      }
    }
    

# Failurous Java Client Guice Integration

This module makes using Failurous from Guice a breeze.

## Installation

Please see the [failurous-java README](http://github.com/teropa/failurous-java#readme) for general installation instructions.

### 1. Maven

Add the following dependency to your Maven project:

    <dependency>
      <groupId>com.failurous</groupId>
      <artifactId>failurous-java-guice</artifactId>
      <version>0.1-SNAPSHOT</version>
    </dependency>

### 2. Guice

Install `FailurousModule` module to your Guice Injector:

#### Example

    Guice.createInjector(new FailurousModule(), new MyModule1(), new MyModule2());
    
or

    public class MyModule extends AbstractModule {
      public void configure() {
        install(new FailurousModule());
        // ... other stuff ...
      }
    }
        
      
## Guicy Configuration

With Guice you can use annotated properties for configuring the Failurous server address and API key.
This is an alternative to configuring via `failurous.properties` (which will be used if you don't bind
the configuration with annotations).

    bindConstant().annotatedWith(FailurousServerAddress.class).to("http://localhost:3000/");
    bindConstant().annotatedWith(FailurousAPIKey.class).to("my API key");

## Reporting exceptions occurring in web requests

If you're using Guice `ServletModule` you can install the Failurous servlet filter from its configuration:

    filter("/*").through(FailurousServletFilter.class);
    
Be sure to add this filter before anything else if you want to report all exceptions.

## Reporting exceptions occurring elsewhere

To report exceptions occurring outside the request/response cycle, annotate the methods from which
you want reports with `@ReportExceptions`:

    @ReportExceptions
    public String myMethod(String myArg) {
      throw new IllegalArgumentException("This will be sent to Failurous");
    }

## Custom notifications

See [failurous-java README](http://github.com/teropa/failurous-java#readme)

In Guice the `FailSender` instance is available to your code via injection:

    public class MyClass {
    
      @Inject private FailSender failSender;
      
      public void myMethod() {
        failSender.send(new Fail("my failure notification"));
      }
    }
    

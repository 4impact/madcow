---
layout: post
title: Configuration
description: Details about the configuration magic of Madcow.
---

Once Madcow is setup and running correctly, the configuration files within the _conf_ directory will need to be changed for the particular site that you are working on.
>Please note that the configuration has changed significantly from the way it was setup under Madcow v1.

## Madcow Configuration
To assist in managing multiple target environments, Madcow 2.0 now supports a global configuration file `madcow-config.xml`. This XML file is used to configure the
- execution runner plugin to use (including the default browser to use in the tests) as well as
- other environment configuration data for the different web application environments under test.

The included default example is included below:
{% highlight xml %}
<madcow>
    <execution>
        <runner>
            <type>au.com.ps4impact.madcow.runner.webdriver.WebDriverStepRunner</type>
            <parameters>
                <!--<browser>Firefox</browser>-->
                <browser>HtmlUnit</browser>
            </parameters>
        </runner>
        <env.default>DEV</env.default>
        <threads>10</threads>
    </execution>
    <environments>
        <environment name="DEV">
            <invokeUrl>
                <ADDRESSBOOK>http://madcow-test.4impact.net.au:8080/madcow-test-site-2</ADDRESSBOOK>
                <CREATETABLE>http://madcow-test.4impact.net.au:8080/madcow-test-site-2/address/createTableLayout</CREATETABLE>
            </invokeUrl>
        </environment>
        <environment name="TEST">
            <invokeUrl>
                <ADDRESSBOOK>http://madcow-test.4impact.net.au:8280/madcow-test-site-2</ADDRESSBOOK>
            </invokeUrl>
        </environment>
    </environments>
</madcow>
{% endhighlight %}

-------------------

### Madcow Base Configuration Options
There are a number of base Madcow configuration options that can be set to be used with all madcow test executions. 

These include the following:
*   `<env.default>` [Default Environment](#default_environment)
*   `<parallel>` [Parallel Execution](#parallel_execution)
*   `<threads>` [Threads](#threads)
*   `<retries>` [Retry Count](#retry_count)



#### Default Environment
> Specifies the Default Environment that should be used when no environment `-e` parameter is provided when running tests at the command line

This is a special default environment tag which basically tells Madcow 2.0 which environment (URL) it should run tests against should one not be specified at the command line.

For example if we had two environments called `DEV` and `TEST` and wanted all our tests to run against the `TEST` environment by default we would set
{% highlight xml %}
<env.default>TEST</env.default>
{% endhighlight %}
For more information on how the actual environment configuration data is setup see [Environment Configuration Data](#environment_configuration_data).

#### Parallel Execution
> Specifies if the tests should be run in parallel mode or not. Accepts true or false, defaults to true.

For example: 
{% highlight xml %}
<parallel>false</parallel>
{% endhighlight %}
will execute all tests sequentially. 

#### Threads
> Specifies if the number of threads that should be used to run the tests. Accepts a whole number, defaults to 10. 

For example: 
{% highlight xml %}
<threads>2</threads>
{% endhighlight %}
will effectively execute 2 tests at a time also. 

#### Retry Count
> Specifies if the number of retries that should be attempted for a test before it is considered as failed. Accepts a whole number, defaults to 1. 

For example: 
{% highlight xml %}
<retries>2</retries>
{% endhighlight %}
will effectively execute a failed test a second time, marking it as failed if and only if it fails a second time as well. (Useful for remote server mode if experiencing connection dropouts)

------------------

### Execution Runner
This configuration file above defines the default test runner `type` to be the provided default `WebDriverStepRunner` plugin. The default parameter for `browser` above is set to `HtmlUnit`. By simply changing the browser parameter to be `Firefox` instead, you can force Madcow 2.0 to run your test using the [Mozilla Firefox](http://www.getfirefox.com) browser.  Most of this configuration you won't need to change out of the box unless you wish to extend Madcow 2.0 with your own execution runner plugin. More information on parameters for the runner are included in the next section.

#### Execution Runner Specific Parameters
Parameters in the XML are passed to the defined runner `type`. You can have as many as you would like but they will only be used if accepted by the defined runner. By default Madcow 2.0 comes setup with the `WebDriverStepRunner` runner which provides the following parameters out of the box: 
*   `<Browser>` - [Browser Mode](#browser_mode)
*   `<Emulate>` - [Emulation Mode](#emulation_mode)
*   `<RemoteServerUrl>` - [Remote Server Mode](#remote_server_mode)
*   `<ImplicitTimeout>` - [Implicit Timeout](#implicit_timeout)
*   `<ScriptTimeout>` - [Script Timeout](#script_timeout)
*   `<PageLoadTimeout>` - [Page Load Timeout](#page_load_timeout)


##### Browser Mode
> Specifies the browser type that Madcow 2.0 will run using. By default it will use HtmlUnit. 
> To use any of the other browsers they must first be supported and installed on your operating system.

Currently Madcow 2.0 only supports these values:
*   HtmlUnit
*   Firefox
*   Remote
*   Chrome
*   PhantomJs
*   IE (**untested**)

For example:

{% highlight xml %}
<parameters>
     <browser>PhantomJs</browser>
</parameters>
{% endhighlight %}    
would run Madcow using a locally installed PhantomJs instance. Note: In this particular case Madcow may also require PhantomJS to be on the system path. 

##### Emulation Mode
> Determines the browser emulation mode that HtmlUnit will run under 
>
> **OR**  Determines the browser instance type to start up remotely when using with Remote Server Mode. 
>
> Only valid when `<browser>HtmlUnit</browser>` OR `<browser>Remote</browser>`

When using the HtmlUnit browser mode `<browser>HtmlUnit</browser>` all requests will be made to the server as though they have come from the selected browser, effectively setting the provided user-agent on all requests. 
When using the Remote browser mode `<browser>Remote</browser>` all new tests will attempt to started up and run using the provided browser application. 

The following are valid values for this configurable:
*   IE7
*   IE8
*   IE9
*   CHROME
*   PHANTOMJS
*   FF3
*   FF3.6
*   FIREFOX
*   OPERA
*   SAFARI

For example:

{% highlight xml %}
<parameters>
     <browser>HtmlUnit</browser>
     <emulate>IE8</emulate>
</parameters>
{% endhighlight %}    
would run HTMLUnit in IE8 emulation mode. 


##### Remote Server Mode
> Specifies the URL where the Remote Webdriver Grid Server is that all tests should be routed through for Remote operations. 
>
> Only valid when `<browser>Remote</browser>`

When the remote browser is selected using
{% highlight xml %}
<browser>Remote</browser>
{% endhighlight %}

Madcow will attempt to look for a remote webdriver grid server.
You can need to define this server URL with the parameter
{% highlight xml %}
<remoteServerUrl>http://localhost:4444/wd/hub</remoteServerUrl>
{% endhighlight %}

You can then use the [emulate](#emulation_mode) parameter to set the browser that you require from the remote webdriver server.
You can spin up a remote webdriver server instance using the instructions and jar file found <a href="http://code.google.com/p/selenium/wiki/RemoteWebDriverServer" target="_blank">here</a> or alternatively just point your madcow instance straight at your <a href="http://www.saucelabs.com" target="_blank">SauceLabs</a> url with your username and password. 

For example:
{% highlight xml %}
<parameters>
     <browser>Remote</browser>
     <emulate>CHROME</emulate>
     <remoteServerUrl>http://sauceLabsUser:sauceLabsApiKey@ondemand.saucelabs.com:80/wd/hub</remoteServerUrl>
</parameters>
{% endhighlight %}    
would run all tests against the Remote Webdriver Grid Server configured in the cloud at SauceLabs, firing up a Chrome browser instance for each of the tests. 

##### Implicit Timeout
> Specifies the amount of time the driver should wait when searching for an element if it is not immediately present. 

For example:
{% highlight xml %}
<parameters>
     <implicitTimeout>10</implicitTimeout>
</parameters>     
{% endhighlight %}
would wait 10 seconds before declaring it could not find an element on the page. 

##### Script Timeout
> Sets the amount of time to wait for an asynchronous script to finish execution before throwing an error. 

For example:
{% highlight xml %}
<parameters>
     <scriptTimeout>10</scriptTimeout>
</parameters>     
{% endhighlight %}
would wait 10 seconds for long running javascript to finish execution before continuing. 

##### Page Load Timeout
> Sets the amount of time to wait for a page load to complete before throwing an error. 

For example:
{% highlight xml %}
<parameters>
     <pageLoadTimeout>10</pageLoadTimeout>
</parameters>     
{% endhighlight %}
would wait 10 seconds for a page to load before failing if the response from the webserver was not returned. 

-----------------

### Environment Configuration Data
Also in the above example `madcow-config.xml`, we have defined two different test environments, `DEV` and `TEST` with various data to be made available to different test steps.

For example the following script will run the `AddressTest` on the `DEV` environment.
{% highlight bash %}
 ./runMadcow.sh -a AddressTest -e DEV
{% endhighlight %}

#### Configuring Multiple Named Test Environments
Whilst developing and testing software, you may in fact have the need to have multiple versions of your web application running on multiple servers. 
Madcow 2.0 accomodates for the need of wanting to run your test cases against various environments in order to prevent regression bugs being introduced. 
You may for example have a DEV environment used only for your CI tool and a TEST environment used only by your SYSTEM testers. 
To set this up in madcow 2.0 you could simply define something like: 
{% highlight xml %}
<environment name="DEV">
   <invokeUrl>
       <ADDRESSBOOK>http://dev.4impact.net.au/myAwesomeSite</ADDRESSBOOK>
   </invokeUrl>
</environment>
<environment name="TEST">
   <invokeUrl>
       <ADDRESSBOOK>http://test.4impact.net.au/myAwesomeSite</ADDRESSBOOK>
   </invokeUrl>
</environment>
{% endhighlight %}

This would then allow you to be able to pick which test environment you want to run a test suite against at the command line. 

For example running 
{% highlight bash %}
./runMadcow.sh -e TEST -t MyAwesomeTest
{% endhighlight %}
would run the MyAwesomeTest.grass test case against the TEST environment with ADDRESSBOOK URL `http://test.4impact.net.au/myAwesomeSite`. 


#### Using Environment Based Configuration Data in Test Cases
The environment information configured in `madcow-config.xml` can also be substituted in your test cases as the URL being invoked by the Invoke Url command.

For instance, having a hard-coded URL for a Create Address page,
{% highlight bash %}
invokeUrl = http://madcow-test.4impact.net.au:8080/madcow-test-site-2/address/create
{% endhighlight %}

should really be expressed as
{% highlight bash %}
invokeUrl = ADDRESSBOOK/address/create
{% endhighlight %}

where the ADDRESSBOOK key is defined in the Madcow 2.0 config file under the DEV environment section as
{% highlight xml %}
<environment name="DEV">
   <invokeUrl>
       <ADDRESSBOOK>http://dev.4impact.net.au/myAwesomeSite</ADDRESSBOOK>
   </invokeUrl>
</environment>
{% endhighlight %}

With this example, when Madcow 2.0 is executing the `invokeUrl` command, it will substitute ADDRESSBOOK with the DEV environments URL `http://dev.4impact.net.au/myAwesomeSite`

should you execute the same test case however with a different named environment by excuting
{% highlight bash %}
./runMadcow.sh -a AddressTest -e TEST
{% endhighlight %}

Madcow 2.0 would substitue ADDRESSBOOK with the TEST environments URL instead, i.e. `http://test.4impact.net.au/myAwesomeSite`
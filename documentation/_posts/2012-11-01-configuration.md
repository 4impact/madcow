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

### Execution Runner
This configuration file above defines the default test runner `type` to be the provided default `WebDriverStepRunner` plugin. The default parameter for `browser` above is set to `HtmlUnit`. By simply changing the browser parameter to be `Firefox` instead, you can force Madcow 2.0 to run your test using the [Mozilla Firefox](http://www.getfirefox.com) browser.  Most of this configuration you won't need to change out of the box unless you wish to extend Madcow 2.0 with your own execution runner plugin. More information on parameters for the runner are included in the next section.

#### Parameters
Parameters in the XML are passed to the defined runner `type`. You can have as many as you would like but they will only be used if accepted by the defined runner. By default Madcow 2.0 comes setup with the `WebDriverStepRunner` runner which provides the following parameters out of the box.
##### Browser
Specifies the internet browser that Madcow 2.0 will emulate by default. Currently Madcow 2.0 only supports these values:
* Firefox
* HtmlUnit

> More coming soon

##### Emulate
When using the parameter `<browser>HtmlUnit</browser>` you can also specify an `<emulate>` tag. This will determine the browser emulation mode that HtmlUnit will use to run under. The following are valid values for this configurable:
* IE6
* IE7
* IE8
* FIREFOX
* FF3
* FF3.6

For example:

{% highlight xml %}
<parameters>
     <browser>HtmlUnit</browser>
     <emulate>IE8</emulate>
</parameters>
{% endhighlight %}

#### Default Environment
There is also a special default environment tag defined in the `madcow-config.xml` which basically tells Madcow 2.0 which environment it should run tests against should one not be specified at the command line.
For example if we had two environments called `DEV` and `TEST` and we wanted all our tests to run against the `TEST` environments by default we could simply alter the tag to look like this...
{% highlight xml %}
<env.default>TEST</env.default>
{% endhighlight %}
For more information on how the actual environment configuration data is setup is detailed below.

### Environment Configuration Data
Also in the above example `madcow-config.xml`, we have defined two different test environments, `DEV` and `TEST` with various data to be made available to different test steps.

For example the following script will run the `AddressTest` on the `DEV` environment.
{% highlight bash %}
 ./runMadcow.sh -a AddressTest -e DEV
{% endhighlight %}


#### Using Environment Based Configuration Data in Test Cases
The environment information configured in `madcow-config.xml` can also be substituted in your test cases as the URL being invoked by the Invoke Url command.

For instance, having a hard-coded URL for a Create Address page,
{% highlight bash %}
invokeUrl = http://madcow-test.4impact.net.au:8080/madcow-test-site-2/address/create
{% endhighlight %}

can be expressed as
{% highlight bash %}
invokeUrl = ADDRESS_BOOK/address/create
{% endhighlight %}

where the _ADDRESS_BOOK_ key is defined in the Madcow 2.0 config file under the DEV environment section as
{% highlight xml %}
<environment name="DEV">
   <invokeUrl>
       <ADDRESSBOOK>http://madcow-test.4impact.net.au:8080/madcow-test-site-2</ADDRESSBOOK>
   </invokeUrl>
</environment>
{% endhighlight %}

For this example, when Madcow 2.0 is executing the `invokeUrl` command, it will substitute _ADDRESS_BOOK_ with the DEV environments URL `http://madcow-test.4impact.net.au:8080/madcow-test-site-2`

should you execute the same test case however with a different environment by excuting
{% highlight bash %}
./runMadcow.sh -a AddressTest -e TEST
{% endhighlight %}

Madcow 2.0 would substitue _ADDRESSBOOK_ with the TEST environments URL instead, i.e. `http://madcow-test.4impact.net.au:8280/madcow-test-site-2`
---
layout: post
title: Getting Started
description: A quick start guide to getting up and running with Madcow
---

# Setting Up

## Requirements

* Java v1.6+

## Installation

* unzip the madcow-x-install.zip (where x is the version of Madcow you are installing) into your new madcow project directory

{% highlight bash %}
mkdir new-madcow-test-project
cd new-madcow-test-project
unzip madcow-x-install.zip
{% endhighlight %}


After extracting the Madcow package you should have a directory structure similar to the below. Each of the folders purpose is explained here also.  
{% highlight bash %}
--new-madcow-test-project
|- /conf         (all Madcow configuration files)
|- /lib          (any Madcow plugin extensions)
|- /mappings     (all Madcow mappings data, this folder not exist unless created)
|- /results      (the results of the last Madcow test run, this will not exist until the first run)
|- /templates    (any Madcow resuable test templates that you have created, see Templates)
|- /tests        (all the Madcow test case "Grass" files)
|- runMadcow.bat (script to run Madcow on Windows)
|- runMadcow.sh  (script to run Madcow on UNIX based OSes) 
{% endhighlight %}


## Running Madcow
Run the either the `runMadcow.sh` or `runMadcow.bat` scripts from the command line in the root directory of this project directory structure

### Running Linux and Mac OSX Script
{% highlight bash %}
user@host:~/new-madcow-test-project$ ls -a
.  ..  .madcow  build  conf  lib  runMadcow.sh  runMadcow.bat  test
user@host:~/new-madcow-test-project$ chmod u+x runMadcow.sh
user@host:~/new-madcow-test-project$ ./runMadcow.sh
{% endhighlight %}

> Note the use of `chmod` in the above example to ensure that you have permissions to run madcow. 

### Running Windows Script
{% highlight bash %}
C:\new-madcow-test-project> runMadcow.bat
{% endhighlight %}

## Ensuring Madcow 2.0 is Setup Correctly 

To ensure that Madcow 2.0 is setup correctly you can simply execute the following command on Mac:
{% highlight bash %}
user@host:~/new-madcow-test-project$ ./runMadcow.sh --help
{% endhighlight %}

or the following on Windows: 

{% highlight bash %}
C:\new-madcow-test-project> runMadcow.bat --help
{% endhighlight %}

It should returns something similar to the below which then means you are all set to start writing your first test cases. Welcome to the wonderful world of Test Automation!
{% highlight bash %}
user@host:~/new-madcow-test-project$ ./runMadcow.sh --help
usage: runMadcow [options]
Options:
 -a,--all               Run all tests
 -e,--env <env-name>    Environment to load from the madcow-config.xml
 -h,--help              Show usage information
 -t,--test <testname>   Comma seperated list of test names
{% endhighlight %}


## Madcow Upgrades 

1. Remove the Madcow directory (_.madcow_) from within your Madcow test project
{% highlight bash %}
user@host:~/new-madcow-test-project$ rm -rf .madcow
{% endhighlight %}

2. Unzip the Madcow upgrade zip file within your Madcow test project
{% highlight bash %}
unzip madcow-x.x.x-upgrade.zip
{% endhighlight %}

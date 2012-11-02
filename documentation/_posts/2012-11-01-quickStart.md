---
layout: post
title: Quick Start
description: A quick start guide to getting up and running with Madcow
---

# Setting Up

## Requirements

* Java v1.6+ - [[http://java.com]]

## Installation

* unzip the madcow-x-install.zip (where x is the version of Madcow you are installing) into your new madcow project directory

{% highlight bash %}
mkdir new-madcow-test-project
cd new-madcow-test-project
unzip madcow-x-install.zip
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

### Running Windows Script
{% highlight bash %}
C:\new-madcow-test-project> runMadcow.bat
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
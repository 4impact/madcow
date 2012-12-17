---
layout: postBasicOperation
title: Madcow Skip
description: Tells Madcow to skip this step.
category: basic operations
---

If this is found as the first line of a madcow test, it will skip the execution of this test altogether.
This is useful for when you want to check into version control part way through a writing a test that is not yet working.

## Usage
{% highlight bash %}
madcow.ignore
{% endhighlight %}

## Examples
{% highlight bash %}
# this test is disabled because i'm still working on it
madcow.ignore

invokeUrl = http://www.google.com
{% endhighlight %}



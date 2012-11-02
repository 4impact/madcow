---
layout: postBasicOperation
title: Wait For Text
description: Waits for a given text value to exist for a given HTML element.
category: basic operations
---

Waits for a given text value to exist for a given HTML element, by default waits 20 seconds before reporting a test error.

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].waitForText = [MADCOW:text value to wait for]
{% endhighlight %}

## Examples
{% highlight bash %}
myCountryField.waitForText = "New Zealand"
{% endhighlight %}



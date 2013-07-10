---
layout: postBasicOperation
title: Check Value Contains
description: Checks a given HTML element contains a given text value.
category: basic operations
---

_Please note, this operation differs from Check Value in that the specified value has to match only a **portion** of the field value, not the entire text._

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].checkValueContains = "<<some text value>>"
{% endhighlight %}

## Examples
{% highlight bash %}
countryField.checkValueContains = "New Zeal"
{% endhighlight %}



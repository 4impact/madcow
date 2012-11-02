---
layout: postBasicOperation
title: Check Value
description: Checks a given HTML element contains a given text value.
category: basic operations
---

_Please note, this operation differs from Check Value Contains in that the specified value has to match the **entire** field value, not just a portion of it._

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].checkValue = [MADCOW:some text value]
{% endhighlight %}

## Examples
{% highlight bash %}
countryField.checkValue = "New Zealand"
{% endhighlight %}



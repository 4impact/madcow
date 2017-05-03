---
layout: postBasicOperation
title: Check Value Not Empty
description: Checks a given HTML element not empty of a given text value.
category: basic operations
---

## Usage

{% highlight bash %}
[MADCOW:htmlElementReference].checkValueNotEmpty = [<<some text value>>]
{% endhighlight %}

## Examples

checking element <a href="#" name="aLinkName" id="aLinkId">A link</a>

{% highlight bash %}
aLinkId.checkValueNotEmpty = "A link"
{% endhighlight %}
{% highlight bash %}
aLinkId.checkValueNotEmpty   (true)
{% endhighlight %}

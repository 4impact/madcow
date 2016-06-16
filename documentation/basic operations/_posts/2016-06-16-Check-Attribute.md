---
layout: postBasicOperation
title: Check Attribute
description: Tests one or more named attributes of an element to see if they each match the desired text.
category: basic operations
---

Tests one or more named attributes of an element to see if they each match the desired text.

## Usage

{% highlight bash %}
[MADCOW:htmlElementReference].checkAttribute = [<<map of attribute : value pairs>>]
{% endhighlight %}

## Examples

checking element <div id="theComplexThing" role="radio" group="master" />

{% highlight bash %}
theComplexThing.checkAttribute = [role:"radio", group:"master"]
{% endhighlight %}

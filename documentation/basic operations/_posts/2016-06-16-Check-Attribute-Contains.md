---
layout: postBasicOperation
title: Check Attribute Contains
description: Tests one or more named attributes of an element to see if they each contain the desired text.
category: basic operations
---

Tests one or more named attributes of an element to see if they each contain the desired text.

## Usage

{% highlight bash %}
[MADCOW:htmlElementReference].checkAttributeContains = [<<map of attribute : value pairs>>]
{% endhighlight %}

## Examples

checking element <div id="theComplexThing" role="radio" group="master" />

{% highlight bash %}
theComplexThing.checkAttribute = [role:"adio", group:"mast"]
{% endhighlight %}

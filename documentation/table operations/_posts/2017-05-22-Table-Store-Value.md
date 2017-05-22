---
layout: postTableOperation
title: Table Store Value
description: Ability to store a value (parameter) of an table element.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].table.currentRow.storeValue = <<text value to store>>
{% endhighlight %}

## Examples

{% highlight bash %}
theTable.table.currentRow.storeValue = ['Column Number 1' : 'Parameter']
{% endhighlight %}



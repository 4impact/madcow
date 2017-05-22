---
layout: postTableOperation
title: Table Select Row
description: Sets a value on a combo box element.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].table.selectRow = <<text row value>>
{% endhighlight %}

## Examples

{% highlight bash %}
cell-editing-1025.table.selectRow = row2

{% endhighlight %}

{% highlight bash %}
theTable.table.selectRow = ['Column Number 2' : 'Country']
{% endhighlight %}
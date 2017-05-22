---
layout: postTableOperation
title: Table Select Field
description: Sets a value on a combo box element.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].table.currentRow.selectField = <<text value of index>>
{% endhighlight %}

## Examples

{% highlight bash %}
cell-editing-1025.table.selectRow = first
cell-editing-1025.table.currentRow.selectField = ['column2' : 'Sunny']
cell-editing-1025.table.currentRow.selectField = ['Light' : 'Sunny']
{% endhighlight %}

{% highlight bash %}
cell-editing-old.table.selectRow = row2
cell-editing-old.table.currentRow.selectField = ['column2' : 'Cameras and Accessories']
{% endhighlight %}
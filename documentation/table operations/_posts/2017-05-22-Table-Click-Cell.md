---
layout: postTableOperation
title: Table Click Cell
description: It selects the specific table cell.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].table.selectRow = <<text value>>
[MADCOW:htmlElementReference].table.currentRow.selectRow = <<text value>>
{% endhighlight %}

## Examples

{% highlight bash %}
cell-editing-1025.table.selectRow = first
cell-editing-1025.table.currentRow.clickCell = 'Common Name'
{% endhighlight %}

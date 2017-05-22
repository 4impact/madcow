---
layout: postTableOperation
title: Table Verify Select Field Options
description: Verifies a table cell equals a given text value for the specified column on the current selected row in an HTML table.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlTableReference].table.currentRow.verifySelectFieldOptions = [columnName : [columnValue1, columnValue2, columnValue3]]
{% endhighlight %}

## Examples
{% highlight bash %}
cell-editing-1025.table.currentRow.verifySelectFieldOptions = ['column2' : ['Shade', 'Mostly Shady', 'Sun or Shade', 'Mostly Sunny', 'Sunny']]
{% endhighlight %}

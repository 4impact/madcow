---
layout: postTableOperation
title: Table Click Link
description: Clicks a link within a specific column on the current selected row in an HTML table.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlTableReference].table.currentRow.clickLink = [columnName]
{% endhighlight %}

## Examples
{% highlight bash %}
searchResultsTable.table.currentRow.clickLink = 'Id'
searchResultsTable.table.currentRow.clickLink = firstColumn
searchResultsTable.table.currentRow.clickLink = lastColumn
searchResultsTable.table.currentRow.clickLink = column2
{% endhighlight %}

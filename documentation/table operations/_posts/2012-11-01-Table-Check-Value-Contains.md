---
layout: postTableOperation
title: Table Check Value Contains
description: Checks a table cell contains a given text value for the specified column on the current selected row in an HTML table.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlTableReference].table.currentRow.checkValueContains = [columnName : columnValue, columnName2 : columnValue2]
{% endhighlight %}

## Examples
{% highlight bash %}
searchResultsTable.table.currentRow.checkValueContains = [country : 'South '] 
searchResultsTable.table.currentRow.checkValueContains = [country : 'New Zeal', province : 'Otago']
searchResultsTable.table.currentRow.checkValueContains = ['column3' : 'New Zealand']
searchResultsTable.table.currentRow.checkValueContains = ['firstColumn' : 'Unit']
searchResultsTable.table.currentRow.checkValueContains = ['lastColumn' : 'My name is ']
{% endhighlight %}

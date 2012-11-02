---
layout: postTableOperation
title: Table Value
description: Sets a table cell text value with a given text value for the specified column on the current selected row in an HTML table.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlTableReference].table.currentRow.value = [columnName : columnValue, columnName2 : columnValue2]
{% endhighlight %}

## Examples
{% highlight bash %}
searchResultsTable.table.currentRow.value = [country : 'New Zealand'] 
searchResultsTable.table.currentRow.value = [country : 'New Zealand', province : 'Otago']
searchResultsTable.table.currentRow.value = ['firstColumn' : 'UnitA']
searchResultsTable.table.currentRow.value = ['lastColumn' : 'Earth']
searchResultsTable.table.currentRow.value = ['column3' : 'Australia']
{% endhighlight %}

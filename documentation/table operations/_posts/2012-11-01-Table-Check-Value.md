---
layout: postTableOperation
title: Table Check Value
description: Checks a table cell equals a given text value for the specified column on the current selected row in an HTML table.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlTableReference].table.currentRow.checkValue = [columnName : columnValue, columnName2 : columnValue2]
{% endhighlight %}

## Examples
{% highlight bash %}
searchResultsTable.table.currentRow.checkValue = [country : 'New Zealand'] 
searchResultsTable.table.currentRow.checkValue = [country : 'New Zealand', province : 'Otago']
searchResultsTable.table.currentRow.checkValue = ['column3' : 'New Zealand']
searchResultsTable.table.currentRow.checkValue = ['firstColumn' : 'Unit A']
searchResultsTable.table.currentRow.checkValue = ['lastColumn' : 'Earth']
{% endhighlight %}

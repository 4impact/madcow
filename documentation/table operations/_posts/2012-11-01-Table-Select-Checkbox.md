---
layout: postTableOperation
title: Table Select Checkbox
description: Selects/Clicks a checkbox within a specific column on the current selected row in an HTML table.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlTableReference].table.currentRow.selectCheckbox = [columnName]
{% endhighlight %}

## Examples
{% highlight bash %}
searchResultsTable.table.currentRow.selectCheckbox = 'Select All'
searchResultsTable.table.currentRow.selectCheckbox = firstColumn
searchResultsTable.table.currentRow.selectCheckbox = lastColumn
searchResultsTable.table.currentRow.selectCheckbox = column1
{% endhighlight %}

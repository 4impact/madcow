---
layout: postTableOperation
title: Table Verify Select Field Contains
description: Verify the select field in the table which contains in the list values.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlTableReference].table.currentRow.verifySelectFieldContains = [columnName : [columnValue1, columnValue2, columnValue3]]
{% endhighlight %}

## Examples
<table name="aTable"><tr><td>
<select id="aSelectId" name="aSelectName" class="aSelectClass" style="display: block;border-bottom-width: 2px">
        <option value="AUD">Australia</option>
        <option value="NZD">New Zealand</option>
        <option value="USD">United States</option>
</select>
</td></tr></table>
{% highlight bash %}
aTable.table.currentRowverifySelectFieldContains = ['Australia']
{% endhighlight %}




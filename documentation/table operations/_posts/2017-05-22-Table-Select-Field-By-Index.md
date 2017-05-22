---
layout: postTableOperation
title: Table Select Field By Index
description: Sets a value on a combo box element based on the 0-th based index.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].table.currentRow.selectFieldByIndex = <<text value of index>>
{% endhighlight %}

## Examples

{% highlight bash %}
cell-editing-1025.table.selectRow = first
cell-editing-1025.table.currentRow.selectFieldByIndex = ['column2' : '1']
{% endhighlight %}

{% highlight bash %}
policyCenter_pet_policyInfo_convictionTable.gw.table.selectRow = row2
policyCenter_pet_policyInfo_convictionTable.table.currentRow.selectFieldByIndex = ['column4':'7']
{% endhighlight %}
---
layout: postTableOperation
title: Table Upload File
description: It sets a value on a file input table element on the page.
category: table operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].table.currentRow.uploadFile = <<text file value>>
{% endhighlight %}

## Examples

{% highlight bash %}
cell-editing-upload.table.selectRow = first
cell-editing-upload.table.currentRow.uploadFile = ['Description': 'US 221 Assessment Spreadsheet mockup.xlsm
{% endhighlight %}



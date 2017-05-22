---
layout: postBasicOperation
title: Upload File
description: It sets a value on a file input element on the page.
category: basic operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlElementReference].uploadFile = <<text file value>>
{% endhighlight %}

## Examples
<input id="fileuploadfield-1867-button-fileInputEl" class=" x-form-file-input" type="file" size="1" name="fileContent" role="button"></a>

{% highlight bash %}
fileContent.uploadFile = US 221 Assessment Spreadsheet mockup.xlsm
{% endhighlight %}

{% highlight bash %}
fileuploadfield-1867-button-fileInputEl.uploadFile = US 221 Assessment Spreadsheet mockup.xlsm
{% endhighlight %}



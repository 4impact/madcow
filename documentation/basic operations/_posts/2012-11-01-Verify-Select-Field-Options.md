---
layout: postBasicOperation
title: Verify Select Field Options
description: Verify the select field contains all of the given options, and no others.
category: basic operations
---

## Usage

{% highlight bash %}
[MADCOW:htmlElementReference].verifySelectFieldOptions = <<[List of options]>>
{% endhighlight %}

## Examples

{% highlight bash %}
countryList.verifySelectFieldOptions = ["Australia", "New Zealand", "Japan"]
{% endhighlight %}



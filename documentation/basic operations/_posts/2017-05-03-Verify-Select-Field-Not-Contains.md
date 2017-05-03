---
layout: postBasicOperation
title: Verify Select Field Not Contains
description: Verify the select field which not contains in the list values.
category: basic operations
---

## Usage

{% highlight bash %}
[MADCOW:htmlElementReference].verifySelectFieldNotContains = <<[List of values]>>
{% endhighlight %}

## Examples

{% highlight bash %}
countryList.verifySelectFieldNotContains = ["UK", "Wales"]
{% endhighlight %}



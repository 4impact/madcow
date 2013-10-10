---
layout: postBasicOperation
title: Select Field
description: Selects a given text value field in an HTML select or multi-select element.
category: basic operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlSelectReference].selectField = <<field value/s>>
{% endhighlight %}

## Examples
{% highlight bash %}
countryList.selectField = "New Zealand"
{% endhighlight %}
{% highlight bash %}
carMakes.selectField = ["Vovlo","Saab","Ferrari"]
{% endhighlight %}



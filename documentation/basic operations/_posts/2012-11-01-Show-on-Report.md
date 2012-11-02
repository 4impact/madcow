---
layout: postBasicOperation
title: Show on Report
description: Shows the specified element text on the overview report.
category: basic operations
---

## Usage
{% highlight bash %}
[MADCOW:htmlElement].showOnReport = [MADCOW:name of reported]
{% endhighlight %}

## Examples
{% highlight bash %}
country.showOnReport = "SelectedCountry"
{% endhighlight %}

## Advanced

Show on Report also supports a format string to use when displaying the value on the report.

When displayed, it will replace all occurances of the 'value' name with the text from the element on the page.

{% highlight bash %}
showOnReport = [xpath: "//*[@id='addressId']",
                value: 'CreatedAddressNumber',
                valueFormatString: '&lt;a href="http://test-site/address/show/CreatedAddressNumber"&gt;View CreatedAddressNumber</a>']
{% endhighlight %}



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
country.showOnReport = "Selected Country"
{% endhighlight %}

## Advanced

Show on Report also supports a format string to use when displaying the value on the report.

When displayed, it will replace all occurances of the '%s' with the name parameter.

{% highlight bash %}
country.showOnReport = [name: 'Selected Country', format: '<b>%s is a nice country</b>']
{% endhighlight %}



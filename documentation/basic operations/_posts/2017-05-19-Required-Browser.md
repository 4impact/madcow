---
layout: postBasicOperation
title: Required Browser
description: Allow selection of browser to use per test case.
category: basic operations
---

## Usage
{% highlight bash %}
requiredBrowser  = <<text value of browser type>>
{% endhighlight %}

Currently Madcow 2.0 only supports these values:
*   HtmlUnit
*   Firefox
*   Remote
*   Chrome
*   PhantomJs
*   IE (**untested**)

See configuration for browser versions

## Examples

{% highlight bash %}
requiredBrowser = "HtmlUnit"
{% endhighlight %}



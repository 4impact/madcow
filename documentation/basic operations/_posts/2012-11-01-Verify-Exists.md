---
layout: postBasicOperation
title: Verify Exists
description: Checks that the element is currently found on the page for the user to interact with.
category: basic operations
---

Checks that the element is currently found on the page for the user to interact with.
Note that if the element is actually on the page but hidden (e.g. style="display:none;") this operation will fail.

## Usage

{% highlight bash %}
[MADCOW:htmlElementReference].verifyExists
{% endhighlight %}

## Examples

{% highlight bash %}
country.verifyExists
google_searchBox.verifyExists
{% endhighlight %}


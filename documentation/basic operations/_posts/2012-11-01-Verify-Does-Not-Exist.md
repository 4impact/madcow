---
layout: postBasicOperation
title: Verify Does Not Exist
description: Checks that the element is currently NOT found on the page for the user to interact with.
category: basic operations
---

Checks that the element is currently NOT found on the page for the user to interact with.
Note that if the element is actually on the page but hidden (e.g. style="display:none;") this operation will pass.

## Usage

{% highlight bash %}
[MADCOW:htmlElementReference].verifyDoesNotExist
{% endhighlight %}

## Examples

{% highlight bash %}
country.verifyDoesNotExist
google_searchBox.verifyDoesNotExist
{% endhighlight %}


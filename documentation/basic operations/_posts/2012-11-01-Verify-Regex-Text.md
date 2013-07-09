---
layout: postBasicOperation
title: Verify Regex Text
description: This operation verifies that text matching the given regular expression is on the page (or in the specified element)
category: basic operations
---

## Usage
{% highlight bash %}
verifyRegexText = "<<Regular expression to match on page>>"
[MADCOW:htmlElementReference].verifyRegexText = "<<Regular expression to match text in element>>"
{% endhighlight %}

## Examples
{% highlight bash %}
verifyRegexText = Consultant id [\d]* Created.
someTextInput.verifyRegexText = .*Test (Street|Avenue).*
{% endhighlight %}



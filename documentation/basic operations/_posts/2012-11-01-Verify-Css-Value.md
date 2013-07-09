---
layout: postBasicOperation
title: VerifyCssValue
description: Checks that the element has all of the CSS styling provided
category: basic operations
---

Checks that the element has all of the CSS styling provided.

## Usage
{% highlight bash %}
[MADCOW:htmlElement].verifyCssValue = <<[The css values expected]>>
{% endhighlight %}

## Examples
{% highlight bash %}
country.verifyCssValue = "display: block;"
country.verifyCssValue = "display:inline; font-weight:700;"
{% endhighlight %}

## Advanced

Verify CSS Value also supports a map of CSS styling values to check for.
It will pass only if all of the provided styles are found.

For example

{% highlight bash %}
country.verifyCssValue = ["display":"inline", "font-weight": "700"]
{% endhighlight %}

Finally also note that for color style testing you will need to use the RGB values e.g. rgb(51, 51, 51)



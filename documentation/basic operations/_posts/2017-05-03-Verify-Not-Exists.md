---
layout: postBasicOperation
title: Verify Not Exists
description: Checks that the element is currently NOT found on the page for the user to interact with.
category: basic operations
---

Checks that the element is currently NOT found on the page for the user to interact with.
Note that if the element is actually on the page but hidden (e.g. style="display:none;") this operation will pass.

## Usage

{% highlight bash %}
[MADCOW:htmlElementReference].verifyNotExists
{% endhighlight %}

## Examples

{% highlight bash %}
claimCenter_common_admin_holidays_editBTN.verifyNotExists
{% endhighlight %}
{% highlight bash %}
claimCenter_ctp_02PostLodgement_initiateClaim_dateOfDeath.verifyNotExists
{% endhighlight %}
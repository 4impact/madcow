---
layout: post
title: Madcow Best Practices
description: The best way to do things to save yourself pain
---

## Re-use test steps using templates rather than copy-and-paste

Rather than copying sections of Grass scripts between test scripts, use data parameters, default parameters  and templates to generalise the steps for re-use. Although this increases the complexity of writing a single script it has a number of advantages:

* If the "shared" page changes you only need to fix the shared template and not all the tests.
* Hide unimportant steps from the test making it easier to see what the test is doing.
* Save time when writing your next script.
* Give sections in your test meaningful names instead of Grass steps. e.g.

{% highlight bash %}
importTemplate  = Login
....
{% endhighlight %}

## Set Default Values for all Data Parameters Used in Templates

In order to maximise the flexibility of templates, we usually use data parameters to specify the values for each operation used in the template. In order to prevent having to specify values for parameters that we don't care about, it's advisable to set default values for all parameters used in a given template. That way if a value hasn't been specified for the data parameter, the template will still function correctly.

The following example demonstrates setting default values for a template that will select a drivers name. If the calling test doesn't specify a value for ``@firstname`` or ``@lastname``, then the driver name will be set to ``Joe Schmoe``.

{% highlight bash %}
#-----------------------------#
# Select Driver Name Template #
#-----------------------------#

#Set defaults for the drivers name
@firstname.default = Joe
@lastname.default = Schmoee

#Navigate to the page
goToDriverPage.clickLink

#Set the drivers name
driverFirstName.value = @firstname
driverLastName.value = @lastname
{% endhighlight %}

## Use a version control system to store tests

Version Control Systems (VCS) like Subversion (SVN) or Git are useful additions to your test development. VCSs give you the ability to retrieve old versions of your tests, share tests with other testers, tag revisions of tests and, branch and merge tests.

Subversion is probably to simplest open-source VCS to learn. For more information see http://subversion.apache.org/

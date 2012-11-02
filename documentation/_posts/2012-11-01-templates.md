---
layout: post
title: Templates
description: Resuable steps that can be imported and overridden - unleash the power!
---

Madcow 2.0 provides the ability to define a set of templates, which can be imported into a test. This allows for a common set of test functions to be defined and reused throughout multiple tests.

## Defining a Template

Templates are properties files defined *outside* of the default Madcow 2.0 `test` folder - by default, under `templates`.
They support the same syntax and operations as a normal Madcow 2.0 `GRASS` file test, with the main difference being that are not run as part of the collection of tests.

{% highlight bash %}
CountryTemplate.grass:

searchResults.table.selectRow = ['Country' : 'New Zealand']
searchResults.table.currentRow.checkValue = ['Country' : 'New Zealand', 'lastColumn' : 'NZD']
{% endhighlight %}

## Using a Template

A template is included into a Test file, using the Madcow syntax of `import`.
This will put all the contents of the specified template into the test.

To import a template named _CountryTemplate_:

{% highlight bash %}
testInfo = Test various countries
import = CountryTemplate
{% endhighlight %}

When executed, this will expand to
{% highlight bash %}
testInfo = Test various countries

searchResults.table.selectRow = ['Country' : 'New Zealand']
searchResults.table.currentRow.checkValue = ['Country' : 'New Zealand', 'lastColumn' : 'NZD']
{% endhighlight %}

> When importing a template, exclude the _.grass_ file extension!

## Data Parameters and Templates

As templates support all Madcow operations, they support the use of Data Parameters.
This enables the user to create generic functionality, which can be reused by altering the data passed to the template from a test.

Building on the examples previously, we could pass through the currency and country being searched for in the template from our test.

### Country Template
{% highlight bash %}
searchResults.table.selectRow = ['Country' : @selectedCountry]
searchResults.table.currentRow.checkValue = ['Country' : '@selectedCountry', 'lastColumn' : '@selectedCountryCurrency']
{% endhighlight %}

### CountryTest
{% highlight bash %}
testInfo = Test various countries

@selectedCountry = Australia
@selectedCountryCurrency = AUD
import = CountryTemplate

@selectedCountry = New Zealand
@selectedCountryCurrency = NZD
import = CountryTemplate

@selectedCountry = United States of America
@selectedCountryCurrency = USD
import = CountryTemplate
{% endhighlight %}

### Expanded version of CountryTest
{% highlight bash %}
testInfo = Test various countries

searchResults.table.selectRow = ['Country' : 'Australia']
searchResults.table.currentRow.checkValue = ['Country' : 'Australia', 'lastColumn' : 'AUD']

searchResults.table.selectRow = ['Country' : 'New Zealand']
searchResults.table.currentRow.checkValue = ['Country' : 'New Zealand', 'lastColumn' : 'NZD']

searchResults.table.selectRow = ['Country' : 'United States of America']
searchResults.table.currentRow.checkValue = ['Country' : 'United States of America', 'lastColumn' : 'USD']
{% endhighlight %}


## Default Values for Parameters in Templates

In order to maxamise the flexibility of templates, we can define a default value for any of the parameters used in the template. This allows us to only override the parameters that we want to change.

You can define a default value for a parmeter like so: `{parameterName}.default={parameterValue}`. The following example illustrates in more detail.

### SearchAddressTemplate
{% highlight bash %}
@addressLine.default = 55 Shemp Street
@postCode.default = 4000
@state.default = Queensland

addressLineField.value = @addressLine
postCodeField.value = @postCode
stateField.selectField = @state

searchButton.clickButton
{% endhighlight %}

### SearchTest
{% highlight bash %}
testInfo = Search for an address

@addressLine = 124 Queen Street
import = SearchAddressTemplate
{% endhighlight %}

### Expanded version of SearchTest
{% highlight bash %}
testInfo = Search for an address

addressLineField.value = 124 Queen Street
postCodeField.value = 4000
stateField.selectField = Queensland

searchButton.clickButton
{% endhighlight %}
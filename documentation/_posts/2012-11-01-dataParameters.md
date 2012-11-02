---
layout: post
title: Data Parameters
description: Provides variable data storage
---

Madcow provides an intuitive method for providing data parameters to Madcow tests.

* must be set before they are used
* can be overridden
* names must be unique with a test
* can be used to pass data to Madcow templates

## Data Parameters

### Define a Data Parameter

To define a data parameter in a Madcow test we use the `@` symbol followed by the name you give the data parameter.

{% highlight bash %}
@myFirstName=Madcow
{% endhighlight %}

### Use a Defined Data Parameter

To use a data parameter in a Madcow test we again use the `@` symbol followed by the name of the data parameter you require.

{% highlight bash %}
firstNameField.value = @myFirstName
{% endhighlight %}

## Runtime Data

Madcow provides the ability to store the value of a web page element during test execution and then to use the stored value within the Madcow test.

### Define a Runtime Data Parameter

To define a runtime data parameter in a Madcow test we use the Madcow `store` operation on an web page element passing it the name of the data parameter to store.

{% highlight bash %}
generatedReferenceNumber.store = MyReferenceNumber
{% endhighlight %}

### Use a Runtime Data Parameter

To use a runtime data parameter in a Madcow test we again use the `@` symbol followed by the name of the stored data parameter.

{% highlight bash %}
searchField.value = @MyReferenceNumber
{% endhighlight %}

## Global Data Parameters

Madcow provides the following common global data parameters.


<table>
<tr><td>@global.currentDate</td><td>dd/MM/yyyy</td></tr>
<tr><td>@global.currentTime12hr</td><td>hh:mm a</td></tr>
<tr><td>@global.currentTime24hr</td><td>hh:mm:ss</td></tr>
<tr><td>@global.currentDateTime12hr</td><td>dd/MM/yyyy hh:mm a</td></tr>
<tr><td>@global.currentDateTime24hr</td><td>dd/MM/yyyy hh:mm:ss</td></tr>
</table>

### Use a Global Data Parameter

To use a global data parameter in a Madcow test we again use the `@` symbol followed by the name of the global data parameter.

{% highlight bash %}
date.value = @global.currentDate
time.value = @global.currentTime24hr
{% endhighlight %}

## Advanced Data Parameter Usage

Data Parameters can be used within a list and with the Madcow table syntax.

{% highlight bash %}
@newZealand = NZD
@australian = AUD
@japanese = JPY
@selectDollarFieldValues = [@newZealand, @australian, @japanese]
dollarList.verifySelectFieldOptions = @selectDollarFieldValues
{% endhighlight %}

{% highlight bash %}
@selectedCountry = New Zealand
@selectedCountryCurrency = NZD
@selectRowCriteria = ['Country' : @selectedCountry]
@searchResultsCheckValues = ['Country' : '@selectedCountry', 'lastColumn' : '@selectedCountryCurrency']
searchResults.table.selectRow = @selectRowCriteria
searchResults.table.currentRow.checkValue = @searchResultsCheckValues
{% endhighlight %}

## Embedded Inline Data Parameters

Data Parameters can be used within a Madcow Value. This allows partial substitution of the value with the data parameter.
This is done through `@{...}` syntax, with the contents within the curly braces defining the name of the data parameter.

{% highlight bash %}
@expectedResultCount = 20
verifyText = There are @{expectedResultCount} countries found
{% endhighlight %}
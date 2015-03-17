# Madcow 2.0  [![Build Status](https://travis-ci.org/4impact/madcow.png)](https://travis-ci.org/4impact/madcow) [![Project Status](http://stillmaintained.com/4impact/madcow.png)](https://stillmaintained.com/4impact/madcow)

Check out the awesome documentation at http://madcow.4impact.net.au now and get testing!

## Migration Guide from v1.x

### Configuration changes

TODO - Lots!

### Filename changes

All files are now of type `grass`. Be sure to rename any `*.madcow.mappings.properties` to `*.grass`.

### Operation Changes

* `import` is now `importTemplate`
* `checkSelectValue` is now `verifySelectFieldOptions`
* `waitUntilExists` is now `waitFor`
* `setRadioButton` has been removed - setting radio buttons can be accomplished using `click`
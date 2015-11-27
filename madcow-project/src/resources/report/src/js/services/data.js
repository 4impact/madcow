(function($, window) {
  'use strict';

  function DataService() {
    this.setup();
  }

  DataService.prototype = {

    setup: function() {
      this.rootSuite = window.Madcow ? window.Madcow.Results : null;

      // sanitize the results for display
      this.applyAgainstEachTestCase = $.proxy(this.applyAgainstEachTestCase, this);
      this.applyAgainstEachTestCase(function(testCase) {
        if (testCase.reportDetails && testCase.reportDetails['Test Summary']) {
          testCase.testInfo = testCase.reportDetails['Test Summary'].split('\n');
          delete testCase.reportDetails['Test Summary'];
        }

        return testCase;
      });
    },

    applyAgainstEachTestCase: function(func) {

      function applyRecursively(inSuite) {

        // apply to test cases
        if (inSuite.testCases && inSuite.testCases.length > 0) {
          for (var testCaseIdx = 0; testCaseIdx < inSuite.testCases.length; ++testCaseIdx) {
            var testCase = inSuite.testCases[testCaseIdx];
            inSuite.testCases[testCaseIdx] = func(testCase);
          }
        }

        // search sub suites
        if (inSuite.suites && inSuite.suites.length > 0) {
          for (var suiteIdx = 0; suiteIdx < inSuite.suites.length; ++suiteIdx) {
            var suite = inSuite.suites[suiteIdx];
            applyRecursively(suite);
          }
        }
      }

      if (this.rootSuite) {
        applyRecursively(this.rootSuite);
      }
    },

    /**
     * Find a test case by the given name, starting at the provided suite.
     * If the suite is null, the root madcow result suite will be used.
     */
    findTestCase: function(name, inSuite) {

      if (!inSuite) {
        inSuite = this.rootSuite;
      }

      // search test cases
      if (inSuite.testCases && inSuite.testCases.length > 0) {
        for (var testCaseIdx = 0; testCaseIdx < inSuite.testCases.length; ++testCaseIdx) {
          var testCase = inSuite.testCases[testCaseIdx];
          if (testCase.name && testCase.name === name) {
            return testCase;
          }
        }
      }

      // search sub suites
      if (inSuite.suites && inSuite.suites.length > 0) {
        var self = this;
        for (var suiteIdx = 0; suiteIdx < inSuite.suites.length; ++suiteIdx) {
          var suite = inSuite.suites[suiteIdx];
          var found = self.findTestCase(name, suite);
          if (found) {
            return found;
          }
        }
      }

      return null;
    }
  };

  window.DataService = new DataService();

})(jQuery, window);
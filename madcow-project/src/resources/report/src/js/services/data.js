(function($, window, moment) {
  'use strict';

  function DataService() {
    this.setup();
  }

  DataService.prototype = {

    setup: function() {

      if (!window.Madcow) {
        return;
      }

      var madcowResults =  window.Madcow.Results;

      this.rootSuite = madcowResults.results;

      var totalTests = madcowResults.passedCount + madcowResults.errorCount + madcowResults.failedCount + madcowResults.skippedCount;
      this.overview = {
        total: totalTests,
        passed: madcowResults.passedCount,
        passedPercentage: Math.round(madcowResults.passedCount / totalTests * 100),
        error: madcowResults.errorCount,
        errorPercentage: Math.round(madcowResults.errorCount / totalTests * 100),
        failed: madcowResults.failedCount,
        failedPercentage: Math.round(madcowResults.failedCount / totalTests * 100),
        skipped: madcowResults.skippedCount,
        skippedPercentage: Math.round(madcowResults.skippedCount / totalTests * 100),
        timestamp: new Date(madcowResults.timestamp),
        timestampFormatted: madcowResults.timestampFormatted,
        totalTime: moment.duration(parseInt(madcowResults.totalTime), 'seconds').humanize(),
        madcowVersion: madcowResults.madcowVersion
      };

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

})(jQuery, window, window.moment);
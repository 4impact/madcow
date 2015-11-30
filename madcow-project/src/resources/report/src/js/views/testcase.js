(function($, window, dataService) {
  'use strict';

  function TestCaseView() {
    this.setup();
  }

  TestCaseView.prototype = {

    setup: function() {
      // nothing... yet
    },

    render: function(testCaseName) {
      this.testCase = dataService.findTestCase(testCaseName);

      $('.sidebar').html(JST.sidebar({}));
      $('.content').html(JST['testcase/testcase']({
        testCase: this.testCase
      }));

      window.document.title = 'Madcow Results: ' + testCaseName;
    }
  };

  window.TestCaseView = new TestCaseView();

})(jQuery, window, window.DataService);
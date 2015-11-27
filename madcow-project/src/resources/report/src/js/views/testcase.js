(function($, window, dataService) {
  'use strict';

  function TestCaseView() {
    this.setup();
  }

  TestCaseView.prototype = {

    setup: function () {

      this.testCase = dataService.findTestCase('search.SearchAddressTest');

      $('.sidebar').html(JST.sidebar({}));
      $('.content').html(JST['testcase/testcase']({
        testCase: this.testCase
      }));

      this.refresh = $.proxy(this.refresh, this);
      this.refresh();
    },

    refresh: function() {
      //var testCase = dataService.findTestCase('AddressTest');
      console.log(this.testCase);
    }
  };

  window.TestCaseView = TestCaseView;

})(jQuery, window, window.DataService);
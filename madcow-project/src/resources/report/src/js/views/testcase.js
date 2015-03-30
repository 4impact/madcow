(function($, window) {
  'use strict';

  function TestCaseView() {
    this.setup();
  }

  TestCaseView.prototype = {

    setup: function () {

      $('.sidebar').html(JST.sidebar({}));
      $('.content').html(JST.testcase({}));

      this.refresh = $.proxy(this.refresh, this);
      this.refresh();
    },

    refresh: function() {
      //
    }
  };

  window.TestCaseView = TestCaseView;

})(jQuery, window);
(function($, window) {
  'use strict';

  function SuiteView() {
    this.setup();
  }

  SuiteView.prototype = {

    setup: function () {
      // nothing... yet
    },

    render: function(suite) {
      $('.sidebar').html(JST.sidebar({}));
      $('.content').html(JST['suite/suite']({
        suite: suite
      }));
    }
  };

  window.SuiteView = new SuiteView();

})(jQuery, window);
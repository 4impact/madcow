(function($, window, dataService, routie) {
  'use strict';

  function MainView() {
    this.setup();
  }

  MainView.prototype = {

    setup: function () {

      $('.sidebar').html(JST.sidebar({}));

      routie('', function() {
        window.SuiteView.render(dataService.rootSuite);
      });

      routie('testcase/:name', function(name) {
        window.TestCaseView.render(name);
      });
    }
  };

  window.MainView = MainView;

})(jQuery, window, window.DataService, window.routie);
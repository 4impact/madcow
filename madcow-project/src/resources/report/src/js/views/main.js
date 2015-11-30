(function($, window, dataService, routie) {
  'use strict';

  function MainView() {
    this.setup();
  }

  MainView.prototype = {

    setup: function () {

      window.document.title = 'Madcow Results';

      routie('', function() {
        window.SuiteView.render(dataService.rootSuite);
        window.SidebarView.render('suite');
      });

      routie('testcase/:name', function(name) {
        window.TestCaseView.render(name);
        window.SidebarView.render('testcase');
      });

      routie('mappings', function() {
        window.MappingsView.render();
        window.SidebarView.render('mappings');
      });

      routie('settings', function() {
        window.SettingsView.render();
        window.SidebarView.render('settings');
      });
    }
  };

  window.MainView = MainView;

})(jQuery, window, window.DataService, window.routie);
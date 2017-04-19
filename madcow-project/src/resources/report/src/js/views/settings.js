(function($, window, dataService) {
  'use strict';

  function SettingsView() {
    this.setup();
  }

  SettingsView.prototype = {

    setup: function () {
      // nothing... yet
    },

    render: function() {
      $('.sidebar').html(JST.sidebar({}));
      $('.content').html(JST['settings/settings']({
        config: dataService.config,
        overview: dataService.overview
      }));

      window.document.title = 'Madcow Settings';
    }
  };

  window.SettingsView = new SettingsView();

})(jQuery, window, window.DataService);
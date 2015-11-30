(function($, window) {
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
      $('.content').html(JST['settings/settings']({}));

      window.document.title = 'Madcow Settings';
    }
  };

  window.SettingsView = new SettingsView();

})(jQuery, window);
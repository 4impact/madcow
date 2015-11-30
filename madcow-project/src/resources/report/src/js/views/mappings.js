(function($, window) {
  'use strict';

  function MappingsView() {
    this.setup();
  }

  MappingsView.prototype = {

    setup: function () {
      // nothing... yet
    },

    render: function() {
      $('.sidebar').html(JST.sidebar({}));
      $('.content').html(JST['mappings/mappings']({}));

      window.document.title = 'Madcow Mappings';
    }
  };

  window.MappingsView = new MappingsView();

})(jQuery, window);
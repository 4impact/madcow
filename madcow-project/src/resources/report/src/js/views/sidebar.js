(function($, window) {
  'use strict';

  function SidebarView() {
    this.setup();
  }

  SidebarView.prototype = {

    setup: function() {

    },

    render: function(routeName) {

      var settings = {
        suite: (!routeName || routeName === '') || (routeName === 'suite') || (routeName === 'testcase'),
        mappings: (routeName === 'mappings'),
        settings: (routeName === 'settings')
      };

      $('.sidebar').html(JST.sidebar(settings));
    }
  };

  window.SidebarView = new SidebarView();

})(jQuery, window);
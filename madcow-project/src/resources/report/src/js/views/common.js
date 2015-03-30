(function($, window) {
  'use strict';

  function CommonView() {
    this.setup();
  }

  CommonView.prototype = {

    setup: function () {

      // push setup onto the end of the js queue to ensure all hbs templates have been rendered
      setTimeout($.proxy(function() {

        $('.say-hello').hello();

      }, this), 1);
    }
  };

  window.CommonView = new CommonView();

})(jQuery, window);
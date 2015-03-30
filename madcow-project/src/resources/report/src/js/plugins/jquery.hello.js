(function($, window) {
  'use strict';

  var pluginName = 'hello';

  function Hello(el) {
    this.$el = $(el);
    this.setup();
  }

  Hello.prototype = {

    setup: function() {
      this.$el.off('click.hello').on('click.hello', $.proxy(function() {
        console.log('Hello!');
      }, this));
    }
  };

  /**
   * Plugin wrapper to ensure 1 instance per element.
   */
  $.fn.hello = function() {

    var instance = null;
    this.each(function() {
      instance = $.data(this, 'plugin_' + pluginName);
      if (!instance) {
        instance = new Hello(this);
        $.data(this, 'plugin_' + pluginName, instance);
      } else {
        instance.setup();
      }
    });

    return instance;
  };

  window.Hello = Hello;

})(jQuery, window);
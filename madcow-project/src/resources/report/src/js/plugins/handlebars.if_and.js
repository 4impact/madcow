(function(Handlebars) {
  Handlebars.registerHelper('if_and', function(a, b, opts) {
    if (a && b)
      return opts.fn(this);
    else
      return opts.inverse(this);
  });

  Handlebars.registerHelper('if_or', function(a, b, opts) {
    if (a || b)
      return opts.fn(this);
    else
      return opts.inverse(this);
  });

})(window.Handlebars);
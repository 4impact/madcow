(function(Handlebars) {
  Handlebars.registerHelper('if_eq', function(a, b, opts) {
    if (a === b)
      return opts.fn(this);
    else
      return opts.inverse(this);
  });

  Handlebars.registerHelper('if_noteq', function(a, b, opts) {
    if (a !== b)
      return opts.fn(this);
    else
      return opts.inverse(this);
  });

  Handlebars.registerHelper('if_object', function(a, opts) {
    if (typeof(a) === 'object')
      return opts.fn(this);
    else
      return opts.inverse(this);
  });
})(window.Handlebars);
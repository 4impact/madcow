(function(Handlebars, S) {

  Handlebars.registerHelper('humanize', function(text) {
    return new Handlebars.SafeString(S(text).humanize());
  });

  Handlebars.registerHelper('capitalize', function(text) {
    text = text.toString();
    text = text.replace(/(?:^|\s)\S/g, function(a) { return a.toUpperCase(); });
    return new Handlebars.SafeString(text);
  });

})(window.Handlebars, window.S);
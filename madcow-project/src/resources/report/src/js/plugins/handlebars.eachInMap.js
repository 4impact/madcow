(function(Handlebars) {
  Handlebars.registerHelper('eachInMap', function (map, block) {
    var out = '';

    var keys = Object.keys(map);
    keys.sort(function (a, b) {
      if (a < b) return -1;
      if (b < a) return 1;
      return 0;
    });

    keys.map(function (prop) {
      out += block.fn({key: prop, value: map[prop]});
    });

    return out;
  });

})(window.Handlebars);
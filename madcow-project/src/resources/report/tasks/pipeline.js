module.exports.banner = ['/*!',
                         ' * <%= pkg.name %> - v<%= pkg.version %>',
                         ' * <%= grunt.template.today("yyyy-mm-dd") %>',
                         ' */\n\n'
                         ].join('\n');

module.exports.javascriptFiles = [
  'components/underscore/underscore-min.js',
  'components/jquery/dist/jquery.min.js',
  'components/handlebars/handlebars.runtime.min.js',
  'components/routie/dist/routie.min.js',
  'components/Chart.js/Chart.min.js',
  'components/moment/min/moment.min.js',
  'components/string/dist/string.js',

  'dist/assets/js/templates.js',

  'src/js/plugins/*.js',
  'src/js/services/*.js',
  'src/js/views/*.js'
];
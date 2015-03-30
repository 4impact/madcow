module.exports = function(grunt) {

  grunt.config.set('handlebars', {
    compile: {
      options: {
        namespace: 'JST',
        processName: function(filePath) {
          var pathToDropLength = 'src/templates/'.length;
          return filePath.substr(pathToDropLength, filePath.indexOf('.hbs') - pathToDropLength).toLowerCase();
        }
      },
      files: {
        'dist/js/templates.js': 'src/templates/**/*.hbs'
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-handlebars');
};

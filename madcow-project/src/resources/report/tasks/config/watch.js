module.exports = function(grunt) {

  grunt.config.set('watch', {
    html: {
      files: ['src/**/*.html'],
      tasks: ['clean:html', 'copy:html'],
      options: {
        livereload: true
      }
    },
    templates: {
      files: ['src/templates/**/*.hbs'],
      tasks: ['clean:js', 'handlebars', 'concat', 'uglify'],
      options: {
        livereload: true
      }
    },
    less: {
      files: ['src/less/**/*'],
      tasks: ['clean:css', 'less', 'cssmin'],
      options: {
        livereload: true
      }
    },
    js: {
      files: ['src/js/**/*'],
      tasks: ['lint', 'test', 'clean:js', 'handlebars', 'concat', 'uglify'],
      options: {
        livereload: true
      }
    },
    assets: {
      files: ['src/assets/**/*'],
      tasks: ['clean:assets', 'copy:assets'],
      options: {
        livereload: true
      }
    }
  });

  grunt.loadNpmTasks('grunt-contrib-watch');
};

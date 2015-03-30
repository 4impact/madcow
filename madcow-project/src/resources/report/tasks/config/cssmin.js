module.exports = function(grunt) {

  grunt.config.set('cssmin', {
    app: {
      src: ['dist/css/app.css'],
      dest: 'dist/css/app.min.css'
    }
  });

  grunt.loadNpmTasks('grunt-contrib-cssmin');
};

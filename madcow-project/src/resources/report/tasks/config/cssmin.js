module.exports = function(grunt) {

  grunt.config.set('cssmin', {
    app: {
      src: ['dist/assets/css/app.css'],
      dest: 'dist/assets/css/app.min.css'
    }
  });

  grunt.loadNpmTasks('grunt-contrib-cssmin');
};

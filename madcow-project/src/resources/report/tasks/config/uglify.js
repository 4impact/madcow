module.exports = function (grunt) {

  grunt.config.set('uglify', {
    options: {
      banner: require('../pipeline').banner
    },
    dist: {
      src: ['dist/js/app.js'],
      dest: 'dist/js/app.min.js'
    }
  });

  grunt.loadNpmTasks('grunt-contrib-uglify');
};

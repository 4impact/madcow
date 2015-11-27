module.exports = function (grunt) {

  grunt.config.set('uglify', {
    options: {
      banner: require('../pipeline').banner
    },
    dist: {
      src: ['dist/assets/js/app.js'],
      dest: 'dist/assets/js/app.min.js'
    }
  });

  grunt.loadNpmTasks('grunt-contrib-uglify');
};

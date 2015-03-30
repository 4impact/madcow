module.exports = function(grunt) {

  grunt.config.set('karma', {
    default: {
      options: {
        configFile: 'test/karma.conf.js'
      }
    },
    run: {
      options: {
        configFile: 'test/karma.conf.js',
        singleRun: true
      }
    }
  });

  grunt.loadNpmTasks('grunt-karma');
};

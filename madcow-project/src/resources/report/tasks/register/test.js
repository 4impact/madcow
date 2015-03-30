module.exports = function (grunt) {
  grunt.registerTask('test', [
    'karma:run'
  ]);

  grunt.registerTask('test-unit', [
    'karma:run'
  ]);
};

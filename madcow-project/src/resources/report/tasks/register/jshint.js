module.exports = function (grunt) {
  grunt.registerTask('lint', [
    'jshint:lib', 'jshint:test'
  ]);

  grunt.registerTask('lint-lib', [
    'jshint:lib'
  ]);

  grunt.registerTask('lint-test', [
    'jshint:test'
  ]);
};

module.exports = function (grunt) {
  grunt.registerTask('build', [
    'lint', 'clean:dist', 'handlebars', 'test', 'concat', 'uglify', 'less', 'cssmin', 'copy'
  ]);
};

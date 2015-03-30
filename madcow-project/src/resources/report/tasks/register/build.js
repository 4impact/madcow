module.exports = function (grunt) {
  grunt.registerTask('build', [
    'lint', 'test', 'clean:dist', 'handlebars', 'concat', 'uglify', 'less', 'cssmin', 'copy'
  ]);
};

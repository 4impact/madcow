module.exports = function(grunt) {

  grunt.config.set('copy', {
    html: {
      files: [{
        expand: true,
        cwd: './src',
        src: ['**/*.html'],
        dest: './dist'
      }]
    },
    assets: {
      files: [{
        expand: true,
        cwd: './src/assets',
        src: ['**/*', '!**/*.psd'],
        dest: './dist/assets'
      }]
    }
  });

  grunt.loadNpmTasks('grunt-contrib-copy');
};

module.exports = function(grunt) {

	grunt.config.set('clean', {
    dist: ['dist'],
    css: ['dist/css'],
    js: ['dist/js'],
    html: ['dist/*.html'],
    assets: ['dist/assets']
	});

	grunt.loadNpmTasks('grunt-contrib-clean');
};

module.exports = function(grunt) {

	grunt.config.set('clean', {
    dist: ['dist'],
    css: ['dist/assets/css'],
    js: ['dist/assets/js'],
    html: ['dist/*.html'],
    assets: ['dist/assets'],
    results: ['dist/results']
	});

	grunt.loadNpmTasks('grunt-contrib-clean');
};

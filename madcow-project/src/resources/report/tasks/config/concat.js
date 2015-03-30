module.exports = function(grunt) {

	grunt.config.set('concat', {
    options: {
      banner: require('../pipeline').banner,
      stripBanners: true
    },
		js: {
			src: require('../pipeline').javascriptFiles,
			dest: 'dist/js/app.js'
		}
	});

	grunt.loadNpmTasks('grunt-contrib-concat');
};

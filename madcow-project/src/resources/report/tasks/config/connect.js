module.exports = function(grunt) {

	grunt.config.set('connect', {
    server: {
      options: {
        hostname: '*',
        port: 1337,
        base: './dist/',

        middleware: function (connect) {
          return [
            require('connect-livereload')(),

            // Serve the project folder
            connect.static('./dist/')
          ];
        }
      }
    }
	});

	grunt.loadNpmTasks('grunt-contrib-connect');
};

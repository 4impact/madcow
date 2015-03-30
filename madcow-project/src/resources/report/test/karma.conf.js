
var pipeline = require('../tasks/pipeline');

module.exports = function(config) {
  config.set({
    basePath: '../',

    frameworks: [ 'jasmine' ],

    plugins: [
      'karma-jasmine',
      'karma-coverage',
      'karma-phantomjs-launcher'
    ],

    browsers: [ 'PhantomJS' ],

    autoWatch: true,

    files: pipeline.javascriptFiles.concat([
      'test/unit/**/*.spec.js'
    ]),

    preprocessors: {
      'lib/**/*.js': ['coverage']
    },

    reporters: ['progress', 'coverage'],

    coverageReporter: {
      type : 'lcov',
      dir : 'test/reports/unit-test-coverage/'
    }
  });
};
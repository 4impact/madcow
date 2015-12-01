(function($, window, dataService, Chart) {
  'use strict';

  var passedColor = '#4CAF50';
  var failedColor = '#F44336';
  var errorColor = '#3f454d';
  var skippedColor = '#FFC107';

  function SuiteView() {
    this.setup();
  }

  SuiteView.prototype = {

    setup: function () {
      // nothing... yet
    },

    render: function(suite) {
      $('.sidebar').html(JST.sidebar({}));
      $('.content').html(JST['suite/suite']({
        overview: dataService.overview,
        suite: suite
      }));

      if (suite.name) {
        window.document.title = 'Madcow Results: ' + suite.name;
      } else {
        window.document.title = 'Madcow Results';
      }

      $('tr[data-link]').click(function() {
        window.location.href = $(this).data('link');
      });

      this.renderChart();
    },

    renderChart: function() {
      var pieData = [{
        value: dataService.overview.passed,
        color: passedColor,
        label: 'Passed'
        },
        {
          value: dataService.overview.error,
          color: errorColor,
          label: 'Error'
        },
        {
          value: dataService.overview.skipped,
          color: skippedColor,
          label: 'Skipped'
        },
        {
          value: dataService.overview.failed,
          color: failedColor,
          label: 'Failed'
        }];

      var pieOptions = {
        segmentShowStroke: true,
        segmentStrokeColor: '#fff',
        segmentStrokeWidth: 2,
        percentageInnerCutout: 50,
        animationSteps: 50,
        animationEasing: 'easeOutBounce',
        animateRotate: true,
        animateScale: true,
        responsive: false,
        maintainAspectRatio: true,
        tooltipTemplate: '<%if (label){%><%=label%>: <%}%><%= value %>'
      };

      var pieChartCanvas = $('.chart');
      new Chart(pieChartCanvas.get(0).getContext('2d')).Doughnut(pieData, pieOptions);
    }
  };

  window.SuiteView = new SuiteView();

})(jQuery, window, window.DataService, window.Chart);
(function(Handlebars) {

  Handlebars.registerHelper('stepKeyIcon', function(step) {

    if (step.result.status === 'FAIL') {
      return new Handlebars.SafeString(
             '<a href="#" class="btn linkless">' +
             '<i class="fa fa-times"></i>' +
             '</a>');
    }

    if (step.result.status === 'NOT_YET_EXECUTED') {
      return new Handlebars.SafeString('');
    }

    var icon = '<a href="#" class="btn linkless">';

    if (step.blade.operation === 'testInfo') {
      icon += '<i class="fa fa-sliders"></i>';
    } else if (step.blade.type === 'DATA_PARAMETER') {
      icon += '<i class="fa fa-database"></i>';
    } else {
      icon += '<i class="fa fa-check"></i>';
    }

    icon += '</a>';
    return new Handlebars.SafeString(icon);
  });

  Handlebars.registerHelper('niceStatus', function(status) {

    var translated;
    switch (status) {
      case 'PASS':
        translated = 'Passed';
        break;
      case 'FAIL':
        translated = 'Failed';
        break;
      case 'NOT_YET_EXECUTED':
        translated = 'Not Yet Executed';
        break;
      case 'NO_OPERATION':
        translated = 'Skipped';
        break;
      default:
        translated = status;
        break;
    }

    return new Handlebars.SafeString(translated);
  });

  Handlebars.registerHelper('formatSequenceNumber', function(step, parentStepSequenceNumber) {

    if (parentStepSequenceNumber) {
      return new Handlebars.SafeString(parentStepSequenceNumber + '.' + step.sequenceNumber);
    } else {
      return new Handlebars.SafeString(step.sequenceNumber);
    }
  });

  Handlebars.registerHelper('stripSuiteName', function(testCaseName) {
    return new Handlebars.SafeString(testCaseName.substring(testCaseName.lastIndexOf('.') + 1));
  });
})(window.Handlebars);
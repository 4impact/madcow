this["JST"] = this["JST"] || {};

Handlebars.registerPartial("setting", Handlebars.template({"1":function(depth0,helpers,partials,data) {
  var stack1, helperMissing=helpers.helperMissing, buffer = "";
  stack1 = ((helpers.eachInMap || (depth0 && depth0.eachInMap) || helperMissing).call(depth0, (depth0 != null ? depth0.value : depth0), {"name":"eachInMap","hash":{},"fn":this.program(2, data),"inverse":this.noop,"data":data}));
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"2":function(depth0,helpers,partials,data) {
  var stack1, helperMissing=helpers.helperMissing, buffer = "\n              <ul>\n                <li>\n";
  stack1 = ((helpers.if_object || (depth0 && depth0.if_object) || helperMissing).call(depth0, (depth0 != null ? depth0.value : depth0), {"name":"if_object","hash":{},"fn":this.program(3, data),"inverse":this.program(6, data),"data":data}));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "                </li>\n\n              </ul>\n\n";
},"3":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "\n                    <b>"
    + escapeExpression(((helper = (helper = helpers.key || (depth0 != null ? depth0.key : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"key","hash":{},"data":data}) : helper)))
    + "</b>\n\n                    <ul class=\"child-setting\">\n";
  stack1 = ((helpers.eachInMap || (depth0 && depth0.eachInMap) || helperMissing).call(depth0, (depth0 != null ? depth0.value : depth0), {"name":"eachInMap","hash":{},"fn":this.program(4, data),"inverse":this.noop,"data":data}));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "                    </ul>\n";
},"4":function(depth0,helpers,partials,data) {
  var helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression;
  return "                        <li>\n                          <b>"
    + escapeExpression(((helper = (helper = helpers.key || (depth0 != null ? depth0.key : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"key","hash":{},"data":data}) : helper)))
    + ":</b> "
    + escapeExpression(((helper = (helper = helpers.value || (depth0 != null ? depth0.value : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"value","hash":{},"data":data}) : helper)))
    + "\n                        </li>\n";
},"6":function(depth0,helpers,partials,data) {
  var helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression;
  return "                    <b>"
    + escapeExpression(((helper = (helper = helpers.key || (depth0 != null ? depth0.key : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"key","hash":{},"data":data}) : helper)))
    + ":</b> "
    + escapeExpression(((helper = (helper = helpers.value || (depth0 != null ? depth0.value : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"value","hash":{},"data":data}) : helper)))
    + " <br/>\n";
},"8":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, buffer = "            ";
  stack1 = ((helper = (helper = helpers.value || (depth0 != null ? depth0.value : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"value","hash":{},"data":data}) : helper));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n";
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  var stack1, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "    <tr>\n        <td class=\"setting-key\"><h3>"
    + escapeExpression(((helpers.capitalize || (depth0 && depth0.capitalize) || helperMissing).call(depth0, ((helpers.humanize || (depth0 && depth0.humanize) || helperMissing).call(depth0, (depth0 != null ? depth0.key : depth0), {"name":"humanize","hash":{},"data":data})), {"name":"capitalize","hash":{},"data":data})))
    + "</h3></td>\n        <td>\n";
  stack1 = ((helpers.if_object || (depth0 && depth0.if_object) || helperMissing).call(depth0, (depth0 != null ? depth0.value : depth0), {"name":"if_object","hash":{},"fn":this.program(1, data),"inverse":this.program(8, data),"data":data}));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "        </td>\n    </tr>\n\n";
},"useData":true}));

Handlebars.registerPartial("suite", Handlebars.template({"1":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "<div class=\"suite-results box\">\n\n    <div class=\"box-header\">\n      "
    + escapeExpression(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"name","hash":{},"data":data}) : helper)))
    + "\n    </div>\n    <div class=\"box-body\">\n      <table>\n          <thead>\n\n          </thead>\n          <tbody>\n\n";
  stack1 = helpers.each.call(depth0, (depth0 != null ? depth0.testCases : depth0), {"name":"each","hash":{},"fn":this.program(2, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n          </tbody>\n      </table>\n    </div>\n</div>\n";
},"2":function(depth0,helpers,partials,data) {
  var stack1, buffer = "";
  stack1 = this.invokePartial(partials.testcase, '            ', 'testcase', depth0, undefined, helpers, partials, data);
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"4":function(depth0,helpers,partials,data) {
  var stack1, buffer = "";
  stack1 = this.invokePartial(partials.suite, '  ', 'suite', depth0, undefined, helpers, partials, data);
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, lambda=this.lambda, buffer = "\n\n";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.testCases : depth0), {"name":"if","hash":{},"fn":this.program(1, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "\n";
  stack1 = helpers.each.call(depth0, (depth0 != null ? depth0.suites : depth0), {"name":"each","hash":{},"fn":this.program(4, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n<!--<tr class=\"suite-result-header-row\">-->\n    <!--<td colspan=\"3\">-->\n      <!--<h3>"
    + escapeExpression(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"name","hash":{},"data":data}) : helper)))
    + " <p class=\"subtitle\">"
    + escapeExpression(lambda(((stack1 = (depth0 != null ? depth0.testCases : depth0)) != null ? stack1.length : stack1), depth0))
    + " test(s)</p></h3>-->\n        <!--"
    + escapeExpression(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"name","hash":{},"data":data}) : helper)))
    + "-->\n    <!--</td>-->\n<!--</tr>-->";
},"usePartial":true,"useData":true}));

Handlebars.registerPartial("testcase", Handlebars.template({"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "    <tr data-link=\"#testcase/"
    + escapeExpression(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"name","hash":{},"data":data}) : helper)))
    + "\">\n        <td>\n            <a href=\"#testcase/"
    + escapeExpression(((helper = (helper = helpers.name || (depth0 != null ? depth0.name : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"name","hash":{},"data":data}) : helper)))
    + "\">";
  stack1 = ((helpers.stripSuiteName || (depth0 && depth0.stripSuiteName) || helperMissing).call(depth0, (depth0 != null ? depth0.name : depth0), {"name":"stripSuiteName","hash":{},"data":data}));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "</a>\n        </td>\n        <td>\n            <span class=\"label label-"
    + escapeExpression(((helper = (helper = helpers.status || (depth0 != null ? depth0.status : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"status","hash":{},"data":data}) : helper)))
    + "\">"
    + escapeExpression(((helpers.niceStatus || (depth0 && depth0.niceStatus) || helperMissing).call(depth0, (depth0 != null ? depth0.status : depth0), {"name":"niceStatus","hash":{},"data":data})))
    + "</span>\n        </td>\n        <td>"
    + escapeExpression(((helper = (helper = helpers.time || (depth0 != null ? depth0.time : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"time","hash":{},"data":data}) : helper)))
    + "s</td>\n    </tr>";
},"useData":true}));

Handlebars.registerPartial("step", Handlebars.template({"1":function(depth0,helpers,partials,data) {
  return "has-parent";
  },"3":function(depth0,helpers,partials,data) {
  return "is-parent";
  },"5":function(depth0,helpers,partials,data) {
  return "last";
  },"7":function(depth0,helpers,partials,data) {
  var stack1, helper, lambda=this.lambda, functionType="function", helperMissing=helpers.helperMissing, buffer = "            <a class=\"btn btn-info\" href=\"results/";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.name : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "/";
  stack1 = ((helper = (helper = helpers.sequenceNumberString || (depth0 != null ? depth0.sequenceNumberString : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"sequenceNumberString","hash":{},"data":data}) : helper));
  if (stack1 != null) { buffer += stack1; }
  return buffer + ".html\">\n              <i class=\"fa fa-download\"></i>\n            </a>\n";
},"9":function(depth0,helpers,partials,data) {
  var stack1, helper, lambda=this.lambda, functionType="function", helperMissing=helpers.helperMissing, buffer = "            <a class=\"btn btn-info\" href=\"results/";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.name : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "/";
  stack1 = ((helper = (helper = helpers.sequenceNumberString || (depth0 != null ? depth0.sequenceNumberString : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"sequenceNumberString","hash":{},"data":data}) : helper));
  if (stack1 != null) { buffer += stack1; }
  return buffer + ".png\">\n              <i class=\"fa fa-camera\"></i>\n            </a>\n";
},"11":function(depth0,helpers,partials,data) {
  var stack1, buffer = "\n";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.time : depth0), {"name":"if","hash":{},"fn":this.program(12, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"12":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, buffer = "        ";
  stack1 = ((helper = (helper = helpers.time || (depth0 != null ? depth0.time : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"time","hash":{},"data":data}) : helper));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "s\n";
},"14":function(depth0,helpers,partials,data) {
  var stack1, lambda=this.lambda, buffer = "          <br/><br/>";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.result : depth0)) != null ? stack1.detailMessage : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n";
},"16":function(depth0,helpers,partials,data,depths) {
  var stack1, buffer = "";
  stack1 = helpers.each.call(depth0, (depth0 != null ? depth0.steps : depth0), {"name":"each","hash":{},"fn":this.program(17, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"17":function(depth0,helpers,partials,data,depths) {
  var stack1, helperMissing=helpers.helperMissing, buffer = "";
  stack1 = this.invokePartial(partials.step, '    ', 'step', depth0, {
    'parentStepSequenceNumber': (((helpers.formatSequenceNumber || (depth0 && depth0.formatSequenceNumber) || helperMissing).call(depth0, depths[1], (depths[1] != null ? depths[1].parentStepSequenceNumber : depths[1]), {"name":"formatSequenceNumber","hash":{},"data":data}))),
    'parentStep': (depths[1]),
    'testCase': ((depths[1] != null ? depths[1].testCase : depths[1]))
  }, helpers, partials, data);
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data,depths) {
  var stack1, lambda=this.lambda, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "<tr class=\"step-action step-";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.result : depth0)) != null ? stack1.status : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += " ";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.parentStep : depth0), {"name":"if","hash":{},"fn":this.program(1, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += " ";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.steps : depth0), {"name":"if","hash":{},"fn":this.program(3, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += " ";
  stack1 = helpers['if'].call(depth0, (data && data.last), {"name":"if","hash":{},"fn":this.program(5, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "\">\n    <td>\n        "
    + escapeExpression(((helpers.formatSequenceNumber || (depth0 && depth0.formatSequenceNumber) || helperMissing).call(depth0, depth0, (depth0 != null ? depth0.parentStepSequenceNumber : depth0), {"name":"formatSequenceNumber","hash":{},"data":data})))
    + "\n    </td>\n\n    <td nowrap=\"nowrap\">\n        <div class=\"btn-group\">\n          "
    + escapeExpression(((helpers.stepKeyIcon || (depth0 && depth0.stepKeyIcon) || helperMissing).call(depth0, depth0, {"name":"stepKeyIcon","hash":{},"data":data})))
    + "\n\n";
  stack1 = helpers['if'].call(depth0, ((stack1 = (depth0 != null ? depth0.result : depth0)) != null ? stack1.hasResultFile : stack1), {"name":"if","hash":{},"fn":this.program(7, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "\n";
  stack1 = helpers['if'].call(depth0, ((stack1 = (depth0 != null ? depth0.result : depth0)) != null ? stack1.hasScreenshot : stack1), {"name":"if","hash":{},"fn":this.program(9, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "        </div>\n    </td>\n\n    <td class=\"step-time\">";
  stack1 = ((helpers.if_noteq || (depth0 && depth0.if_noteq) || helperMissing).call(depth0, ((stack1 = (depth0 != null ? depth0.result : depth0)) != null ? stack1.status : stack1), "NOT_YET_EXECUTED", {"name":"if_noteq","hash":{},"fn":this.program(11, data, depths),"inverse":this.noop,"data":data}));
  if (stack1 != null) { buffer += stack1; }
  buffer += "    </td>\n\n    <td class=\"step-blade\">";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.blade : depth0)) != null ? stack1.line : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "</td>\n\n    <td class=\"step-message\">";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.result : depth0)) != null ? stack1.message : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "\n";
  stack1 = helpers['if'].call(depth0, ((stack1 = (depth0 != null ? depth0.result : depth0)) != null ? stack1.detailMessage : stack1), {"name":"if","hash":{},"fn":this.program(14, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "    </td>\n</tr>\n\n";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.steps : depth0), {"name":"if","hash":{},"fn":this.program(16, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"usePartial":true,"useData":true,"useDepths":true}));

this["JST"]["mappings/mappings"] = Handlebars.template({"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  return "<div class=\"header\">\n    <h1>\n        Mappings\n    </h1>\n</div>\n\n<div class=\"mappings\">\n    TODO - dump mappings\n</div>";
  },"useData":true});

this["JST"]["settings/settings"] = Handlebars.template({"1":function(depth0,helpers,partials,data) {
  var stack1, buffer = "";
  stack1 = this.invokePartial(partials.setting, '                  ', 'setting', depth0, undefined, helpers, partials, data);
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  var stack1, helperMissing=helpers.helperMissing, buffer = "<div class=\"header\">\n    <h1>\n      Settings\n    </h1>\n</div>\n\n<div class=\"settings\">\n\n    <div class=\"settings-list box\">\n        <div class=\"box-body\">\n            <table>\n                <tbody>\n\n";
  stack1 = ((helpers.eachInMap || (depth0 && depth0.eachInMap) || helperMissing).call(depth0, (depth0 != null ? depth0.config : depth0), {"name":"eachInMap","hash":{},"fn":this.program(1, data),"inverse":this.noop,"data":data}));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n                </tbody>\n            </table>\n        </div>\n    </div>\n\n</div>";
},"usePartial":true,"useData":true});

this["JST"]["sidebar"] = Handlebars.template({"1":function(depth0,helpers,partials,data) {
  return "class=\"active\"";
  },"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  var stack1, buffer = "\n<h1>\n    madcow <p class=\"subtitle\">v2.0</p>\n</h1>\n\n<ul class=\"nav\">\n    <li ";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.suite : depth0), {"name":"if","hash":{},"fn":this.program(1, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += ">\n        <a href=\"#\" alt=\"Results\"><i class=\"fa fa-tasks\"></i></a>\n    </li>\n    <li ";
  stack1 = helpers['if'].call(depth0, (depth0 != null ? depth0.settings : depth0), {"name":"if","hash":{},"fn":this.program(1, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + ">\n        <a href=\"#settings\" alt=\"Settings\"><i class=\"fa fa-wrench\"></i></a>\n    </li>\n</ul>";
},"useData":true});

this["JST"]["suite/suite"] = Handlebars.template({"1":function(depth0,helpers,partials,data) {
  var stack1, buffer = "";
  stack1 = this.invokePartial(partials.testcase, '                ', 'testcase', depth0, undefined, helpers, partials, data);
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"3":function(depth0,helpers,partials,data) {
  var stack1, buffer = "";
  stack1 = this.invokePartial(partials.suite, '    ', 'suite', depth0, undefined, helpers, partials, data);
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data) {
  var stack1, lambda=this.lambda, buffer = "<div class=\"header suite-header\">\n    <h1>\n        Suite Results\n        <p class=\"subtitle\">";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.overview : depth0)) != null ? stack1.timestampFormatted : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "</p>\n    </h1>\n    <div class=\"header-stats\">\n      <div class=\"header-stats-box chart-wrapper\">\n          <canvas class=\"chart\"></canvas>\n      </div>\n      <div class=\"header-stats-box time-wrapper\">\n          <span class=\"time-label\">Total Time</span>\n          <span class=\"time-value\">";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.overview : depth0)) != null ? stack1.totalTime : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "</span>\n      </div>\n    </div>\n</div>\n\n<div class=\"suite\">\n\n  <div class=\"suite-results box\">\n      <div class=\"box-body\">\n        <table>\n            <thead>\n\n            </thead>\n            <tbody>\n\n";
  stack1 = helpers.each.call(depth0, ((stack1 = (depth0 != null ? depth0.suite : depth0)) != null ? stack1.testCases : stack1), {"name":"each","hash":{},"fn":this.program(1, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "            </tbody>\n        </table>\n      </div>\n  </div>\n\n";
  stack1 = helpers.each.call(depth0, ((stack1 = (depth0 != null ? depth0.suite : depth0)) != null ? stack1.suites : stack1), {"name":"each","hash":{},"fn":this.program(3, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "\n</div>";
},"usePartial":true,"useData":true});

this["JST"]["testcase/testcase"] = Handlebars.template({"1":function(depth0,helpers,partials,data) {
  var stack1, lambda=this.lambda, buffer = "        <li>\n            <span class=\"title\">Suite</span>\n            <span>";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.suiteName : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  return buffer + "</span>\n        </li>\n";
},"3":function(depth0,helpers,partials,data) {
  var stack1, buffer = "            <ol>\n";
  stack1 = helpers.each.call(depth0, ((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.testInfo : stack1), {"name":"each","hash":{},"fn":this.program(4, data),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "            </ol>\n";
},"4":function(depth0,helpers,partials,data) {
  var lambda=this.lambda, escapeExpression=this.escapeExpression;
  return "                    <li>\n                        <a href=\"#\">"
    + escapeExpression(lambda(depth0, depth0))
    + "</a>\n                    </li>\n";
},"6":function(depth0,helpers,partials,data) {
  return "              None\n";
  },"8":function(depth0,helpers,partials,data) {
  var stack1, helperMissing=helpers.helperMissing, buffer = "        <div class=\"runtime-data\">\n";
  stack1 = ((helpers.eachInMap || (depth0 && depth0.eachInMap) || helperMissing).call(depth0, ((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.reportDetails : stack1), {"name":"eachInMap","hash":{},"fn":this.program(9, data),"inverse":this.noop,"data":data}));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "        </div>\n";
},"9":function(depth0,helpers,partials,data) {
  var stack1, helper, functionType="function", helperMissing=helpers.helperMissing, buffer = "            <h3>";
  stack1 = ((helper = (helper = helpers.key || (depth0 != null ? depth0.key : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"key","hash":{},"data":data}) : helper));
  if (stack1 != null) { buffer += stack1; }
  buffer += " <span class=\"details\">";
  stack1 = ((helper = (helper = helpers.value || (depth0 != null ? depth0.value : depth0)) != null ? helper : helperMissing),(typeof helper === functionType ? helper.call(depth0, {"name":"value","hash":{},"data":data}) : helper));
  if (stack1 != null) { buffer += stack1; }
  return buffer + "</span></h3>\n";
},"11":function(depth0,helpers,partials,data,depths) {
  var stack1, buffer = "";
  stack1 = this.invokePartial(partials.step, '              ', 'step', depth0, {
    'testCase': ((depths[1] != null ? depths[1].testCase : depths[1]))
  }, helpers, partials, data);
  if (stack1 != null) { buffer += stack1; }
  return buffer;
},"compiler":[6,">= 2.0.0-beta.1"],"main":function(depth0,helpers,partials,data,depths) {
  var stack1, lambda=this.lambda, helperMissing=helpers.helperMissing, escapeExpression=this.escapeExpression, buffer = "<div class=\"header header-";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.status : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "\">\n    <h1>\n        <a href=\"#\" class=\"back-link\"><i class=\"fa fa-angle-left\"></i></a> ";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.name : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "\n        <p class=\"subtitle\">"
    + escapeExpression(((helpers.niceStatus || (depth0 && depth0.niceStatus) || helperMissing).call(depth0, ((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.status : stack1), {"name":"niceStatus","hash":{},"data":data})))
    + "</p>\n    </h1>\n\n    <ul class=\"links\">\n        <li>\n            <span class=\"title\"><a href=\"results/";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.name : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "/madcow.log\" class=\"btn\">Open Log</a></span>\n        </li>\n        <li>\n            <span class=\"title\">Steps</span>\n            <span>";
  stack1 = lambda(((stack1 = ((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.steps : stack1)) != null ? stack1.length : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "</span>\n        </li>\n        <li>\n            <span class=\"title\">Time</span>\n            <span>";
  stack1 = lambda(((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.time : stack1), depth0);
  if (stack1 != null) { buffer += stack1; }
  buffer += "s</span>\n        </li>\n";
  stack1 = helpers['if'].call(depth0, ((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.suiteName : stack1), {"name":"if","hash":{},"fn":this.program(1, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "    </ul>\n</div>\n\n<div class=\"testcase\">\n\n    <div class=\"testcase-info box\">\n        <div class=\"test-info\">\n            <h3>Test Information</h3>\n\n";
  stack1 = helpers['if'].call(depth0, ((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.testInfo : stack1), {"name":"if","hash":{},"fn":this.program(3, data, depths),"inverse":this.program(6, data, depths),"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "        </div>\n\n";
  stack1 = helpers['if'].call(depth0, ((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.reportDetails : stack1), {"name":"if","hash":{},"fn":this.program(8, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  buffer += "    </div>\n\n    <div class=\"testcase-results box\">\n      <table>\n          <thead>\n            <tr>\n                <th>#</th>\n                <th>Status</th>\n                <th>Time</th>\n                <th>Grass</th>\n                <th>Details</th>\n            </tr>\n          </thead>\n          <tbody>\n";
  stack1 = helpers.each.call(depth0, ((stack1 = (depth0 != null ? depth0.testCase : depth0)) != null ? stack1.steps : stack1), {"name":"each","hash":{},"fn":this.program(11, data, depths),"inverse":this.noop,"data":data});
  if (stack1 != null) { buffer += stack1; }
  return buffer + "          </tbody>\n      </table>\n    </div>\n\n</div>";
},"usePartial":true,"useData":true,"useDepths":true});
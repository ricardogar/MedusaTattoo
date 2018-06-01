(function() {
    'use strict';

    angular
        .module('medusaTattooApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'highcharts-ng',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'g1b.scroll-events',
            'multipleSelect',
            'ngSanitize',
            'ui.select',
			'mwl.calendar',
            'g1b.datetime-range'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();

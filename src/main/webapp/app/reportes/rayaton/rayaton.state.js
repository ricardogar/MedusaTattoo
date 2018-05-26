(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('Rrayaton', {
            parent: 'app',
            url: '/Rrayaton',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'medusaTattooApp.rayaton.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/reportes/rayaton/rayatons.html',
                    controller: 'RayatonReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rayaton');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }

})();

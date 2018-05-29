(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('Rsedes', {
            parent: 'app',
            url: '/Rsedes',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'medusaTattooApp.sede.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/reportes/sedes/sedes.html',
                    controller: 'SedesReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sede');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }

})();

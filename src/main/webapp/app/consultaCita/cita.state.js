(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('consultaCita', {
            parent: 'app',
            url: '/consultaCita',
            data: {
                authorities: [],
                pageTitle: 'medusaTattooApp.cita.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/consultaCita/citas.html',
                    controller: 'consultaCitaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cita');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }

})();

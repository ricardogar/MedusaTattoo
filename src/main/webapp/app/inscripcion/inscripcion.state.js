(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inscripcion_rayaton', {
            parent: 'app',
            url: '/inscripcion_rayaton',
            data: {
                authorities: [],
                pageTitle: 'medusaTattooApp.inscripcion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/inscripcion/inscripcions.html',
                    controller: 'InscripcionRController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscripcion');
                    $translatePartialLoader.addPart('estado_inscripcion');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }

})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('Rtatuadores', {
            parent: 'app',
            url: '/Rtatuadores',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'medusaTattooApp.tatuador.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/reportes/tatuadores/tatuadores.html',
                    controller: 'TatuadoresReportController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tatuador');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }

})();

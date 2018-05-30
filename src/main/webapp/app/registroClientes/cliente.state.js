(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('registroCliente', {
            parent: 'app',
            url: '/registro_cliente',
            data: {
                authorities: [],
                pageTitle: 'medusaTattooApp.cliente.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/registroClientes/clientes.html',
                    controller: 'RegistroClienteController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cliente');
                    $translatePartialLoader.addPart('tipo_documento');
                    $translatePartialLoader.addPart('genero');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
    }

})();

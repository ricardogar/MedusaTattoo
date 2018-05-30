(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tatuador', {
            parent: 'entity',
            url: '/tatuador',
            data: {
                authorities: ['ROLE_SECRETARIA'],
                pageTitle: 'medusaTattooApp.tatuador.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tatuador/tatuadors.html',
                    controller: 'TatuadorController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tatuador');
                    $translatePartialLoader.addPart('tipo_documento');
                    $translatePartialLoader.addPart('genero');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tatuador-detail', {
            parent: 'tatuador',
            url: '/tatuador/{id}',
            data: {
                authorities: ['ROLE_SECRETARIA'],
                pageTitle: 'medusaTattooApp.tatuador.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tatuador/tatuador-detail.html',
                    controller: 'TatuadorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tatuador');
                    $translatePartialLoader.addPart('tipo_documento');
                    $translatePartialLoader.addPart('genero');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tatuador', function($stateParams, Tatuador) {
                    return Tatuador.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tatuador',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tatuador-detail.edit', {
            parent: 'tatuador-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tatuador/tatuador-dialog.html',
                    controller: 'TatuadorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tatuador', function(Tatuador) {
                            return Tatuador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tatuador.new', {
            parent: 'tatuador',
            url: '/new',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tatuador/tatuador-dialog.html',
                    controller: 'TatuadorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tipodocumento: null,
                                documento: null,
                                nombre: null,
                                apellido: null,
                                telefono: null,
                                genero: null,
                                apodo: null,
                                foto: null,
                                fotoContentType: null,
                                estado: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tatuador', null, { reload: 'tatuador' });
                }, function() {
                    $state.go('tatuador');
                });
            }]
        })
        .state('tatuador.edit', {
            parent: 'tatuador',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tatuador/tatuador-dialog.html',
                    controller: 'TatuadorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tatuador', function(Tatuador) {
                            return Tatuador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tatuador', null, { reload: 'tatuador' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tatuador.delete', {
            parent: 'tatuador',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tatuador/tatuador-delete-dialog.html',
                    controller: 'TatuadorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tatuador', function(Tatuador) {
                            return Tatuador.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tatuador', null, { reload: 'tatuador' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

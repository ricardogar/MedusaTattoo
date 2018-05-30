(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('palabra-clave', {
            parent: 'entity',
            url: '/palabra-clave',
            data: {
                authorities: ['ROLE_SECRETARIA'],
                pageTitle: 'medusaTattooApp.palabraClave.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/palabra-clave/palabra-claves.html',
                    controller: 'PalabraClaveController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('palabraClave');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('palabra-clave-detail', {
            parent: 'palabra-clave',
            url: '/palabra-clave/{id}',
            data: {
                authorities: ['ROLE_SECRETARIA'],
                pageTitle: 'medusaTattooApp.palabraClave.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/palabra-clave/palabra-clave-detail.html',
                    controller: 'PalabraClaveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('palabraClave');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PalabraClave', function($stateParams, PalabraClave) {
                    return PalabraClave.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'palabra-clave',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('palabra-clave-detail.edit', {
            parent: 'palabra-clave-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/palabra-clave/palabra-clave-dialog.html',
                    controller: 'PalabraClaveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PalabraClave', function(PalabraClave) {
                            return PalabraClave.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('palabra-clave.new', {
            parent: 'palabra-clave',
            url: '/new',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/palabra-clave/palabra-clave-dialog.html',
                    controller: 'PalabraClaveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                palabra: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('palabra-clave', null, { reload: 'palabra-clave' });
                }, function() {
                    $state.go('palabra-clave');
                });
            }]
        })
        .state('palabra-clave.edit', {
            parent: 'palabra-clave',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/palabra-clave/palabra-clave-dialog.html',
                    controller: 'PalabraClaveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PalabraClave', function(PalabraClave) {
                            return PalabraClave.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('palabra-clave', null, { reload: 'palabra-clave' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('palabra-clave.delete', {
            parent: 'palabra-clave',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/palabra-clave/palabra-clave-delete-dialog.html',
                    controller: 'PalabraClaveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PalabraClave', function(PalabraClave) {
                            return PalabraClave.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('palabra-clave', null, { reload: 'palabra-clave' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

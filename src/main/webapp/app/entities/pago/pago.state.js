(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('pago', {
            parent: 'entity',
            url: '/pago',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'medusaTattooApp.pago.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pago/pagos.html',
                    controller: 'PagoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pago');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('pago-detail', {
            parent: 'pago',
            url: '/pago/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'medusaTattooApp.pago.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/pago/pago-detail.html',
                    controller: 'PagoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('pago');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Pago', function($stateParams, Pago) {
                    return Pago.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'pago',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('pago-detail.edit', {
            parent: 'pago-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pago/pago-dialog.html',
                    controller: 'PagoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pago', function(Pago) {
                            return Pago.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pago.new', {
            parent: 'pago',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pago/pago-dialog.html',
                    controller: 'PagoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                valor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('pago', null, { reload: 'pago' });
                }, function() {
                    $state.go('pago');
                });
            }]
        })
        .state('pago.edit', {
            parent: 'pago',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pago/pago-dialog.html',
                    controller: 'PagoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Pago', function(Pago) {
                            return Pago.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pago', null, { reload: 'pago' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('pago.delete', {
            parent: 'pago',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/pago/pago-delete-dialog.html',
                    controller: 'PagoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Pago', function(Pago) {
                            return Pago.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('pago', null, { reload: 'pago' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

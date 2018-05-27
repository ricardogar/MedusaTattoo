(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sede', {
            parent: 'entity',
            url: '/sede',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'medusaTattooApp.sede.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sede/sedes.html',
                    controller: 'SedeController',
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
        .state('sede-detail', {
            parent: 'sede',
            url: '/sede/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'medusaTattooApp.sede.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sede/sede-detail.html',
                    controller: 'SedeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sede');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Sede', function($stateParams, Sede) {
                    return Sede.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sede',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sede-detail.edit', {
            parent: 'sede-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sede/sede-dialog.html',
                    controller: 'SedeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sede', function(Sede) {
                            return Sede.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sede.new', {
            parent: 'sede',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sede/sede-dialog.html',
                    controller: 'SedeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                direccion: null,
                                telefono: null,
                                estado: true,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sede', null, { reload: 'sede' });
                }, function() {
                    $state.go('sede');
                });
            }]
        })
        .state('sede.edit', {
            parent: 'sede',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sede/sede-dialog.html',
                    controller: 'SedeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Sede', function(Sede) {
                            return Sede.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sede', null, { reload: 'sede' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sede.delete', {
            parent: 'sede',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sede/sede-delete-dialog.html',
                    controller: 'SedeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Sede', function(Sede) {
                            return Sede.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sede', null, { reload: 'sede' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('trabajo', {
            parent: 'entity',
            url: '/trabajo',
            data: {
                authorities: ['ROLE_SECRETARIA','ROLE_CLIENTE'],
                pageTitle: 'medusaTattooApp.trabajo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trabajo/trabajos.html',
                    controller: 'TrabajoController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trabajo');
                    $translatePartialLoader.addPart('estado_trabajo');
                    $translatePartialLoader.addPart('tipo_trabajo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('trabajo-detail', {
            parent: 'trabajo',
            url: '/trabajo/{id}',
            data: {
                authorities: ['ROLE_SECRETARIA','ROLE_CLIENTE'],
                pageTitle: 'medusaTattooApp.trabajo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/trabajo/trabajo-detail.html',
                    controller: 'TrabajoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('trabajo');
                    $translatePartialLoader.addPart('estado_trabajo');
                    $translatePartialLoader.addPart('tipo_trabajo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Trabajo', function($stateParams, Trabajo) {
                    return Trabajo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'trabajo',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('trabajo-detail.edit', {
            parent: 'trabajo-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trabajo/trabajo-dialog.html',
                    controller: 'TrabajoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trabajo', function(Trabajo) {
                            return Trabajo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trabajo.new', {
            parent: 'trabajo',
            url: '/new',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trabajo/trabajo-dialog.html',
                    controller: 'TrabajoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                costoTotal: null,
                                totalPagado: null,
                                estado: "EN_PROGRESO",
                                tipo: "NORMAL",
                                foto: null,
                                fotoContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('trabajo', null, { reload: 'trabajo' });
                }, function() {
                    $state.go('trabajo');
                });
            }]
        })
        .state('trabajo.edit', {
            parent: 'trabajo',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trabajo/trabajo-dialog.html',
                    controller: 'TrabajoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Trabajo', function(Trabajo) {
                            return Trabajo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trabajo', null, { reload: 'trabajo' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('trabajo.delete', {
            parent: 'trabajo',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/trabajo/trabajo-delete-dialog.html',
                    controller: 'TrabajoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Trabajo', function(Trabajo) {
                            return Trabajo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('trabajo', null, { reload: 'trabajo' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

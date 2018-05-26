(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('rayaton', {
            parent: 'entity',
            url: '/rayaton',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'medusaTattooApp.rayaton.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rayaton/rayatons.html',
                    controller: 'RayatonController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rayaton');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('rayaton-detail', {
            parent: 'rayaton',
            url: '/rayaton/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'medusaTattooApp.rayaton.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/rayaton/rayaton-detail.html',
                    controller: 'RayatonDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('rayaton');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Rayaton', function($stateParams, Rayaton) {
                    return Rayaton.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'rayaton',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('rayaton-detail.edit', {
            parent: 'rayaton-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rayaton/rayaton-dialog.html',
                    controller: 'RayatonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rayaton', function(Rayaton) {
                            return Rayaton.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rayaton.new', {
            parent: 'rayaton',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rayaton/rayaton-dialog.html',
                    controller: 'RayatonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fecha: null,
                                cupos: null,
                                valorCupo: null,
                                comentario: null,
                                imagen: null,
                                imagenContentType: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('rayaton', null, { reload: 'rayaton' });
                }, function() {
                    $state.go('rayaton');
                });
            }]
        })
        .state('rayaton.edit', {
            parent: 'rayaton',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rayaton/rayaton-dialog.html',
                    controller: 'RayatonDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Rayaton', function(Rayaton) {
                            return Rayaton.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rayaton', null, { reload: 'rayaton' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('rayaton.delete', {
            parent: 'rayaton',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/rayaton/rayaton-delete-dialog.html',
                    controller: 'RayatonDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Rayaton', function(Rayaton) {
                            return Rayaton.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('rayaton', null, { reload: 'rayaton' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

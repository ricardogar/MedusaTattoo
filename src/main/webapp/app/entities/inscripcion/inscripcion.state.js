(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('inscripcion', {
            parent: 'entity',
            url: '/inscripcion',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'medusaTattooApp.inscripcion.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscripcion/inscripcions.html',
                    controller: 'InscripcionController',
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
        .state('inscripcion-detail', {
            parent: 'inscripcion',
            url: '/inscripcion/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'medusaTattooApp.inscripcion.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/inscripcion/inscripcion-detail.html',
                    controller: 'InscripcionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('inscripcion');
                    $translatePartialLoader.addPart('estado_inscripcion');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Inscripcion', function($stateParams, Inscripcion) {
                    return Inscripcion.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'inscripcion',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('inscripcion-detail.edit', {
            parent: 'inscripcion-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion/inscripcion-dialog.html',
                    controller: 'InscripcionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inscripcion', function(Inscripcion) {
                            return Inscripcion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscripcion.new', {
            parent: 'inscripcion',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion/inscripcion-dialog.html',
                    controller: 'InscripcionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nombre: null,
                                telefono: null,
                                imagen: null,
                                imagenContentType: null,
                                estado: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('inscripcion', null, { reload: 'inscripcion' });
                }, function() {
                    $state.go('inscripcion');
                });
            }]
        })
        .state('inscripcion.edit', {
            parent: 'inscripcion',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion/inscripcion-dialog.html',
                    controller: 'InscripcionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Inscripcion', function(Inscripcion) {
                            return Inscripcion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscripcion', null, { reload: 'inscripcion' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('inscripcion.delete', {
            parent: 'inscripcion',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/inscripcion/inscripcion-delete-dialog.html',
                    controller: 'InscripcionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Inscripcion', function(Inscripcion) {
                            return Inscripcion.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('inscripcion', null, { reload: 'inscripcion' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

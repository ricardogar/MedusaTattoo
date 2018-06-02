(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('cita', {
            parent: 'entity',
            url: '/cita',
            data: {
                authorities: ['ROLE_SECRETARIA','ROLE_CLIENTE'],
                pageTitle: 'medusaTattooApp.cita.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cita/citas.html',
                    controller: 'CitaController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cita');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('cita-detail', {
            parent: 'cita',
            url: '/cita/{id}',
            data: {
                authorities: ['ROLE_SECRETARIA'],
                pageTitle: 'medusaTattooApp.cita.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/cita/cita-detail.html',
                    controller: 'CitaDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cita');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Cita', function($stateParams, Cita) {
                    return Cita.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'cita',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('cita-detail.edit', {
            parent: 'cita-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cita/cita-dialog.html',
                    controller: 'CitaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cita', function(Cita) {
                            return Cita.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cita.new', {
            parent: 'cita',
            url: '/new',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cita/cita-dialog.html',
                    controller: 'CitaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                fechaYHora: null,
                                duracion: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('cita', null, { reload: 'cita' });
                }, function() {
                    $state.go('cita');
                });
            }]
        })
         .state('cita.new.calendar', {
                parent: 'cita',
                url: '/new/{fecha}/{duracion}',
                data: {
                    authorities: ['ROLE_SECRETARIA']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'app/entities/cita/cita-dialog-calendar.html',
                        controller: 'CitaDialogCalendarController',
                        controllerAs: 'vm',
                        backdrop: 'static',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fechaYHora: $stateParams.fecha,
                                    duracion: $stateParams.duracion,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function() {
                        $state.go('cita', null, { reload: 'cita' });
                    }, function() {
                        $state.go('cita');
                    });
                }]
            })
        .state('cita.edit', {
            parent: 'cita',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cita/cita-dialog.html',
                    controller: 'CitaDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Cita', function(Cita) {
                            return Cita.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cita', null, { reload: 'cita' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('cita.delete', {
            parent: 'cita',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_SECRETARIA']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/cita/cita-delete-dialog.html',
                    controller: 'CitaDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Cita', function(Cita) {
                            return Cita.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('cita', null, { reload: 'cita' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

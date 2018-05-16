(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('ClienteDetailController', ClienteDetailController);

    ClienteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cliente', 'Trabajo', 'Inscripcion'];

    function ClienteDetailController($scope, $rootScope, $stateParams, previousState, entity, Cliente, Trabajo, Inscripcion) {
        var vm = this;

        vm.cliente = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('medusaTattooApp:clienteUpdate', function(event, result) {
            vm.cliente = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('CitaDetailController', CitaDetailController);

    CitaDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Cita', 'Trabajo'];

    function CitaDetailController($scope, $rootScope, $stateParams, previousState, entity, Cita, Trabajo) {
        var vm = this;

        vm.cita = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('medusaTattooApp:citaUpdate', function(event, result) {
            vm.cita = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TrabajoDetailController', TrabajoDetailController);

    TrabajoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Trabajo', 'Rayaton', 'Pago', 'Cita', 'Foto', 'Tatuador', 'Cliente', 'Sede'];

    function TrabajoDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Trabajo, Rayaton, Pago, Cita, Foto, Tatuador, Cliente, Sede) {
        var vm = this;

        vm.trabajo = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('medusaTattooApp:trabajoUpdate', function(event, result) {
            vm.trabajo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

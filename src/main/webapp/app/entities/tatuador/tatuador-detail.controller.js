(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TatuadorDetailController', TatuadorDetailController);

    TatuadorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Tatuador', 'Trabajo', 'Rayaton'];

    function TatuadorDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Tatuador, Trabajo, Rayaton) {
        var vm = this;

        vm.tatuador = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('medusaTattooApp:tatuadorUpdate', function(event, result) {
            vm.tatuador = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

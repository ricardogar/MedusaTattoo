(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('FotoDetailController', FotoDetailController);

    FotoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Foto', 'PalabraClave', 'Trabajo'];

    function FotoDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Foto, PalabraClave, Trabajo) {
        var vm = this;

        vm.foto = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('medusaTattooApp:fotoUpdate', function(event, result) {
            vm.foto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

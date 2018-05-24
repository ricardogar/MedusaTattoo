(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RayatonDetailController', RayatonDetailController);

    RayatonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Rayaton', 'Inscripcion', 'Trabajo', 'Tatuador'];

    function RayatonDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Rayaton, Inscripcion, Trabajo, Tatuador) {
        var vm = this;

        vm.rayaton = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('medusaTattooApp:rayatonUpdate', function(event, result) {
            vm.rayaton = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

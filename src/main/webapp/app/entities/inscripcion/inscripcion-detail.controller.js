(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('InscripcionDetailController', InscripcionDetailController);

    InscripcionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Inscripcion', 'Rayaton', 'Sede'];

    function InscripcionDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Inscripcion, Rayaton, Sede) {
        var vm = this;

        vm.inscripcion = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('medusaTattooApp:inscripcionUpdate', function(event, result) {
            vm.inscripcion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

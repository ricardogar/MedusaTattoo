(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('InscripcionDetailController', InscripcionDetailController);

    InscripcionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Inscripcion', 'Rayaton', 'Cliente'];

    function InscripcionDetailController($scope, $rootScope, $stateParams, previousState, entity, Inscripcion, Rayaton, Cliente) {
        var vm = this;

        vm.inscripcion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('medusaTattooApp:inscripcionUpdate', function(event, result) {
            vm.inscripcion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

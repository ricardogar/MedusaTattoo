(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('SedeDetailController', SedeDetailController);

    SedeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sede', 'Trabajo', 'Tatuador', 'Inscripcion'];

    function SedeDetailController($scope, $rootScope, $stateParams, previousState, entity, Sede, Trabajo, Tatuador, Inscripcion) {
        var vm = this;

        vm.sede = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('medusaTattooApp:sedeUpdate', function(event, result) {
            vm.sede = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

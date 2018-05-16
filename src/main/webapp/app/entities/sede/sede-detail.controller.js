(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('SedeDetailController', SedeDetailController);

    SedeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sede', 'Trabajo'];

    function SedeDetailController($scope, $rootScope, $stateParams, previousState, entity, Sede, Trabajo) {
        var vm = this;

        vm.sede = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('medusaTattooApp:sedeUpdate', function(event, result) {
            vm.sede = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

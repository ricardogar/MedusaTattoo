(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('PagoDetailController', PagoDetailController);

    PagoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Pago', 'Trabajo'];

    function PagoDetailController($scope, $rootScope, $stateParams, previousState, entity, Pago, Trabajo) {
        var vm = this;

        vm.pago = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('medusaTattooApp:pagoUpdate', function(event, result) {
            vm.pago = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

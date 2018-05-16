(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('PalabraClaveDetailController', PalabraClaveDetailController);

    PalabraClaveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PalabraClave', 'Foto'];

    function PalabraClaveDetailController($scope, $rootScope, $stateParams, previousState, entity, PalabraClave, Foto) {
        var vm = this;

        vm.palabraClave = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('medusaTattooApp:palabraClaveUpdate', function(event, result) {
            vm.palabraClave = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

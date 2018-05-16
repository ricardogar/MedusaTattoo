(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('SedeDeleteController',SedeDeleteController);

    SedeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Sede'];

    function SedeDeleteController($uibModalInstance, entity, Sede) {
        var vm = this;

        vm.sede = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Sede.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RayatonDeleteController',RayatonDeleteController);

    RayatonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Rayaton'];

    function RayatonDeleteController($uibModalInstance, entity, Rayaton) {
        var vm = this;

        vm.rayaton = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Rayaton.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

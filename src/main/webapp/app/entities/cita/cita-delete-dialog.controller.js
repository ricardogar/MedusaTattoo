(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('CitaDeleteController',CitaDeleteController);

    CitaDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cita'];

    function CitaDeleteController($uibModalInstance, entity, Cita) {
        var vm = this;

        vm.cita = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cita.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

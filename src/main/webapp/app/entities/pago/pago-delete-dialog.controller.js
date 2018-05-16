(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('PagoDeleteController',PagoDeleteController);

    PagoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Pago'];

    function PagoDeleteController($uibModalInstance, entity, Pago) {
        var vm = this;

        vm.pago = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Pago.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

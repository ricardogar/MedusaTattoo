(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('PalabraClaveDeleteController',PalabraClaveDeleteController);

    PalabraClaveDeleteController.$inject = ['$uibModalInstance', 'entity', 'PalabraClave'];

    function PalabraClaveDeleteController($uibModalInstance, entity, PalabraClave) {
        var vm = this;

        vm.palabraClave = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PalabraClave.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

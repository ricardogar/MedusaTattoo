(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TatuadorDeleteController',TatuadorDeleteController);

    TatuadorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tatuador'];

    function TatuadorDeleteController($uibModalInstance, entity, Tatuador) {
        var vm = this;

        vm.tatuador = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tatuador.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

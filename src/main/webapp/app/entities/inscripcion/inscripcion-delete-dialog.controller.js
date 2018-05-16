(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('InscripcionDeleteController',InscripcionDeleteController);

    InscripcionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Inscripcion'];

    function InscripcionDeleteController($uibModalInstance, entity, Inscripcion) {
        var vm = this;

        vm.inscripcion = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Inscripcion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

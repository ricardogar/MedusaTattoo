(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('InscripcionDialogController', InscripcionDialogController);

    InscripcionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Inscripcion', 'Rayaton', 'Cliente'];

    function InscripcionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Inscripcion, Rayaton, Cliente) {
        var vm = this;

        vm.inscripcion = entity;
        vm.clear = clear;
        vm.save = save;
        vm.rayatons = Rayaton.query();
        vm.clientes = Cliente.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.inscripcion.id !== null) {
                Inscripcion.update(vm.inscripcion, onSaveSuccess, onSaveError);
            } else {
                Inscripcion.save(vm.inscripcion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:inscripcionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

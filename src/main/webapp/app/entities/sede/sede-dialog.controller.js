(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('SedeDialogController', SedeDialogController);

    SedeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Sede', 'Trabajo', 'Tatuador', 'Inscripcion'];

    function SedeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Sede, Trabajo, Tatuador, Inscripcion) {
        var vm = this;

        vm.sede = entity;
        vm.clear = clear;
        vm.save = save;
        vm.trabajos = Trabajo.query();
        vm.tatuadors = Tatuador.query();
        vm.inscripcions = Inscripcion.query();
        vm.showEstado=vm.sede.id!=null;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sede.id !== null) {
                Sede.update(vm.sede, onSaveSuccess, onSaveError);
            } else {
                Sede.save(vm.sede, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:sedeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

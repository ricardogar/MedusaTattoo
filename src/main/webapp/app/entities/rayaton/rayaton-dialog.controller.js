(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RayatonDialogController', RayatonDialogController);

    RayatonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Rayaton', 'Inscripcion', 'Trabajo', 'Tatuador'];

    function RayatonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Rayaton, Inscripcion, Trabajo, Tatuador) {
        var vm = this;

        vm.rayaton = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.inscripcions = Inscripcion.query();
        vm.trabajos = Trabajo.query();
        vm.tatuadors = Tatuador.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.rayaton.id !== null) {
                Rayaton.update(vm.rayaton, onSaveSuccess, onSaveError);
            } else {
                Rayaton.save(vm.rayaton, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:rayatonUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fecha = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

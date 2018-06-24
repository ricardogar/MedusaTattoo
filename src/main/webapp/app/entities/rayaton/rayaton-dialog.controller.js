(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RayatonDialogController', RayatonDialogController);

    RayatonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Rayaton', 'Inscripcion', 'Trabajo', 'Tatuador','TatuadorActivos'];

    function RayatonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Rayaton, Inscripcion, Trabajo, Tatuador,TatuadorActivos) {
        var vm = this;

        vm.rayaton = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.tatuadors = TatuadorActivos.query();
        vm.options={
          minDate:new Date()
        };

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

        vm.setImagen = function ($file, rayaton) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        rayaton.imagen = base64Data;
                        rayaton.imagenContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

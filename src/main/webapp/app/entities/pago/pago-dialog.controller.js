(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('PagoDialogController', PagoDialogController);

    PagoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pago', 'Trabajo'];

    function PagoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pago, Trabajo) {
        var vm = this;

        vm.pago = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.trabajos = Trabajo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.pago.id !== null) {
                Pago.update(vm.pago, onSaveSuccess, onSaveError);
            } else {
                Pago.save(vm.pago, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:pagoUpdate', result);
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

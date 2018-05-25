(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('CitaDialogController', CitaDialogController);

    CitaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cita', 'Trabajo','Principal','TrabajosByAccountAndStatus'];

    function CitaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cita, Trabajo,Principal,TrabajosByAccountAndStatus) {
        var vm = this;

        vm.cita = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.minDate = new Date();
        vm.minDate.setHours(8);
        vm.minTime=vm.minDate;
        vm.maxDate = new Date();
        vm.maxDate.setFullYear(vm.maxDate.getFullYear()+1);
        vm.maxTime=vm.maxDate;
        vm.maxDate.setHours(18);
        console.log(vm.minDate);
        console.log(vm.maxDate.getHours());
        vm.dateOptions={minDate: vm.minDate,
                        maxDate: vm.maxDate
        };
        vm.timeOptions={min: new Date(vm.minDate.getFullYear(),vm.minDate.getMonth(),vm.minDate.getDay(),7,0,0,0),
                        max: new Date(vm.minDate.getFullYear(),vm.minDate.getMonth(),vm.minDate.getDay(),18,0,0,0)
        };

        //vm.trabajos = TrabajosByAccountAndStatus.query({id:vm.account.id});
		vm.account = null;
		getAccount();

		function getAccount() {
            Principal.identity().then(function(account) {
				console.log("==========cita dialog controller=======");
                vm.account = account;
				console.log(vm.account);
                vm.trabajos = TrabajosByAccountAndStatus.query({id:vm.account.id,status:"EN_PROGRESO"});
            });
        }
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cita.id !== null) {
                Cita.update(vm.cita, onSaveSuccess, onSaveError);
            } else {
                Cita.save(vm.cita, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:citaUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaYHora = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

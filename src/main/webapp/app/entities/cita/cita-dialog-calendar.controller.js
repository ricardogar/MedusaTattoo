(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('CitaDialogCalendarController', CitaDialogCalendarController);

    CitaDialogCalendarController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cita', 'Trabajo','Principal','TrabajosByAccountAndStatus','moment'];

    function CitaDialogCalendarController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cita, Trabajo,Principal,TrabajosByAccountAndStatus,moment) {
        var vm = this;

        moment.locale('es');
        vm.cita = entity;
        vm.clear = clear;
        vm.save = save;
		vm.account = null;

		vm.formatFecha=moment(vm.cita.fechaYHora).format('LL');
		vm.formatStart= moment(vm.cita.fechaYHora).format("h:mm A");
		vm.formatEnd=moment(vm.cita.fechaYHora).add(vm.cita.duracion,'hours').format("h:mm A");
		vm.formatDuracion=vm.cita.duracion==1?"hora":"horas";

		getAccount();

		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
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
    }
})();

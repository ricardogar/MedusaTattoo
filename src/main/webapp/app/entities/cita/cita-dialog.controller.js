(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('CitaDialogController', CitaDialogController);

    CitaDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cita', 'Trabajo','Principal'];

    function CitaDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cita, Trabajo,Principal) {
        var vm = this;

        vm.cita = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.trabajos = Trabajo.query();
		vm.account = null;
		getAccount();
		
		function getAccount() {
            Principal.identity().then(function(account) {
				console.log("==========cita dialog controller=======");
                vm.account = account;
				console.log(vm.account);
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
		$scope.sedeFilter = function (item){
			if(vm.account.authorities.includes("ROLE_ADMIN")){
				return true;
			}else{
				return item.sede.id===vm.account.sede.id
			}
		};
    }
})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('PagoDialogController', PagoDialogController);

    PagoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Pago', 'Trabajo','Principal','TrabajosByAccountAndStatus'];

    function PagoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Pago, Trabajo, Principal,TrabajosByAccountAndStatus) {
        var vm = this;

        vm.pago = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        //vm.trabajos = Trabajo.query({id:vm.account.id});
		vm.pago.fecha=Date.now();
		vm.account = null;
        vm.maxValue = 0;
		getAccount();

		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
				console.log("=================Pago dialog controller====================");
				console.log(vm.account);
                vm.trabajos = TrabajosByAccountAndStatus.query({id:vm.account.id,status:"EN_PROGRESO"});
            });
        }
        $scope.setMaxValue = function () {
            vm.maxValue = vm.pago.trabajo.costoTotal - vm.pago.trabajo.totalPagado;
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
			vm.pago.fecha=Date.now();
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

		/*$scope.sedeFilter = function (item){
			if(vm.account.authorities.includes("ROLE_ADMIN")){
				return true;
			}else{
				return item.sede.id===vm.account.sede.id
			}
		};*/
    }
})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('ClienteDialogController', ClienteDialogController);

    ClienteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Cliente', 'Trabajo', 'Inscripcion'];

    function ClienteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Cliente, Trabajo, Inscripcion) {
        var vm = this;

        vm.cliente = entity;
        vm.clear = clear;
        vm.save = save;
        vm.trabajos = Trabajo.query();
        vm.inscripcions = Inscripcion.query();
		if(vm.cliente.tipodocumento==null){
			vm.cliente.tipodocumento="CEDULA";
		}
		
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.cliente.id !== null) {
                Cliente.update(vm.cliente, onSaveSuccess, onSaveError);
            } else {
                Cliente.save(vm.cliente, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:clienteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
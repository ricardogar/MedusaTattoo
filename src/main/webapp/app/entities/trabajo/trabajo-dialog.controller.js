(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TrabajoDialogController', TrabajoDialogController);

    TrabajoDialogController.$inject = ['$timeout', '$scope', '$log', '$stateParams', '$uibModalInstance', 'DataUtils','Principal', 'entity', 'Trabajo', 'Pago', 'Cita', 'Foto', 'Tatuador', 'Cliente', 'Sede'];

    function TrabajoDialogController ($timeout, $scope, $log, $stateParams, $uibModalInstance, DataUtils, Principal, entity, Trabajo, Pago, Cita, Foto, Tatuador, Cliente, Sede) {
        var vm = this;

        vm.trabajo = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.pagos = Pago.query();
        vm.citas = Cita.query();
        vm.fotos = Foto.query();
        vm.tatuadors = Tatuador.query();
        vm.clientes = Cliente.query();
        vm.sedes = Sede.query();
		vm.account = null;
		vm.isAdmin=false;
		getAccount();
		if(vm.trabajo.estado==null){
			vm.trabajo.estado="EN_PROGRESO";
		}
		if(vm.trabajo.tipo==null){
			vm.trabajo.tipo="NORMAL";
		}

		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
				if(vm.trabajo.sede==null){
					vm.trabajo.sede=vm.account.sede;
				}
				
				vm.isAdmin=vm.account.authorities.includes("ROLE_ADMIN");
				
				console.log("=================Trabajo Dialog====================");
				console.log(vm.account);
				console.log("isAdmin: ");
				console.log(vm.isAdmin);
            });
        }
		
		if(vm.trabajo.sede==null){
			getAccount();
			if(vm.account!==null){
				vm.trabajo.sede=vm.account.sede;
			}
		}
		
        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.trabajo.id !== null) {
                Trabajo.update(vm.trabajo, onSaveSuccess, onSaveError);
            } else {
                Trabajo.save(vm.trabajo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:trabajoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFoto = function ($file, trabajo) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        trabajo.foto = base64Data;
                        trabajo.fotoContentType = $file.type;
                    });
                });
            }
        };

    }
})();

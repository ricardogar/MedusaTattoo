(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TatuadorDialogController', TatuadorDialogController);

    TatuadorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Tatuador', 'Trabajo', 'Sede', 'Rayaton','Principal'];

    function TatuadorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Tatuador, Trabajo, Sede, Rayaton,Principal) {
        var vm = this;

        vm.tatuador = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.trabajos = Trabajo.query();
        vm.sedes = Sede.query();
        vm.rayatons = Rayaton.query();
		vm.account = null;
		vm.showEstado=false;
		if(vm.tatuador.tipodocumento==null){
			vm.tatuador.tipodocumento="CEDULA";
		}else{
			vm.showEstado=true;
		}
		getAccount();

		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
				vm.isAdmin=vm.account.authorities.includes("ROLE_ADMIN");
				if (vm.tatuador.sede == null) {
                    vm.tatuador.sede=vm.account.sede;
                }
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
            if (vm.tatuador.id !== null) {
                Tatuador.update(vm.tatuador, onSaveSuccess, onSaveError);
            } else {
                Tatuador.save(vm.tatuador, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:tatuadorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFoto = function ($file, tatuador) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        tatuador.foto = base64Data;
                        tatuador.fotoContentType = $file.type;
                    });
                });
            }
        };

    }
})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TrabajoDialogController', TrabajoDialogController);

    TrabajoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Trabajo',  'Tatuador', 'Cliente','Principal','OpenSedes','HasRayaton','TatuadorActivos'];

    function TrabajoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Trabajo, Tatuador, Cliente,Principal,OpenSedes,HasRayaton,TatuadorActivos) {
        var vm = this;

        vm.trabajo = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.hasRayatons = HasRayaton.get();
        vm.clientes = Cliente.query();
        vm.sedes = OpenSedes.query();
		vm.account = {};
		vm.tatuadors=TatuadorActivos.query();
		getAccount();

		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
				if(vm.trabajo.sede==null){
					vm.trabajo.sede=vm.account.sede;
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

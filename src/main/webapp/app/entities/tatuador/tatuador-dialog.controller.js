(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TatuadorDialogController', TatuadorDialogController);

    TatuadorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Tatuador', 'Trabajo', 'Rayaton'];

    function TatuadorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Tatuador, Trabajo, Rayaton) {
        var vm = this;

        vm.tatuador = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.trabajos = Trabajo.query();
        vm.rayatons = Rayaton.query();
		vm.showEstado=false;
		if(vm.tatuador.tipodocumento==null){
			vm.tatuador.tipodocumento="CEDULA";
		}else{
			vm.showEstado=true;
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

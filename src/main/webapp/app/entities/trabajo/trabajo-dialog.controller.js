(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TrabajoDialogController', TrabajoDialogController);

    TrabajoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Trabajo', 'Pago', 'Cita', 'Foto', 'Tatuador', 'Cliente', 'Sede'];

    function TrabajoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Trabajo, Pago, Cita, Foto, Tatuador, Cliente, Sede) {
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

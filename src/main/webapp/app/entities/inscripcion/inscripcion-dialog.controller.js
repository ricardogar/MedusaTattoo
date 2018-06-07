(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('InscripcionDialogController', InscripcionDialogController);

    InscripcionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Inscripcion', 'Rayaton', 'Sede','Principal','OpenSedes'];

    function InscripcionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Inscripcion, Rayaton, Sede,Principal,OpenSedes) {
        var vm = this;

        vm.inscripcion = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.rayatons = Rayaton.query();
        vm.sedes = OpenSedes.query();
		vm.account = {};
		getAccount();
		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
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
            if (vm.inscripcion.id !== null) {
                Inscripcion.update(vm.inscripcion, onSaveSuccess, onSaveError);
            } else {
                Inscripcion.save(vm.inscripcion, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:inscripcionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setImagen = function ($file, inscripcion) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        inscripcion.imagen = base64Data;
                        inscripcion.imagenContentType = $file.type;
                    });
                });
            }
        };

    }
})();

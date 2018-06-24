(function () {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('FotoDialogController', FotoDialogController);

    FotoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Foto', 'PalabraClave', 'Trabajo', 'ThumbnailService'];

    function FotoDialogController($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Foto, PalabraClave, Trabajo, ThumbnailService) {
        var vm = this;

        vm.foto = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.palabraclaves = PalabraClave.query();
        vm.trabajos = Trabajo.query();

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            if (vm.thumbWidth>0) {
                if (vm.foto.miniatura) {
                    vm.isSaving = true;
                    if (vm.foto.id !== null) {
                        Foto.update(vm.foto, onSaveSuccess, onSaveError);
                    } else {
                        Foto.save(vm.foto, onSaveSuccess, onSaveError);
                    }
                }
            }else{
                vm.getDataUrl(vm.foto.imagen)
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('medusaTattooApp:fotoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }


        vm.getSize = function () {
            vm.thumbWidth = angular.element('#miniatura').width();
        };

        vm.getDataUrl= function(origen, tipo) {
            vm.thumbWidth=$('#miniatura').width();
            if (vm.thumbWidth<=0){
                vm.getSize();
            }
            ThumbnailService.generate('data:' + tipo + ';base64,' + origen, {
                type: tipo,
                noDistortion: true,
                width: vm.thumbWidth,
                height: 220
            }).then(
                function success(data) {
                    vm.foto.miniatura = data.split(',')[1];
                },
                function error(reason) {
                    console.log(reason);
                }
            );
        };

        vm.setImagen = function ($file, foto) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function (base64Data) {
                    $scope.$apply(function () {
                        foto.imagen = base64Data;
                        foto.imagenContentType = $file.type;

                    });
                    vm.getDataUrl(vm.foto.imagen,vm.foto.imagenContentType);
                });
                vm.getDataUrl(vm.foto.imagen,vm.foto.imagenContentType);
            }
        };

    }
})();

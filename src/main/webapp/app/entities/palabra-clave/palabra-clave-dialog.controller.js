(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('PalabraClaveDialogController', PalabraClaveDialogController);

    PalabraClaveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PalabraClave', 'Foto'];

    function PalabraClaveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PalabraClave, Foto) {
        var vm = this;

        vm.palabraClave = entity;
        vm.clear = clear;
        vm.save = save;
        vm.fotos = Foto.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.palabraClave.id !== null) {
                PalabraClave.update(vm.palabraClave, onSaveSuccess, onSaveError);
            } else {
                PalabraClave.save(vm.palabraClave, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('medusaTattooApp:palabraClaveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

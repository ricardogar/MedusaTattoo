(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('UserManagementDialogController',UserManagementDialogController);

    UserManagementDialogController.$inject = ['$stateParams', '$uibModalInstance', 'entity', 'User', 'JhiLanguageService','Sede'];

    function UserManagementDialogController ($stateParams, $uibModalInstance, entity, User, JhiLanguageService,Sede) {
        var vm = this;

        vm.authorities = ["ROLE_SECRETARIA", "ROLE_ADMIN","ROLE_CLIENTE"];
        vm.clear = clear;
        vm.languages = null;
        vm.save = save;
        vm.user = entity;
		vm.sedes = Sede.query();


        JhiLanguageService.getAll().then(function (languages) {
            vm.languages = languages;
        });

        vm.setSede = function () {
            return (vm.user.authorities.includes("ROLE_ADMIN") || vm.user.authorities.includes("ROLE_SECRETARIA"))
        };

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function onSaveSuccess (result) {
            vm.isSaving = false;
            $uibModalInstance.close(result);
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        function save () {
            vm.isSaving = true;
            if (vm.user.id !== null) {
                User.update(vm.user, onSaveSuccess, onSaveError);
            } else {
                User.save(vm.user, onSaveSuccess, onSaveError);
            }
        }
    }
})();

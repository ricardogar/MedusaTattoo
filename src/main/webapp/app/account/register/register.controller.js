(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RegisterController', RegisterController);


    RegisterController.$inject = ['$translate', '$timeout', 'Auth', 'Cliente', 'LoginService', 'errorConstants'];

    function RegisterController ($translate, $timeout, Auth, Cliente, LoginService, errorConstants) {
        var vm = this;

        vm.doNotMatch = null;
        vm.error = null;
        vm.errorUserExists = null;
        vm.login = LoginService.open;
        vm.register = register;
        vm.registerAccount = {};
        vm.cliente = {};
        vm.success = null;
        vm.clientes = Cliente.query();
        console.log(vm.clientes);
        vm.registerAccount.login="";
        vm.registerAccount.firstName="";
        vm.registerAccount.lastName="";

        $timeout(function (){angular.element('#login').focus();});

        function register () {
            console.log("entro a registrar");
            if (vm.registerAccount.password !== vm.confirmPassword) {
                vm.doNotMatch = 'ERROR';
            } else {
                vm.registerAccount.langKey = $translate.use();
                vm.doNotMatch = null;
                vm.error = null;
                vm.errorUserExists = null;
                vm.errorEmailExists = null;
                vm.registerAccount.login=vm.registerAccount.email;
                vm.registerAccount.firstName=vm.cliente.nombre;
                vm.registerAccount.lastName=vm.cliente.apellido;
                console.log(vm.registerAccount);
                console.log("##########################")
                console.log(vm.cliente);
                Auth.createAccount(vm.registerAccount).then(function () {
                    console.log("registro");
                    vm.cliente.email=vm.registerAccount.email;
                    Cliente.save(vm.cliente, onSaveSuccess, onSaveError);
                    vm.success = 'OK';
                }).catch(function (response) {
                    vm.success = null;
                    console.log("error al crear cuenta desde vm.registerAccount")
                    if (response.status === 400 && angular.fromJson(response.data).type === errorConstants.LOGIN_ALREADY_USED_TYPE) {
                        vm.errorUserExists = 'ERROR';
                    } else if (response.status === 400 && angular.fromJson(response.data).type === errorConstants.EMAIL_ALREADY_USED_TYPE) {
                        vm.errorEmailExists = 'ERROR';
                    } else {
                        vm.error = 'ERROR';
                    }
                });
            }
        }
        function onSaveSuccess (result) {
            //$scope.$emit('medusaTattooApp:clienteUpdate', result);
            $('#clientRegisterInfo').text("Registro completo!");
            $('#clientRegisterInfo2').text("Porfavor revisa tu correo elctrónico para activar tu cuenta");
            vm.isSaving = false;
        }

        function onSaveError () {
            $('#clientRegisterInfo').text("Registro Fallido!");
            $('#clientRegisterInfo2').text("El email ya se encuentra en uso");
            vm.isSaving = false;
        }
    }
})();

(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RegistroClienteController', RegistroClienteController);

    RegistroClienteController.$inject = ['$translate','$timeout', '$scope','LoginService', 'Cliente','Auth','User'];

    function RegistroClienteController ($translate,$timeout, $scope,LoginService, Cliente,Auth,User) {
        var vm = this;

        vm.cliente = {};
		vm.user = {};
        vm.login = LoginService.open;
        vm.clear = clear;
        vm.save = save;


        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {

        }

        function save () {
            vm.isSaving = true;
            vm.user.login=vm.cliente.email;
            vm.user.firstName=vm.cliente.nombre;
            vm.user.lastName=vm.cliente.apellido;
            vm.user.email=vm.cliente.email;
            vm.user.sede=null;
            vm.user.imageUrl="";
            vm.user.activated=true;
            vm.user.langKey="es";
            vm.user.authorities=['ROLE_CLIENTE'];
            console.log(vm.cliente);console.log(vm.user);
            Cliente.save(vm.cliente, onSaveSuccess, onSaveError);
            User.save(vm.user, onSaveSuccess, onSaveError);



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

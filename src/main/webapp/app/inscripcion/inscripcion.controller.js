(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('InscripcionRController', InscripcionRController);

    InscripcionRController.$inject = ['$timeout', '$scope', '$stateParams', 'DataUtils', 'Inscripcion', 'Rayaton', 'Sede','Principal'];

    function InscripcionRController ($timeout, $scope, $stateParams, DataUtils, Inscripcion, Rayaton, Sede,Principal) {
        var vm = this;

        vm.inscripcion = {};
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
		vm.account = null;
		//getAccount();
        /*if(vm.inscripcion.estado==null){
            vm.inscripcion.estado="PRE_INSCRITO";
        }*/
		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAdmin=vm.account.authorities.includes("ROLE_ADMIN");
				if(vm.inscripcion.sede==null && vm.isAdmin){
					vm.inscripcion.sede=vm.account.sede;
				}
				console.log("isAdmin: ");
				console.log(vm.isAdmin);
            });
        }

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {

        }

        function save () {
            vm.isSaving = true;
			vm.inscripcion.estado="PRE_INSCRITO";
			console.log(vm.inscripcion);
                Inscripcion.save(vm.inscripcion, onSaveSuccess, onSaveError);

        }

        function onSaveSuccess (result) {
            //$scope.$emit('medusaTattooApp:inscripcionUpdate', result);
            //AlertService.success("Inscripción completa! </br> Te avisaremos si eres uno de los seleccionados");
			$('#labelInfo').text("Inscripción completa!");
			$('#labelInfo2').text("Te avisaremos si eres uno de los seleccionados");
			//alert("Inscripción completa!");
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

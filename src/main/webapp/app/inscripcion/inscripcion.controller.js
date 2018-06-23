(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('InscripcionRController', InscripcionRController);

    InscripcionRController.$inject = ['$timeout', '$scope', '$stateParams', 'DataUtils', 'Inscripcion', 'HasRayaton', 'LastRayaton','AlertService'];

    function InscripcionRController ($timeout, $scope, $stateParams, DataUtils, Inscripcion, HasRayaton, LastRayaton,AlertService) {
        var vm = this;

        vm.inscripcion = {};
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.finish=false;

        HasRayaton.get(function (data) {
            vm.hasRayatons=data[0];
            if (vm.hasRayatons==='t'){
                LastRayaton.get(function (info) {
                    vm.rayaton=info;
                })
            }
        });

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
            vm.finish=true;
        }

        function onSaveError () {
            $('#labelInfo').text("Inscripción fallida!");
            $('#labelInfo2').text("No pudimos procesar tu inscripción, por favor intentalo mas tarde");
            vm.finish=true;
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

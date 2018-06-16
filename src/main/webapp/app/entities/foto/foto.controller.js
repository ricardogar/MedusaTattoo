(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('FotoController', FotoController);

    FotoController.$inject = ['DataUtils', 'Foto', 'ParseLinks', 'AlertService', 'paginationConstants','Galeria'];

    function FotoController(DataUtils, Foto, ParseLinks, AlertService, paginationConstants,Galeria) {

        var vm = this;

        vm.fotos = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.galeria={};
        //vm.galeria=Galeria.get();
        //console.log(vm.galeria);

        loadGaleria();
        loadAll();

        function loadAll () {
            Foto.query({
                page: vm.page,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.fotos.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadGaleria () {
            Galeria.get({
                page: 0,
                size: 200
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.galeria=data;
                vm.$elasticgr = $("#elastic_grid").elastic_grid(vm.galeria);
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }



        function reset () {
            vm.page = 0;
            vm.fotos = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

        /*
        angular.element(document).ready(function () {
            console.log("cargado dom");
            vm.$elasticgr = $("#elastic_grid_demo").elastic_grid(vm.galeria);

        });
         */


    }
})();

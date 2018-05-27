(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('consultaCitaController', consultaCitaController);

    consultaCitaController.$inject = ['$scope','CitaByAccount','CitaByDocumento', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function consultaCitaController($scope,CitaByAccount,CitaByDocumento, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.citas = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.showCitas = false;
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
		vm.currentSearch="";



        //loadAll();


vm.cleanFields= function(){
    vm.currentSearch="";
    vm.showCitas=false;
    vm.citas=[];
    $('#infoLabel').text("");
};

        vm.loadAll = function() {
            vm.showCitas=false;
            vm.citas=[];
            $('#infoLabel').text("");
            CitaByDocumento.query({
                documento:vm.currentSearch,
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
                    vm.citas.push(data[i]);
                }
                if (vm.citas.length > 0) {
                    vm.showCitas=true;
                }else{
                    $('#infoLabel').text("No tienes ninguna cita asignada");
                }





            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.citas = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }

    }
})();

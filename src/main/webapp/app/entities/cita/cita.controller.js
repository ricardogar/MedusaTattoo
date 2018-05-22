(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('CitaController', CitaController);

    CitaController.$inject = ['Principal','$scope','filterSedeByAccount','Cita', 'ParseLinks', 'AlertService', 'paginationConstants'];

    function CitaController(Principal,$scope,filterSedeByAccount,Cita, ParseLinks, AlertService, paginationConstants) {

        var vm = this;

        vm.citas = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
		vm.currentSearch="";
        vm.account = null;
        getAccount()

        //loadAll();


		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                console.log(vm.account)
                loadAll();
            });
        }

        function loadAll () {
            filterSedeByAccount.query({
                id:vm.account.id,
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

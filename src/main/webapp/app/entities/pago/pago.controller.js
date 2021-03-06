(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('PagoController', PagoController);

    PagoController.$inject = ['Pago', 'ParseLinks', 'AlertService', 'paginationConstants','Principal','$scope','filterPagoByCuenta'];

    function PagoController(Pago, ParseLinks, AlertService, paginationConstants,Principal,$scope,filterPagoByCuenta) {

        var vm = this;

        vm.pagos = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = false;

        //loadAll();
		vm.account = null;
		getAccount();

		function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                loadAll();
            });
        }

        function loadAll () {
            filterPagoByCuenta.query({
                id: vm.account.id,
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
                    vm.pagos.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.pagos = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();

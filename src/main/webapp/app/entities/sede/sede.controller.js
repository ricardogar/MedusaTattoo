(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('SedeController', SedeController);

    SedeController.$inject = ['Sede', 'ParseLinks', 'AlertService', 'paginationConstants','Principal'];

    function SedeController(Sede, ParseLinks, AlertService, paginationConstants,Principal) {

        var vm = this;

        vm.sedes = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reset = reset;
        vm.reverse = true;
        getAccount()


        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                loadAll();
            });
        }

        function loadAll () {
            Sede.query({
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
                    vm.sedes.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.sedes = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();

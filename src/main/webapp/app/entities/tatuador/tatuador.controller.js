(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('TatuadorController', TatuadorController);

    TatuadorController.$inject = ['DataUtils', 'Tatuador', 'ParseLinks', 'AlertService', 'paginationConstants','Auth', 'Principal','TatuadorByCuenta'];

    function TatuadorController(DataUtils, Tatuador, ParseLinks, AlertService, paginationConstants,Auth, Principal,TatuadorByCuenta) {

        var vm = this;

        vm.tatuadors = [];
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
        getAccount();
        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                loadAll();
            });
        }
        //loadAll();

        function loadAll () {
            TatuadorByCuenta.query({
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
                    vm.tatuadors.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.tatuadors = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();

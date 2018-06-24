(function() {
    'use strict';

    angular
        .module('medusaTattooApp')
        .controller('RayatonController', RayatonController);

    RayatonController.$inject = ['DataUtils', 'Rayaton', 'ParseLinks', 'AlertService', 'paginationConstants','TrabajosByRayaton','HasRayaton'];

    function RayatonController(DataUtils, Rayaton, ParseLinks, AlertService, paginationConstants,TrabajosByRayaton,HasRayaton) {

        var vm = this;

        vm.rayatons = [];
        vm.loadPage = loadPage;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'fecha';
        HasRayaton.get(function (data) {
            vm.hasRayatons=data[0];
        },function (error) {
            console.log(error)
        });
        vm.reset = reset;
        vm.reverse = false;
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;


        loadAll();

        function loadAll () {
            Rayaton.query({
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
                    vm.rayatons.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset () {
            vm.page = 0;
            vm.rayatons = [];
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
        }
    }
})();

(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Pago', Pago)
        .factory('filterPagoByCuenta', filterPagoByCuenta);

    Pago.$inject = ['$resource', 'DateUtils'];
    filterPagoByCuenta.$inject = ['$resource', 'DateUtils'];

    function filterPagoByCuenta ($resource) {
        var resourceUrl =  'api/pagos/cuenta/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }

    function Pago ($resource, DateUtils) {
        var resourceUrl =  'api/pagos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha = DateUtils.convertDateTimeFromServer(data.fecha);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

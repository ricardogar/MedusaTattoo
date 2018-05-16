(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Pago', Pago);

    Pago.$inject = ['$resource', 'DateUtils'];

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

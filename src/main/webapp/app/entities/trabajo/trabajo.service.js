(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Trabajo', Trabajo)
        .factory('filterTrabajoByCuenta', filterTrabajoByCuenta);

    Trabajo.$inject = ['$resource'];
    filterTrabajoByCuenta.$inject = ['$resource'];

    function filterTrabajoByCuenta ($resource) {
        var resourceUrl =  'api/trabajos/cuenta/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }

    function Trabajo ($resource) {
        var resourceUrl =  'api/trabajos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

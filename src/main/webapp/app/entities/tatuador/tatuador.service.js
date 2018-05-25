(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Tatuador', Tatuador)
        .factory('TatuadorByCuenta', TatuadorByCuenta)
        .factory('TatuadorActivoByCuenta', TatuadorActivoByCuenta);

    Tatuador.$inject = ['$resource'];
    TatuadorByCuenta.$inject = ['$resource'];
    TatuadorActivoByCuenta.$inject = ['$resource'];

    function Tatuador ($resource) {
        var resourceUrl =  'api/tatuadors/:id';

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
    function TatuadorByCuenta ($resource) {
        var resourceUrl =  'api/tatuadors/cuenta/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
    function TatuadorActivoByCuenta ($resource) {
        var resourceUrl =  'api/tatuadors/activo/cuenta/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }


})();

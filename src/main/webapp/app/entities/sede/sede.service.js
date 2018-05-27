(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Sede', Sede)
        .factory('OpenSedes', OpenSedes);

    Sede.$inject = ['$resource'];
    OpenSedes.$inject = ['$resource'];

    function Sede ($resource) {
        var resourceUrl =  'api/sedes/:id';

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

    function OpenSedes ($resource) {
        var resourceUrl =  'api/sedes/abierta';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
        });
    }
})();

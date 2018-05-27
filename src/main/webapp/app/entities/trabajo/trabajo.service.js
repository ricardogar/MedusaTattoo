(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Trabajo', Trabajo)
        .factory('TrabajoByAccount', TrabajoByAccount)
        .factory('TrabajosByAccountAndStatus', TrabajosByAccountAndStatus)
        .factory('TrabajosByRayaton', TrabajosByRayaton);

    Trabajo.$inject = ['$resource'];
    TrabajoByAccount.$inject = ['$resource'];
    TrabajosByAccountAndStatus.$inject = ['$resource'];
    TrabajosByRayaton.$inject = ['$resource'];
    function TrabajoByAccount ($resource) {
        var resourceUrl =  'api/trabajos/cuenta/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
    function TrabajosByAccountAndStatus ($resource) {
        var resourceUrl =  'api/trabajos/cuenta/:id/estado/:status';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
    function TrabajosByRayaton ($resource) {
        var resourceUrl =  'api/trabajos/rayaton/:id';

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

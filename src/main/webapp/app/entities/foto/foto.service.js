(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Foto', Foto)
        .factory('Galeria', Galeria);

    Foto.$inject = ['$resource'];
    Galeria.$inject = ['$resource'];

    function Foto ($resource) {
        var resourceUrl =  'api/fotos/:id';

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
    function Galeria ($resource) {
        var resourceUrl =  'api/fotos/galeria';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            }
        });
    }

})();

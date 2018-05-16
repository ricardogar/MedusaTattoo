(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Tatuador', Tatuador);

    Tatuador.$inject = ['$resource'];

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
})();

(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Sede', Sede);

    Sede.$inject = ['$resource'];

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
})();

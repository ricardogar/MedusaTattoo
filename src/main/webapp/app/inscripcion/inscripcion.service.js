(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Inscripcion', Inscripcion);

    Inscripcion.$inject = ['$resource'];

    function Inscripcion ($resource) {
        var resourceUrl =  'api/inscripcions/:id';

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

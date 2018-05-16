(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('PalabraClave', PalabraClave);

    PalabraClave.$inject = ['$resource'];

    function PalabraClave ($resource) {
        var resourceUrl =  'api/palabra-claves/:id';

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

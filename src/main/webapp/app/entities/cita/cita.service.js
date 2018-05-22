(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Cita', Cita)
        .factory('filterSedeByAccount', filterSedeByAccount);

    Cita.$inject = ['$resource', 'DateUtils']
    filterSedeByAccount.$inject = ['$resource'];

    function Cita ($resource, DateUtils) {
        var resourceUrl =  'api/citas/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fechaYHora = DateUtils.convertDateTimeFromServer(data.fechaYHora);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
    function filterSedeByAccount ($resource) {
        var resourceUrl =  'api/citas/cuenta/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
        });
    }
})();

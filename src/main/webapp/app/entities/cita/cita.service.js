(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Cita', Cita)
        .factory('CitaByAccount', CitaByAccount)
        .factory('CitaByAccountAfterDate', CitaByAccountAfterDate)
        .factory('CitasByAccountInDate', CitasByAccountInDate)
        .factory('CitasByAccountBetweenDates', CitasByAccountBetweenDates);

    Cita.$inject = ['$resource', 'DateUtils'];
    CitaByAccount.$inject = ['$resource'];
    CitaByAccountAfterDate.$inject = ['$resource'];
    CitasByAccountInDate.$inject = ['$resource'];
    CitasByAccountBetweenDates.$inject = ['$resource'];

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
    function CitaByAccount ($resource) {
        var resourceUrl =  'api/citas/cuenta/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
        });
    }
    function CitaByAccountAfterDate ($resource) {
        var resourceUrl =  'api/citas/cuenta/:id/after/:date';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
        });
    }
    function CitasByAccountInDate ($resource) {
        var resourceUrl =  'api/citas/cuenta/:id/in/:date';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
        });
    }

    function CitasByAccountBetweenDates ($resource) {
        var resourceUrl =  'api/citas/cuenta/:id/between/:minDate/maxDate';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
        });
    }
})();

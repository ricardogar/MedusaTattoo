(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('SedesMoney', SedesMoney)
        .factory('SedesWorks', SedesWorks)
        .factory('Sedes', Sedes);

    SedesMoney.$inject = ['$resource'];
    SedesWorks.$inject = ['$resource'];
    Sedes.$inject = ['$resource', 'DateUtils'];
    function SedesMoney ($resource) {
        var resourceUrl =  'api/sedes/money/:minDate/:maxDate';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
    function SedesWorks ($resource) {
        var resourceUrl =  'api/sedes/works/:minDate/:maxDate';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }

    function Sedes ($resource, DateUtils) {
        var resourceUrl =  'api/sedes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fecha = DateUtils.convertLocalDateFromServer(data.fecha);
                    }
                    return data;
                }
            }
        });
    }

})();

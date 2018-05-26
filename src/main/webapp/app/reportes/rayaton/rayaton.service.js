(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('RayatonMoney', RayatonMoney)
        .factory('RayatonWorks', RayatonWorks)
        .factory('Rayaton', Rayaton);

    RayatonMoney.$inject = ['$resource'];
    RayatonWorks.$inject = ['$resource'];
    Rayaton.$inject = ['$resource', 'DateUtils'];
    function RayatonMoney ($resource) {
        var resourceUrl =  'api/rayatons/money/:minDate/:maxDate';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
    function RayatonWorks ($resource) {
        var resourceUrl =  'api/rayatons/works/:minDate/:maxDate';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }

    function Rayaton ($resource, DateUtils) {
        var resourceUrl =  'api/rayatons/:id';

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

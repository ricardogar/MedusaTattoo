(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('TatuadoresMoney', TatuadoresMoney)
        .factory('TatuadoresWorks', TatuadoresWorks)
        .factory('Tatuadores', Tatuadores);

    TatuadoresMoney.$inject = ['$resource'];
    TatuadoresWorks.$inject = ['$resource'];
    Tatuadores.$inject = ['$resource', 'DateUtils'];
    function TatuadoresMoney ($resource) {
        var resourceUrl =  'api/tatuadors/money/:minDate/:maxDate';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
    function TatuadoresWorks ($resource) {
        var resourceUrl =  'api/tatuadors/works/:minDate/:maxDate';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }

    function Tatuadores ($resource, DateUtils) {
        var resourceUrl =  'api/tatuadors/:id';

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

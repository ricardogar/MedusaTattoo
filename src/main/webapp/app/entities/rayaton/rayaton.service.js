(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('Rayaton', Rayaton)
        .factory('HasRayaton', HasRayaton)
        .factory('LastRayaton', LastRayaton);

    Rayaton.$inject = ['$resource', 'DateUtils'];
    HasRayaton.$inject = ['$resource'];
    LastRayaton.$inject = ['$resource'];
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
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fecha = DateUtils.convertLocalDateToServer(copy.fecha);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.fecha = DateUtils.convertLocalDateToServer(copy.fecha);
                    return angular.toJson(copy);
                }
            }
        });
    }
    function HasRayaton ($resource) {
        var resourceUrl =  'api/rayatons/exists';
        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET'
            }
        });
    }
    function LastRayaton ($resource) {
        var resourceUrl =  'api/rayatons/last';
        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET'
            }
        });
    }

})();

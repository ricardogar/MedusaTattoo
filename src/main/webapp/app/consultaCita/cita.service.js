(function() {
    'use strict';
    angular
        .module('medusaTattooApp')
        .factory('CitaByDocumento', CitaByDocumento);


    CitaByDocumento.$inject = ['$resource', 'DateUtils'];

    function CitaByDocumento ($resource, DateUtils) {
        var resourceUrl =  'api/citas/cliente/:documento';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
        });
    }

})();

(function() {
    'use strict';
    angular
        .module('isaApp')
        .factory('Visit', Visit);

    Visit.$inject = ['$resource', 'DateUtils'];

    function Visit ($resource, DateUtils) {
        var resourceUrl =  'api/visits/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateOfVisit = DateUtils.convertDateTimeFromServer(data.dateOfVisit);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

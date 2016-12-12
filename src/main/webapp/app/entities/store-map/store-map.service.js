(function() {
    'use strict';
    angular
        .module('isaApp')
        .factory('StoreMap', StoreMap);

    StoreMap.$inject = ['$resource', 'DateUtils'];

    function StoreMap ($resource, DateUtils) {
        var resourceUrl =  'api/store-maps/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.validityStart = DateUtils.convertDateTimeFromServer(data.validityStart);
                        data.validityEnd = DateUtils.convertDateTimeFromServer(data.validityEnd);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

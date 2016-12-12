(function() {
    'use strict';
    angular
        .module('isaApp')
        .factory('Waypoint', Waypoint);

    Waypoint.$inject = ['$resource'];

    function Waypoint ($resource) {
        var resourceUrl =  'api/waypoints/:id';

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

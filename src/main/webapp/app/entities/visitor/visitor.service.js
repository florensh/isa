(function() {
    'use strict';
    angular
        .module('isaApp')
        .factory('Visitor', Visitor);

    Visitor.$inject = ['$resource'];

    function Visitor ($resource) {
        var resourceUrl =  'api/visitors/:id';

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

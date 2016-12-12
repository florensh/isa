(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('WaypointDetailController', WaypointDetailController);

    WaypointDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Waypoint', 'Visit'];

    function WaypointDetailController($scope, $rootScope, $stateParams, previousState, entity, Waypoint, Visit) {
        var vm = this;

        vm.waypoint = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('isaApp:waypointUpdate', function(event, result) {
            vm.waypoint = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

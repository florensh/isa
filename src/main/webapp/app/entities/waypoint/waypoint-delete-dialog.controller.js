(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('WaypointDeleteController',WaypointDeleteController);

    WaypointDeleteController.$inject = ['$uibModalInstance', 'entity', 'Waypoint'];

    function WaypointDeleteController($uibModalInstance, entity, Waypoint) {
        var vm = this;

        vm.waypoint = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Waypoint.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

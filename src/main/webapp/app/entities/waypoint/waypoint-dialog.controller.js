(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('WaypointDialogController', WaypointDialogController);

    WaypointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Waypoint', 'Visit'];

    function WaypointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Waypoint, Visit) {
        var vm = this;

        vm.waypoint = entity;
        vm.clear = clear;
        vm.save = save;
        vm.visits = Visit.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.waypoint.id !== null) {
                Waypoint.update(vm.waypoint, onSaveSuccess, onSaveError);
            } else {
                Waypoint.save(vm.waypoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('isaApp:waypointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

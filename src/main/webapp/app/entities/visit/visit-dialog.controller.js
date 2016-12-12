(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitDialogController', VisitDialogController);

    VisitDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Visit', 'Store', 'Visitor'];

    function VisitDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Visit, Store, Visitor) {
        var vm = this;

        vm.visit = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.stores = Store.query();
        vm.visitors = Visitor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.visit.id !== null) {
                Visit.update(vm.visit, onSaveSuccess, onSaveError);
            } else {
                Visit.save(vm.visit, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('isaApp:visitUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.dateOfVisit = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

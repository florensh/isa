(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('StoreMapDialogController', StoreMapDialogController);

    StoreMapDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'StoreMap', 'Store'];

    function StoreMapDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, StoreMap, Store) {
        var vm = this;

        vm.storeMap = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.stores = Store.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.storeMap.id !== null) {
                StoreMap.update(vm.storeMap, onSaveSuccess, onSaveError);
            } else {
                StoreMap.save(vm.storeMap, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('isaApp:storeMapUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.validityStart = false;
        vm.datePickerOpenStatus.validityEnd = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();

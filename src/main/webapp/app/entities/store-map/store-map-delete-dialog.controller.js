(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('StoreMapDeleteController',StoreMapDeleteController);

    StoreMapDeleteController.$inject = ['$uibModalInstance', 'entity', 'StoreMap'];

    function StoreMapDeleteController($uibModalInstance, entity, StoreMap) {
        var vm = this;

        vm.storeMap = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StoreMap.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

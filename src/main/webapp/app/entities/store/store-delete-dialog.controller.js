(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('StoreDeleteController',StoreDeleteController);

    StoreDeleteController.$inject = ['$uibModalInstance', 'entity', 'Store'];

    function StoreDeleteController($uibModalInstance, entity, Store) {
        var vm = this;

        vm.store = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Store.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

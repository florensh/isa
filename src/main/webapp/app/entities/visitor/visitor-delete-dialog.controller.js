(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitorDeleteController',VisitorDeleteController);

    VisitorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Visitor'];

    function VisitorDeleteController($uibModalInstance, entity, Visitor) {
        var vm = this;

        vm.visitor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Visitor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

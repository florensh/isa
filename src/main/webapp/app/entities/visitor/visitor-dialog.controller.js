(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitorDialogController', VisitorDialogController);

    VisitorDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Visitor'];

    function VisitorDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Visitor) {
        var vm = this;

        vm.visitor = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.visitor.id !== null) {
                Visitor.update(vm.visitor, onSaveSuccess, onSaveError);
            } else {
                Visitor.save(vm.visitor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('isaApp:visitorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

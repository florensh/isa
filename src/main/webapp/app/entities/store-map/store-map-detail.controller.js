(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('StoreMapDetailController', StoreMapDetailController);

    StoreMapDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'StoreMap', 'Store'];

    function StoreMapDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, StoreMap, Store) {
        var vm = this;

        vm.storeMap = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('isaApp:storeMapUpdate', function(event, result) {
            vm.storeMap = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

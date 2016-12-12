(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('StoreMapDetailController', StoreMapDetailController);

    StoreMapDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StoreMap', 'Store'];

    function StoreMapDetailController($scope, $rootScope, $stateParams, previousState, entity, StoreMap, Store) {
        var vm = this;

        vm.storeMap = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('isaApp:storeMapUpdate', function(event, result) {
            vm.storeMap = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

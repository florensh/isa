(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('StoreDetailController', StoreDetailController);

    StoreDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Store'];

    function StoreDetailController($scope, $rootScope, $stateParams, previousState, entity, Store) {
        var vm = this;

        vm.store = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('isaApp:storeUpdate', function(event, result) {
            vm.store = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

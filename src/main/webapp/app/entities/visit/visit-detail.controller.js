(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitDetailController', VisitDetailController);

    VisitDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Visit', 'Store', 'Visitor'];

    function VisitDetailController($scope, $rootScope, $stateParams, previousState, entity, Visit, Store, Visitor) {
        var vm = this;

        vm.visit = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('isaApp:visitUpdate', function(event, result) {
            vm.visit = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

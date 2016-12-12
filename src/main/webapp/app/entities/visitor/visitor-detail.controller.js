(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitorDetailController', VisitorDetailController);

    VisitorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Visitor'];

    function VisitorDetailController($scope, $rootScope, $stateParams, previousState, entity, Visitor) {
        var vm = this;

        vm.visitor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('isaApp:visitorUpdate', function(event, result) {
            vm.visitor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

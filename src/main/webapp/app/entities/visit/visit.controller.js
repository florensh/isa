(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitController', VisitController);

    VisitController.$inject = ['$scope', '$state', 'Visit', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'leafletData'];

    function VisitController ($scope, $state, Visit, ParseLinks, AlertService, paginationConstants, pagingParams, leafletData) {

      angular.extend($scope, {
        center: {
          autoDiscover: true
        },
        overlays: {
          search: {
            name: 'search',
            type: 'group',
            visible: true,
            layerParams: {
              showOnSelector: false
            }
          }
        }
      });

      leafletData.getLayers().then(function(baselayers) {
        console.log(baselayers.overlays.search);
        angular.extend($scope.controls, {
          search: {
            layer: baselayers.overlays.search
          }
        });
      });

      leafletData.getMap().then(function(map) {
        map.addControl(new L.Control.Search({
          url: 'http://nominatim.openstreetmap.org/search?format=json&q={s}',
          jsonpParam: 'json_callback',
          propertyName: 'display_name',
          propertyLoc: ['lat', 'lon'],
          circleLocation: false,
          markerLocation: false,
          autoType: false,
          autoCollapse: true,
          minLength: 2,
          zoom: 10
        }));
      });




        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        loadAll();

        function loadAll () {
            Visit.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.visits = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();

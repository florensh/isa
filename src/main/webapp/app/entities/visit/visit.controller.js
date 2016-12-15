(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitController', VisitController);

    VisitController.$inject = ['$scope', '$state', 'Visit', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'leafletData', 'leafletBoundsHelpers'];

    function VisitController ($scope, $state, Visit, ParseLinks, AlertService, paginationConstants, pagingParams, leafletData, leafletBoundsHelpers) {





      var maxBounds = leafletBoundsHelpers.createBoundsFromArray([[0, 0], [597, 793]]);
      angular.extend($scope, {
          defaults: {
            scrollWheelZoom: false,
            crs: 'Simple',
            maxZoom: 1,
            minZoom: 0
          },
          center: {
              lat: 0,
              lng: 0,
              zoom: 0
          },
          maxBounds: maxBounds,
          layers: {
              baselayers: {
                  sanfrancisco: {
                      name: 'Supermarket',
                      type: 'imageOverlay',
                      url: 'content/images/plan3.png',
                      bounds: [[0, 0], [597, 793]],
                      layerParams: {
                          showOnSelector: false,
                          noWrap: true,
                          // attribution: 'Sample'
                      }
                  }
              },
          },
          paths: {
            p1: {
                color: 'blue',
                weight: 4,
                opacity: 0.2,
                latlngs: [
                    { lat: 580, lng: 600 },
                    { lat: 470, lng: 550 },
                    { lat: 420, lng: 300 },
                    { lat: 420, lng: 50 },
                    { lat: 210, lng: 50 },
                    { lat: 210, lng: 450 },
                    { lat: 50, lng: 450 }
                ],
                message: "Customer 1",
            }
          },
          markers:  [{
                        "lat": 420,
                        "lng": 300,
                        "message": "staying for <strong>5s</strong>"
                    }, {
                        "lat": 210,
                        "lng": 200,
                        "message": "staying for <strong>7s</strong>"
                }]
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

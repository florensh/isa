(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitController', VisitController);

    VisitController.$inject = ['$scope', '$state', 'Visit', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'leafletData', 'leafletBoundsHelpers', '_'];

    function VisitController($scope, $state, Visit, ParseLinks, AlertService, paginationConstants, pagingParams, leafletData, leafletBoundsHelpers) {

        var vm = this;


        var maxBounds = leafletBoundsHelpers.createBoundsFromArray([
            [0, 0],
            [1166, 981]
        ]);
        angular.extend($scope, {
            defaults: {
                scrollWheelZoom: false,
                crs: 'Simple',
                maxZoom: 1,
                minZoom: -2
            },
            center: {
                lat: 0,
                lng: 0,
                zoom: -1
            },
            maxBounds: maxBounds,
            layers: {
                baselayers: {
                    sanfrancisco: {
                        name: 'Supermarket',
                        type: 'imageOverlay',
                        url: 'content/images/daheim_eg.jpg',
                        bounds: [
                            [0, 0],
                            [1166, 981]
                        ],
                        layerParams: {
                            showOnSelector: false,
                            noWrap: true,
                            // attribution: 'Sample'
                        }
                    }
                },
            },
            paths: [],
            // paths: {
            //   p1: {
            //       color: 'blue',
            //       weight: 10,
            //       // stroke: true,
            //       dashArray: "15, 2",
            //       opacity: 0.1,
            //       lineCap: "butt",
            //       // lineJoin: "bevel",
            //       latlngs: [
            //           { lat: 50, lng: 480 },
            //           { lat: 200, lng: 480 },
            //           { lat: 200, lng: 300 },
            //           { lat: 150, lng: 200 }
            //       ],
            //       message: "<h4>Path 1<br><small>23min</small></h4>",
            //   }
            // },
            markers: [{
                    "lat": 150,
                    "lng": 200,
                    "message": "staying for <strong>5s</strong>"
                }
                // ,{
                //         "lat": 210,
                //         "lng": 450,
                //         "message": "staying for <strong>5s</strong>",
                //         "icon": {
                //             "type": 'awesomeMarker',
                //             "icon": 'hourglass',
                //             "markerColor": 'blue'
                //         }
                //     }

            ]
        });





        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.activePaths = {
            ids: {}
        }

        function createPath(visit) {
            return {
                name: visit.id,
                color: 'blue',
                weight: 10,
                // stroke: true,
                dashArray: "15, 2",
                opacity: 0.1,
                lineCap: "butt",
                // lineJoin: "bevel",
                latlngs: _.map(visit.waypoints, function(wp) {
                    return {
                        lat: wp.y,
                        lng: wp.x
                    }
                }),
                message: "<h4>Path 1<br><small>23min</small></h4>",
            }
        }

        vm.refreshMap = function() {
            $scope.paths =
                _.chain(vm.visits)
                .filter(function(v) {
                    return _.get(vm.activePaths.ids, v.id);
                }).map(createPath)
                .keyBy('name')
                .value()

        }

        vm.setActive = function(visit) {
            vm.activePaths
        }

        loadAll();

        function loadAll() {
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

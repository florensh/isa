(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('VisitController', VisitController);

    VisitController.$inject = ['$scope', '$filter', '$state', 'Visit', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'leafletData', 'leafletBoundsHelpers', '_'];

    function VisitController($scope, $filter, $state, Visit, ParseLinks, AlertService, paginationConstants, pagingParams, leafletData, leafletBoundsHelpers, uibDateParser) {

        var vm = this;
        $scope.format = 'yyyy/MM/dd';
        $scope.date = new Date();

        var maxBounds = leafletBoundsHelpers.createBoundsFromArray([
            [0, 0],
            [619, 421]
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
                        url: 'content/images/bibWalls.svg',
                        bounds: [
                            [0, 0],
                            [619, 421]
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
            //       weight: 6,
            //       // stroke: true,
            //       dashArray: "8, 3",
            //       opacity: 0.2,
            //       lineCap: "butt",
            //       // lineJoin: "bevel",
            //       latlngs: [
            //           { lat: 7.121740206626825*(501/25.0805), lng: 22.822013488580314*(501/25.0805) },
            //           { lat: 11.380675488673868*(501/25.0805), lng: 15.395472337919532*(501/25.0805) },
            //           { lat: 18.358690730650693*(501/25.0805), lng: 10.621956482328699*(501/25.0805) }
            //       ],
            //       message: "<h4>Path 1<br><small>23min</small></h4>",
            //   }
            // },
            markers: []
                // markers: [
                //     // {
                //     //       "lat": 150,
                //     //       "lng": 200,
                //     //       "message": "staying for <strong>5s</strong>"
                //     //   },
                //     {
                //         "lat": 150,
                //         "lng": 210,
                //         "message": "staying for <strong>5s</strong>",
                //         "icon": {
                //             "type": 'awesomeMarker',
                //             "icon": 'hourglass',
                //             "markerColor": 'blue'
                //         }
                //     }
                //
                // ]
        });





        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.activePaths = {
            ids: {}
        }

        function createMarker(visit) {
            var start = _.chain(visit.waypoints).orderBy(['timestamp'], ['asc']).head().value()
            var end = _.chain(visit.waypoints).orderBy(['timestamp'], ['asc']).last().value()

            var startEnd = [{
                "lat": start.y * 16.7859492434,
                "lng": start.x * 16.7859492434,
                "message": "Start",
                "icon": {
                    "type": 'awesomeMarker',
                    "icon": 'record',
                    "markerColor": 'blue'
                }
            }, {
                "lat": end.y * 16.7859492434,
                "lng": end.x * 16.7859492434,
                "message": "End",
                "icon": {
                    "type": 'awesomeMarker',
                    "icon": 'log-out',
                    "markerColor": 'blue'
                }
            }]

            var wpArray = _.orderBy(visit.waypoints, ['timestamp'], ['asc'])

            var staying = []
            for (var i = 1; i < wpArray.length - 1; i++) {
                var wp1 = wpArray[i]
                var wp2 = wpArray[i + 1]
                if (wp2.timestamp - wp1.timestamp > 5000) {
                    var sec = (wp2.timestamp - wp1.timestamp) / 1000
                    staying.push({
                        "lat": wp1.y * 16.7859492434,
                        "lng": wp1.x * 16.7859492434,
                        "message": "<h5>Staying<br><small>" + $filter('number')(sec, 0) + " seconds</small></h5>",
                        "icon": {
                            "type": 'awesomeMarker',
                            "icon": 'hourglass',
                            "markerColor": 'purple'
                        }
                    })
                }
            }

            return _.concat(startEnd, staying)


        }

        function createPath(visit) {
            return {
                name: visit.id,
                color: 'blue',
                weight: 4,
                // stroke: true,
                dashArray: "5, 2",
                opacity: 0.4,
                lineCap: "butt",
                // lineJoin: "bevel",
                latlngs: _.chain(visit.waypoints).orderBy(['timestamp'], ['asc']).map(function(wp) {
                    return {
                        lat: wp.y * 16.7859492434,
                        lng: wp.x * 16.7859492434
                    }
                }).value(),
                message: "<h4>Path " + visit.id + "<br><small>" + visit.duration + "</small></h4>",
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

            $scope.markers =
                _.chain(vm.visits)
                .filter(function(v) {
                    return _.get(vm.activePaths.ids, v.id);
                }).map(createMarker)
                .flattenDeep()
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
                var visitsWithDuration = _.chain(data).map(function(v) {
                    var start = _.chain(v.waypoints).orderBy(['timestamp'], ['asc']).head().value()
                    var end = _.chain(v.waypoints).orderBy(['timestamp'], ['asc']).last().value()
                    var duration = (end.timestamp / 1000 - start.timestamp / 1000) / 60
                    var sec = $filter('number')((end.timestamp / 1000 - start.timestamp / 1000) % 60, 0)
                    return _.defaults(v, {
                        'duration': $filter('number')(duration, 0) + ' min ' + sec + ' sec'
                    })
                }).value()


                vm.visits = visitsWithDuration;
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

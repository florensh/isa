(function() {
    'use strict';

    angular
        .module('isaApp')
        .controller('LeafletMapsController', LeafletMapsController);

    LeafletMapsController.$inject = ['$scope', 'leafletData', 'leafletBoundsHelpers', 'Waypoint', '_'];

    function LeafletMapsController($scope, leafletData, leafletBoundsHelpers, Waypoint) {
        var vm = this;
        vm.waypoints = [];
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.predicate = 'id';
        vm.reverse = true;

        $scope.radioModel = 'Morning';

        $scope.$watchCollection('radioModel', function() {
            $scope.loadHeatMap()
        })

        $scope.loadHeatMap = function() {
            $scope.layers.overlays.heat.data = vm.data
            $scope.layers.overlays.heat.doRefresh = true;
        }


        vm.data = [
            // [7 * (619 / 36.9224), 22 * (619 / 36.9224), 0.2],
            // [440, 440, 0.2],
            // [430, 430, 0.2],
            // [430, 430, 0.2],
            // [210, 50, 0.1],
            // [210, 70, 0.1],
            // [210, 90, 0.1]
        ];
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
                overlays: {
                    heat: {
                        name: 'Heat Map',
                        type: 'heat',
                        data: vm.data,
                        layerOptions: {
                            radius: 15,
                            blur: 10,
                            max: 2.0,
                            maxZoom: 1
                        },
                        layerParams: {
                            showOnSelector: false
                        },
                        visible: true
                    }
                }
            }
        });

        loadAll();

        var data = []

        function loadAll() {
            Waypoint.query({
                page: vm.page,
                size: 1000,
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
                vm.data = []
                for (var i = 0; i < data.length; i++) {
                    vm.waypoints.push(data[i]);


                }
                // vm.data.push(_.chain(vm.waypoints).map(function(w) {
                //     console.log(w.x);
                //     return [w.y* 16.7859492434, w.x* 16.7859492434, 0.2]
                // }).value())


                _.forEach(vm.waypoints, function(w) {
                    vm.data.push([w.y * 16.7859492434, w.x * 16.7859492434, 0.1])
                })

                // $scope.layers.overlays.heat.data = vm.data
                // console.log($scope.layers.overlays.heat.data)
                // $scope.layers.overlays.heat.doRefresh = true;
                // console.log($scope.layers.overlays.heat.data)


            }

            function onError(error) {
                // AlertService.error(error.data.message);
            }

        }

        var maxBounds = leafletBoundsHelpers.createBoundsFromArray([
            [0, 0],
            [619, 421]
        ]);

    }
})();

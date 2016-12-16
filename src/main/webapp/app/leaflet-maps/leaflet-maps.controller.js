(function() {
    'use strict';

    angular
      .module('isaApp')
      .controller('LeafletMapsController', LeafletMapsController);

    LeafletMapsController.$inject = ['$scope', 'leafletData', 'leafletBoundsHelpers'];

    function LeafletMapsController($scope, leafletData,leafletBoundsHelpers) {

var data = [ [450, 450, 0.2],
[440, 440, 0.2],
[430, 430, 0.2],
[430, 430, 0.2],
    [210, 50, 0.1],
  [210, 70, 0.1],
[210, 90, 0.1]];

            var maxBounds = leafletBoundsHelpers.createBoundsFromArray([[0, 0], [1166, 981]]);
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
                            bounds: [[0, 0], [1166, 981]],
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
                        data: data,
                        layerOptions: {
                            radius: 20,
                            blur: 10
                        },
                        layerParams: {
                          showOnSelector: false
                        },
                        visible: true
                    }
                    }
                }
            });
    }
  })();

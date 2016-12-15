(function() {
    'use strict';

    angular
      .module('isaApp')
      .controller('LeafletMapsController', LeafletMapsController);

    LeafletMapsController.$inject = ['$scope', 'leafletData', 'leafletBoundsHelpers'];

    function LeafletMapsController($scope, leafletData,leafletBoundsHelpers) {



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
    }
  })();

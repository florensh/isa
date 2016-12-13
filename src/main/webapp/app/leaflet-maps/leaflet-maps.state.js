(function() {
  'use strict';

  angular
    .module('isaApp')
    .config(stateConfig);

  stateConfig.$inject = ['$stateProvider'];

  function stateConfig($stateProvider) {
    $stateProvider.state('store-detail.leaflet-maps', {
      parent: 'store-detail',
      url: '/heatmap',
      data: {
        authorities: [],
        pageTitle: 'Heatmap'
      },
      views: {
        'submenu': {
          templateUrl: 'app/leaflet-maps/leaflet-maps.html',
          controller: 'LeafletMapsController',
          controllerAs: 'vm'
        }
      },
      resolve: {}
    });
  }
})();

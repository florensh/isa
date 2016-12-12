(function() {
    'use strict';

    angular
        .module('isaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('store-map', {
            parent: 'entity',
            url: '/store-map?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StoreMaps'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/store-map/store-maps.html',
                    controller: 'StoreMapController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }]
            }
        })
        .state('store-map-detail', {
            parent: 'entity',
            url: '/store-map/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'StoreMap'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/store-map/store-map-detail.html',
                    controller: 'StoreMapDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'StoreMap', function($stateParams, StoreMap) {
                    return StoreMap.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'store-map',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('store-map-detail.edit', {
            parent: 'store-map-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-map/store-map-dialog.html',
                    controller: 'StoreMapDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StoreMap', function(StoreMap) {
                            return StoreMap.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('store-map.new', {
            parent: 'store-map',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-map/store-map-dialog.html',
                    controller: 'StoreMapDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                validityStart: null,
                                validityEnd: null,
                                url: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('store-map', null, { reload: 'store-map' });
                }, function() {
                    $state.go('store-map');
                });
            }]
        })
        .state('store-map.edit', {
            parent: 'store-map',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-map/store-map-dialog.html',
                    controller: 'StoreMapDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StoreMap', function(StoreMap) {
                            return StoreMap.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('store-map', null, { reload: 'store-map' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('store-map.delete', {
            parent: 'store-map',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store-map/store-map-delete-dialog.html',
                    controller: 'StoreMapDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StoreMap', function(StoreMap) {
                            return StoreMap.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('store-map', null, { reload: 'store-map' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

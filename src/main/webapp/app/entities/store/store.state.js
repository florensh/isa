(function() {
    'use strict';

    angular
        .module('isaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('store', {
            parent: 'entity',
            url: '/store?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Stores'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/store/stores.html',
                    controller: 'StoreController',
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
        .state('store-detail', {
            parent: 'entity',
            url: '/store/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Store'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/store/store-detail.html',
                    controller: 'StoreDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Store', function($stateParams, Store) {
                    return Store.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'store',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('store-detail.edit', {
            parent: 'store-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store/store-dialog.html',
                    controller: 'StoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Store', function(Store) {
                            return Store.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('store.new', {
            parent: 'store',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store/store-dialog.html',
                    controller: 'StoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                street: null,
                                city: null,
                                zip: null,
                                country: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('store', null, { reload: 'store' });
                }, function() {
                    $state.go('store');
                });
            }]
        })
        .state('store.edit', {
            parent: 'store',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store/store-dialog.html',
                    controller: 'StoreDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Store', function(Store) {
                            return Store.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('store', null, { reload: 'store' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('store.delete', {
            parent: 'store',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/store/store-delete-dialog.html',
                    controller: 'StoreDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Store', function(Store) {
                            return Store.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('store', null, { reload: 'store' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

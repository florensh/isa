(function() {
    'use strict';

    angular
        .module('isaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('store-detail.visit', {
            parent: 'store-detail',
            url: '/visit?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Visits'
            },
            views: {
                'submenu': {
                    templateUrl: 'app/entities/visit/visits.html',
                    controller: 'VisitController',
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
        .state('store-detail.visit-detail', {
            parent: 'entity',
            url: '/visit/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Visit'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/visit/visit-detail.html',
                    controller: 'VisitDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Visit', function($stateParams, Visit) {
                    return Visit.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'visit',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('visit-detail.edit', {
            parent: 'visit-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/visit/visit-dialog.html',
                    controller: 'VisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Visit', function(Visit) {
                            return Visit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('store-detail.visit.new', {
            parent: 'store-detail.visit',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/visit/visit-dialog.html',
                    controller: 'VisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateOfVisit: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('visit', null, { reload: 'visit' });
                }, function() {
                    $state.go('visit');
                });
            }]
        })
        .state('visit.edit', {
            parent: 'visit',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/visit/visit-dialog.html',
                    controller: 'VisitDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Visit', function(Visit) {
                            return Visit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('visit', null, { reload: 'visit' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('visit.delete', {
            parent: 'visit',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/visit/visit-delete-dialog.html',
                    controller: 'VisitDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Visit', function(Visit) {
                            return Visit.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('visit', null, { reload: 'visit' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

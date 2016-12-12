(function() {
    'use strict';

    angular
        .module('isaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('visitor', {
            parent: 'entity',
            url: '/visitor?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Visitors'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/visitor/visitors.html',
                    controller: 'VisitorController',
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
        .state('visitor-detail', {
            parent: 'entity',
            url: '/visitor/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Visitor'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/visitor/visitor-detail.html',
                    controller: 'VisitorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Visitor', function($stateParams, Visitor) {
                    return Visitor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'visitor',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('visitor-detail.edit', {
            parent: 'visitor-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/visitor/visitor-dialog.html',
                    controller: 'VisitorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Visitor', function(Visitor) {
                            return Visitor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('visitor.new', {
            parent: 'visitor',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/visitor/visitor-dialog.html',
                    controller: 'VisitorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('visitor', null, { reload: 'visitor' });
                }, function() {
                    $state.go('visitor');
                });
            }]
        })
        .state('visitor.edit', {
            parent: 'visitor',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/visitor/visitor-dialog.html',
                    controller: 'VisitorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Visitor', function(Visitor) {
                            return Visitor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('visitor', null, { reload: 'visitor' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('visitor.delete', {
            parent: 'visitor',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/visitor/visitor-delete-dialog.html',
                    controller: 'VisitorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Visitor', function(Visitor) {
                            return Visitor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('visitor', null, { reload: 'visitor' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

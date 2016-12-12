(function() {
    'use strict';

    angular
        .module('isaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('waypoint', {
            parent: 'entity',
            url: '/waypoint',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Waypoints'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/waypoint/waypoints.html',
                    controller: 'WaypointController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('waypoint-detail', {
            parent: 'entity',
            url: '/waypoint/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Waypoint'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/waypoint/waypoint-detail.html',
                    controller: 'WaypointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Waypoint', function($stateParams, Waypoint) {
                    return Waypoint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'waypoint',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('waypoint-detail.edit', {
            parent: 'waypoint-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/waypoint/waypoint-dialog.html',
                    controller: 'WaypointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Waypoint', function(Waypoint) {
                            return Waypoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('waypoint.new', {
            parent: 'waypoint',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/waypoint/waypoint-dialog.html',
                    controller: 'WaypointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                x: null,
                                y: null,
                                timestamp: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('waypoint', null, { reload: 'waypoint' });
                }, function() {
                    $state.go('waypoint');
                });
            }]
        })
        .state('waypoint.edit', {
            parent: 'waypoint',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/waypoint/waypoint-dialog.html',
                    controller: 'WaypointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Waypoint', function(Waypoint) {
                            return Waypoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('waypoint', null, { reload: 'waypoint' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('waypoint.delete', {
            parent: 'waypoint',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/waypoint/waypoint-delete-dialog.html',
                    controller: 'WaypointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Waypoint', function(Waypoint) {
                            return Waypoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('waypoint', null, { reload: 'waypoint' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

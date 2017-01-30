(function() {
    'use strict';

    angular
        .module('callmeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('call-me-config', {
            parent: 'entity',
            url: '/call-me-config?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'callmeApp.callMeConfig.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/call-me-config/call-me-configs.html',
                    controller: 'CallMeConfigController',
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
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('callMeConfig');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('call-me-config-detail', {
            parent: 'entity',
            url: '/call-me-config/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'callmeApp.callMeConfig.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/call-me-config/call-me-config-detail.html',
                    controller: 'CallMeConfigDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('callMeConfig');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CallMeConfig', function($stateParams, CallMeConfig) {
                    return CallMeConfig.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'call-me-config',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('call-me-config-detail.edit', {
            parent: 'call-me-config-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/call-me-config/call-me-config-dialog.html',
                    controller: 'CallMeConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CallMeConfig', function(CallMeConfig) {
                            return CallMeConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('call-me-config.new', {
            parent: 'call-me-config',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/call-me-config/call-me-config-dialog.html',
                    controller: 'CallMeConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                appKey: null,
                                name: null,
                                appId: null,
                                devKey: null,
                                referrers: null,
                                emailTo: null,
                                emailFrom: null,
                                emailSubject: null,
                                smsTo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('call-me-config', null, { reload: 'call-me-config' });
                }, function() {
                    $state.go('call-me-config');
                });
            }]
        })
        .state('call-me-config.edit', {
            parent: 'call-me-config',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/call-me-config/call-me-config-dialog.html',
                    controller: 'CallMeConfigDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CallMeConfig', function(CallMeConfig) {
                            return CallMeConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('call-me-config', null, { reload: 'call-me-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('call-me-config-demo', {
                parent: 'call-me-config',
                url: "/demo/{id}",
                onEnter: ['$stateParams', '$state', '$window', function($stateParams, $state, $window) {
                    $window.open('/demo/' + $stateParams.id , '_blank');
                    $state.go('call-me-config', null, { reload: 'call-me-config' });
                }],
                data: {
                    authorities: ['ROLE_USER']
                }
            })
        .state('call-me-config-demo2', {
                parent: 'call-me-config',
                url: "/demo2/{id}",
                onEnter: ['$stateParams', '$state', '$window', function($stateParams, $state, $window) {
                    $window.open('/demo2/' + $stateParams.id , '_blank');
                    $state.go('call-me-config', null, { reload: 'call-me-config' });
                }],
                data: {
                    authorities: ['ROLE_USER']
                }
            })
        .state('call-me-config.delete', {
            parent: 'call-me-config',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/call-me-config/call-me-config-delete-dialog.html',
                    controller: 'CallMeConfigDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CallMeConfig', function(CallMeConfig) {
                            return CallMeConfig.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('call-me-config', null, { reload: 'call-me-config' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();

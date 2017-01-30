(function() {
    'use strict';

    angular
        .module('callmeApp')
        .controller('CallMeConfigDetailController', CallMeConfigDetailController);

    CallMeConfigDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CallMeConfig', 'User'];

    function CallMeConfigDetailController($scope, $rootScope, $stateParams, previousState, entity, CallMeConfig, User) {
        var vm = this;

        vm.callMeConfig = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('callmeApp:callMeConfigUpdate', function(event, result) {
            vm.callMeConfig = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();

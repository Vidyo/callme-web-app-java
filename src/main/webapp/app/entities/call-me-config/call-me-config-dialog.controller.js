(function() {
    'use strict';

    angular
        .module('callmeApp')
        .controller('CallMeConfigDialogController', CallMeConfigDialogController);

    CallMeConfigDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CallMeConfig', 'User'];

    function CallMeConfigDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CallMeConfig, User) {
        var vm = this;

        vm.callMeConfig = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(0)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.callMeConfig.id !== null) {
                CallMeConfig.update(vm.callMeConfig, onSaveSuccess, onSaveError);
            } else {
                CallMeConfig.save(vm.callMeConfig, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('callmeApp:callMeConfigUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();

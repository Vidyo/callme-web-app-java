(function() {
    'use strict';

    angular
        .module('callmeApp')
        .controller('CallMeConfigDeleteController',CallMeConfigDeleteController);

    CallMeConfigDeleteController.$inject = ['$uibModalInstance', 'entity', 'CallMeConfig'];

    function CallMeConfigDeleteController($uibModalInstance, entity, CallMeConfig) {
        var vm = this;

        vm.callMeConfig = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CallMeConfig.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();

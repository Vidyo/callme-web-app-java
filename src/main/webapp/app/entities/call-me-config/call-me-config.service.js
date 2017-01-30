(function() {
    'use strict';
    angular
        .module('callmeApp')
        .factory('CallMeConfig', CallMeConfig);

    CallMeConfig.$inject = ['$resource'];

    function CallMeConfig ($resource) {
        var resourceUrl =  'api/call-me-configs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();

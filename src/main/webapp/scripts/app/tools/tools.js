'use strict';

angular.module('hganalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tools', {
                abstract: true,
                parent: 'site'
            });
    });

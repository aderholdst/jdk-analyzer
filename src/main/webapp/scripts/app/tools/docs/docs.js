'use strict';

angular.module('hganalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('docs', {
                parent: 'tools',
                url: '/docs',
                data: {
                    pageTitle: 'API'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/tools/docs/docs.html'
                    }
                }
            });
    });

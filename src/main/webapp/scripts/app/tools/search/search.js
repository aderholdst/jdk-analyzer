'use strict';

angular.module('hganalyzerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('search', {
                parent: 'tools',
                url: '/search',
                data: {
                    pageTitle: 'Suche'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/tools/search/search.html',
                        controller: 'SearchController as vm'
                    }
                },
                resolve: {
                    
                }
            });
    });

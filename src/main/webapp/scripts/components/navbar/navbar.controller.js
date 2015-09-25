'use strict';

angular.module('hganalyzerApp')
    .controller('NavbarController', function ($scope, $location, $state, ENV) {
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';
    });

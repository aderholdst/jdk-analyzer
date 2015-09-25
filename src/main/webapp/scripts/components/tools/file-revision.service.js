'use strict';

angular.module('hganalyzerApp')
    .service('FileRevision', function (esFactory) {
        return esFactory({
            host: 'http://localhost:9200/'
        });
    });

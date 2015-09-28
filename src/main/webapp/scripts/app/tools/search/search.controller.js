'use strict';

angular
		.module('hganalyzerApp')
		.controller(
				'SearchController',
				function($scope, FileRevision) {

					const
					index_cc_results = 'file-revision';
					var vm = this;

					vm.query = {
						searchText : '*'
					}

					vm.editorOptions = {
						lineWrapping : true,
						lineNumbers : true,
						readOnly : 'nocursor',
						mode : 'text/x-java'
					};

					vm.search = search;
					vm.clearCriteria = clearCriteria;
					vm.focusCriteria = focusCriteria;

					activate();

					// //////////

					function activate() {
						search();
					}

					function search() {
						var currentQuery = vm.query;
						vm.elasticQuery =  buildQuery(vm.query);
						FileRevision.search({
							index : index_cc_results,
							size : 40,
							body : {
								"query" : vm.elasticQuery,
								"sort" : [ {
									"date" : {
										"order" : "desc"
									}
								} ]
							}
						}).then(function(resp) {
							// only use the results, if the query hasn't changed
							// in the
							// meantime since request
							// to prevent race conditions
							if (angular.equals(currentQuery, vm.query)) {
								vm.fileRevisions = resp.hits.hits;
							}
						}, function(err) {
							console.trace(err.message);
						});
					}

					function buildQuery(jsonQuery) {
						var query = {
							bool : {
								must : [],
								should : [
										{
											"query_string" : {
												"default_field" : "file-revision.content",
												"query" : jsonQuery.searchText
											}
										},
										{
											"query_string" : {
												"default_field" : "file-revision.changes.content",
												"query" : jsonQuery.searchText
											}
										} ]
							}
						};

						if (jsonQuery.path) {
							query.bool.must.push({
								"term" : {
									"file-revision.path" : jsonQuery.path
								}
							})
						}
						if (jsonQuery.comment) {
							var tokens = jsonQuery.comment.split(' ');
							vm.commentTokens = tokens;
							tokens.forEach(function(token){
								query.bool.must.push({
									"wildcard" : {
										"comment" : token.toLowerCase() + '*'
								}});
							});
						}
						if (jsonQuery.changesetRevision) {
							query.bool.must
									.push({
										"wildcard" : {
											"file-revision.changesetRevision" : jsonQuery.changesetRevision
										}
									})
						}
						return query;
					}

					function clearCriteria(field) {
						vm.query[field] = '';
						search();
					}

					function focusCriteria(field, value, resetText) {
						vm.query[field] = value;
						if (resetText) {
							vm.query.searchText = '*';
						}
						search();
					}

				});

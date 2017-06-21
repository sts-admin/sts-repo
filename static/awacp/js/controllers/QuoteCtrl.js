(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('QuoteCtrl', QuoteCtrl);
	QuoteCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService', 'FileService'];
	function QuoteCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService, FileService){
		var qVm = this;
		qVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		qVm.action = "Add";
		$scope.timers = [];
		qVm.totalItems = -1;
		qVm.currentPage = 1;
		qVm.pageNumber = 1;
		qVm.pageSize = 20;
		qVm.newQuotes = [];
		qVm.quotes = [];
		
		qVm.selectedNewQuote = {};
		qVm.selectedTakeoff = {};
		qVm.takeoffGcs  = [];
		qVm.takeoffBidders = [];
		qVm.selectedQuote = {};
		qVm.worksheetPdfView = function(pdfViewWorksheetId){
			//Add authentication headers as params
			var accessToken = StoreService.getAccessToken();
			//Add authentication headers in URL
			var url = $rootScope.base + '/awacp/generatePdfUrl/'+pdfViewWorksheetId+'?'+Math.random();
			$http({
				url : url,
				method : 'GET',
				headers : {
					'Authorization' : 'Bearer ' + accessToken,
					'Accept' : 'application/json'
				}
			}).success(function(data){
				$window.open(data.fileUrl);
			}).error(function(error){
				alert("Unable to generate PDF View, reason: "+ JSON.stringify(error, null, 4));
			});
		}
		
		qVm.showFileListingView = function(source, sourceId, title, size){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size);
		}
		
		qVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("NEW_QUOTES", size, function(status, size){
				if("success" === status){
					qVm.pageSize = size;
					qVm.pageChanged();
				}
			});
		}
		
		qVm.getPageSize = function(){
			AjaxUtil.getPageSize("NEW_QUOTES", function(status, size){
				if("success" === status){
					qVm.pageSize = size;
				}
			});
		}
		qVm.pageChanged = function() {
			qVm.listQuotes();
		};		
		qVm.cancelQuoteAction = function(){
			$state.go("quotes");
		}
		qVm.showNewQuoteInfo = function(quote){
			qVm.openInfoBox = true;
			qVm.selectedNewQuote =  quote;
		}
		qVm.newQuotePopover = {
			templateUrl: 'templates/new-quote-info.html',
			title: 'New Quote Detail'
		};
		qVm.showQuoteInfo = function(quote){
			qVm.openInfoBox = true;
			qVm.selectedQuote =  quote;
		}
		qVm.quotePopover = {
			templateUrl: 'templates/quote-info.html',
			title: 'Quote Detail'
		};
		qVm.showTakeoffInfo = function(takeoff){
			qVm.openInfoBox = true;
			qVm.selectedTakeoff =  takeoff;
		}
		qVm.takeoffPopover = {
			templateUrl: 'templates/quote-takeoff-info.html',
			title: 'Takeoff Detail'
		};
		
		qVm.GcsPopover = {
			templateUrl: 'templates/quote-gc-list.html',
			title: 'General Contractor(s)'
		};
		qVm.listGcsByTakeoff = function(takeoff){
			if(takeoff.generalContractors){
				qVm.takeoffGcs = takeoff.generalContractors;
			}else{
				qVm.takeoffGcs  = null;
			}
		}
		qVm.bidderPopover = {
			templateUrl: 'templates/quote-bidder-list.html',
			title: 'Bidder(s)'
		};
		qVm.listBiddersByTakeoff = function(takeoff){
			qVm.takeoffBidders = [];
			if(takeoff.bidders){
				qVm.takeoffBidders = takeoff.bidders;
			}else{
				qVm.takeoffBidders = null;
			}
		}
		
		qVm.makeQuote = function(id){
			AlertService.showConfirm(	'AWACP :: Confirmation!', "Are you sure to make quote?")
			.then(function (){
				AjaxUtil.getData("/awacp/makeQuote/"+id, Math.random())
				.success(function(data, status, headers){
					if(data && data.result){
						var msg = "Unable to make quote.";
						if("quote_create_success" === data.result){
							msg = "Quote created successfully.";
						}else if("quote_already_created" === data.result){
							msg = "Quote already created.";
						}
						AlertService.showAlert(	'AWACP :: Message!', msg)
						.then(function (){qVm.listNewTakeoffsForQuote(); return;},function (){return});
						return;
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "QuoteCtrl::qVm.makeQuote::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			},
			function (){
				return false;
			});
			
		}
		qVm.listQuotes = function(){
			qVm.quotes = [];
			qVm.pageNumber = qVm.currentPage;
			if($state.params.qSource != undefined && $state.params.qSource.length > 0){
				AjaxUtil.getData("/awacp/getTakeoff/"+$state.params.qSource, Math.random())
				.success(function(data, status, headers){
					if(data && data.takeoff){
						$scope.$apply(function(){
							qVm.quotes.push(data.takeoff);
						});
					}					
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "QuoteCtrl::qVm.listNewTakeoffsForQuote::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}else{
				AjaxUtil.getData("/awacp/listTakeoffsForView/"+qVm.pageNumber+"/"+qVm.pageSize+"/true", Math.random())
				.success(function(data, status, headers){
					if(data && data.stsResponse && data.stsResponse.totalCount){
						$scope.$apply(function(){
							qVm.totalItems = data.stsResponse.totalCount;
						});
					}
					if(data && data.stsResponse && data.stsResponse.results){
						var tmp = [];
						if(jQuery.isArray(data.stsResponse.results)) {
							jQuery.each(data.stsResponse.results, function(k, v){
								if(v.hasOwnProperty('bidders') && !jQuery.isArray(v.bidders)){
									var b = [];
									b.push(v.bidders);
									v["bidders"] = b;
								}
								if(v.hasOwnProperty('generalContractors') && !jQuery.isArray(v.generalContractors)){
									var gc = [];
									gc.push(v.generalContractors);
									v["generalContractors"] = gc;
								}
								tmp.push(v);
							});					
						} else {
							if(data.stsResponse.results.hasOwnProperty('bidders') && !jQuery.isArray(data.stsResponse.results.bidders)){
								var b = [];
								b.push(data.stsResponse.results.bidders);
								data.stsResponse.results["bidders"] = b;
							}
							if(data.stsResponse.results.hasOwnProperty('generalContractors') && !jQuery.isArray(data.stsResponse.results.generalContractors)){
								var gc = [];
								gc.push(data.stsResponse.results.generalContractors);
								data.stsResponse.results["generalContractors"] = gc;
							}
							tmp.push(data.stsResponse.results);
						}
						$scope.$apply(function(){
							qVm.quotes  = tmp;
						});
					}		
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "QuoteCtrl::qVm.listNewTakeoffsForQuote::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}
			
		}
		qVm.listNewTakeoffsForQuote = function(){
			qVm.newQuotes = [];
			qVm.pageNumber = qVm.currentPage;
			AjaxUtil.getData("/awacp/listNewTakeoffsForQuote/"+qVm.pageNumber+"/"+qVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						qVm.totalItems = data.stsResponse.totalCount;
					});
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
							if(v.hasOwnProperty('bidders') && !jQuery.isArray(v.bidders)){
								var b = [];
								b.push(v.bidders);
								v["bidders"] = b;
							}
							if(v.hasOwnProperty('generalContractors') && !jQuery.isArray(v.generalContractors)){
								var gc = [];
								gc.push(v.generalContractors);
								v["generalContractors"] = gc;
							}
							tmp.push(v);
						});					
					} else {
						if(data.stsResponse.results.hasOwnProperty('bidders') && !jQuery.isArray(data.stsResponse.results.bidders)){
							var b = [];
							b.push(data.stsResponse.results.bidders);
							data.stsResponse.results["bidders"] = b;
						}
						if(data.stsResponse.results.hasOwnProperty('generalContractors') && !jQuery.isArray(data.stsResponse.results.generalContractors)){
							var gc = [];
							gc.push(data.stsResponse.results.generalContractors);
							data.stsResponse.results["generalContractors"] = gc;
						}
					    tmp.push(data.stsResponse.results);
					}
					$scope.$apply(function(){
						qVm.newQuotes = tmp;
					});
				}		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.listNewTakeoffsForQuote::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		qVm.getUsers = function(){
			qVm.users = [];
			AjaxUtil.getData("/awacp/listUser/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						var t = data.stsResponse.results;
						t.customName = t.userCode + " - "+ t.firstName;
						tmp.push(t);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
							v.customName = v.userCode + " - "+ v.firstName;
							tmp.push(v);
						});
					}	
					$scope.$apply(function(){
						qVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		qVm.getPageSize();
	}		
})();



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('QuoteCtrl', QuoteCtrl);
	QuoteCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function QuoteCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
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
		qVm.architects= [];
		qVm.architect = {};
		
		qVm.selectedNewQuote = {};
		
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
			qVm.getArchitects();
		};		
		qVm.cancelQuoteAction = function(){
			$state.go("quotes");
		}
		qVm.showNewQuoteInfo = function(quote){
			qVm.openInfoBox = true;
			qVm.selectedNewQuote =  quote;
		}
		qVm.newQuotePopover = {
			templateUrl: 'templates/quote-info.html',
			title: 'New Quote Detail'
		};
		qVm.makeQuote = function(id){
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
		}
		qVm.listQuotes = function(){
			qVm.quotes = [];
			qVm.pageNumber = qVm.currentPage;
			AjaxUtil.getData("/awacp/listTakeoffsForView/"+qVm.pageNumber+"/"+qVm.pageSize, Math.random())
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
		
		qVm.initArchitectMasterInputs = function(){
			qVm.getUsers();
		}
		
		qVm.editArchitect = function(){
			if($state.params.id != undefined){
				var formData = {};
				AjaxUtil.getData("/awacp/getArchitect/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.architect){
						data.architect.customName = data.architect.userCode + " - "+ data.architect.firstName;
						$scope.$apply(function(){
							qVm.architect = data.architect;	
							qVm.action = qVm.architect && qVm.architect.id?"Update":"Add";							
						});
						qVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "ArchitectrCtrl::qVm.getArchitectrs::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		qVm.deleteArchitect = function(id){
			AjaxUtil.getData("/awacp/deleteArchitect/"+id, Math.random())
			.success(function(data, status, headers){
				qVm.totalItems = (qVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Architect Detail Deleted Successfully.')
					.then(function (){qVm.getArchitects();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Architect Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "QuoteCtrl::qVm.deleteArchitect::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		qVm.getArchitects = function(){
			qVm.architects = [];
			qVm.pageNumber = qVm.currentPage;
			AjaxUtil.getData("/awacp/listArchitects/"+qVm.pageNumber+"/"+qVm.pageSize, Math.random())
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
							tmp.push(v);
						});					
					} else {
					    tmp.push(data.stsResponse.results);
					}
					$scope.$apply(function(){
						qVm.architects = tmp;
					});
				}		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		qVm.addArchitect = function(){
			var message = "Architecture Detail Created Successfully, add more?";
			var url = "/awacp/saveArchitect";
			var update = false;
			if(qVm.architect && qVm.architect.id){
				message = "Architecture Detail Updated Successfully";
				qVm.architect.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateArchitect";
				update = true;
			}else{
				qVm.architect.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};			
			formData["architect"] = qVm.architect;
			
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){qVm.cancelQuoteAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){qVm.cancelQuoteAction();});
					return;
				}
				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "An Architect with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "QuoteCtrl::qVm.addArchitect::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		qVm.editArchitect();
		qVm.getPageSize();
	}		
})();



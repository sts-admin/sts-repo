(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('QuoteFollowupCtrl', QuoteFollowupCtrl);
	QuoteFollowupCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function QuoteFollowupCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var qfVm = this;
		qfVm.quote = {};
		qfVm.action = "Add";
		$scope.timers = [];
		qfVm.quoteFollowups= [];
		qfVm.quoteFollowup = {};		
		
		qfVm.cancelQuoteFollowupAction = function(){
			$state.go("quotes");
		}
		qfVm.listQuoteFollowups = function(){
			qfVm.quoteFollowups = [];
			qfVm.pageNumber = qfVm.currentPage;
			AjaxUtil.getData("/awacp/getAllQuoteFollowups/"+$state.params.takeoffId, Math.random())
			.success(function(data, status, headers){
				if(data && data.quoteFollowup){
					var tmp = [];
					if(jQuery.isArray(data.quoteFollowup)) {
						jQuery.each(data.quoteFollowup, function(k, v){
							tmp.push(v);
						});
					}else{
						tmp.push(data.quoteFollowup);
					}
					qfVm.quoteFollowups = tmp;
				}
					
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteFollowupCtrl::qfVm.getAllQuoteFollowups::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
	
		
		qfVm.addQuoteFollowup = function(){
			var formData = {};			
			qfVm.quoteFollowup.createdById = StoreService.getUser().userId;	
			formData["quoteFollowup"] = qfVm.quoteFollowup;	
			AjaxUtil.submitData("/awacp/saveQuoteFollowup", formData)
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!', "Follow up detail recorded successfully.")
					.then(function (){return},function (){return;});
					qfVm.listQuoteFollowups();
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteFollowupCtrl::qfVm.addArchitect::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		qfVm.getQuote = function(){
			AjaxUtil.getData("/awacp/getTakeoff/"+$state.params.takeoffId, Math.random())
			.success(function(data, status, headers){
				if(data && data.takeoff){
					$scope.$apply(function(){
						qfVm.quote = data.takeoff;		
						qfVm.quoteFollowup.takeoffId = $state.params.takeoffId;
						qfVm.quoteFollowup.worksheetId = $state.params.worksheetId;
						qfVm.quoteFollowup.createdByUserCode = StoreService.getUser().userCode;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteFollowupCtrl::qfVm.getQuote::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		qfVm.getQuote();
		qfVm.listQuoteFollowups();
	}		
})();



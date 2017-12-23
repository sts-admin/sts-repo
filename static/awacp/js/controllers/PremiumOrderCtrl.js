  (function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('PremiumOrderCtrl', PremiumOrderCtrl);
	PremiumOrderCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService'];
	function PremiumOrderCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService){
		var pOrderVm = this;
		pOrderVm.users = [];
		pOrderVm.orderType = "";
		pOrderVm.salesPeron = {id:"", emailId:""};
		pOrderVm.jobOrder = {};
		pOrderVm.showHideCaption = "Show Email Form";
		pOrderVm.appSetting  = {};
		pOrderVm.showEmailOption = false;
		pOrderVm.sendEmail = function(id){			
			if(pOrderVm.salesPeron.id === '' && pOrderVm.salesPeron.emailId === ''){
				alert("Please either enter email or select.");
				return;
			}
			var emailOrId = pOrderVm.salesPeron.id;
			var source = "id";
			if(emailOrId === '' || emailOrId == undefined){
				emailOrId = pOrderVm.salesPeron.emailId;
				source = "email";
			}			
			AjaxUtil.getData("/awacp/sendPremiumOrderMail/"+emailOrId+"/"+source+"/"+id, Math.random())
			.success(function(data, status, headers){
				if(data && data.status){
					alert("Mail sent successfully.");
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::pOrderVm.sendEmail::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		pOrderVm.toggleEmailOption = function(){
			pOrderVm.showEmailOption = !pOrderVm.showEmailOption;
			if(pOrderVm.showEmailOption){
				pOrderVm.getUsers();				
			}else{
				pOrderVm.showHideCaption = "Show Email Form";
			}
		}
		pOrderVm.getUsers = function(){
			pOrderVm.users = [];
			AjaxUtil.getData("/awacp/listUser/-1/-1", Math.random())
			.success(function(data, status, headers){
				pOrderVm.showHideCaption = "Hide Email Form";
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						data.stsResponse.results.customName = data.stsResponse.results.userCode + " - "+ data.stsResponse.results.firstName;
						tmp.push(data.stsResponse.results);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
							v.customName = v.userCode + " - "+ v.firstName;
							tmp.push(v);
						});
					}					
					$scope.$apply(function(){
						pOrderVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::pOrderVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		pOrderVm.getAppSetting  = function(id){
			pOrderVm.appSetting  = {};
			AjaxUtil.getData("/awacp/getAppSetting/"+id, Math.random())
			.success(function(data, status, headers){
				if(data && data.appSetting){
					$scope.$apply(function(){
						 pOrderVm.appSetting = data.appSetting;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "PremiumOrderCtrl::pOrderVm.getAppSetting::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		pOrderVm.fetchPremiumOrder = function(orderBookId){
			AjaxUtil.getData("/awacp/fetchPremiumOrder/"+orderBookId, Math.random())
			.success(function(data, status, headers){
				if(data && data.orderBook){
					$scope.$apply(function(){
						if(data.orderBook.hasOwnProperty('invItems') && !jQuery.isArray(data.orderBook.invItems)){
							var items = [];
							items.push(data.orderBook.invItems);
							data.orderBook["invItems"] = items;
						}
						if(data.orderBook.hasOwnProperty('invItems') && !jQuery.isArray(data.orderBook.invItems)){
							var items = [];
							items.push(data.orderBook.invItems);
							data.orderBook["invItems"] = items;
						}
						pOrderVm.jobOrder = data.orderBook;				
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "PremiumOrderCtrl::pOrderVm.fetchPremiumOrder::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		pOrderVm.getAppSetting(1);
		if($state.params.orderBookId != undefined){
			pOrderVm.fetchPremiumOrder($state.params.orderBookId);
		}
		if($state.params.inventory != undefined){
			pOrderVm.orderType = $state.params.inventory;
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



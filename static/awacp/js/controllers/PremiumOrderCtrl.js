  (function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('JobOrderCtrl', PremiumOrderCtrl);
	PremiumOrderCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService'];
	function PremiumOrderCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService){
		var pOrderVm = this;
		pOrderVm.jobOrder = {};
		
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
						jobVm.jobOrder = data.orderBook;				
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "PremiumOrderCtrl::pOrderVm.fetchPremiumOrder::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



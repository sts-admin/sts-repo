(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('SystemLogCtrl', SystemLogCtrl);
	SystemLogCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function SystemLogCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var sysVm = this;
	    
		$scope.timers = [];
		sysVm.logs= [];
		sysVm.totalItems = -1;
		sysVm.currentPage = 1;
		sysVm.pageNumber = 1;
		sysVm.pageSize = 20;
		sysVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		sysVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("LOG", size, function(status, size){
				if("success" === status){
					sysVm.pageSize = size;
					sysVm.pageChanged();
				}
			});
		}
		
		sysVm.getPageSize = function(){
			AjaxUtil.getPageSize("LOG", function(status, size){
				if("success" === status){
					sysVm.pageSize = size;
				}
			});
		}
		
		
		sysVm.pageChanged = function() {
			sysVm.getLogs();
		};
	
		
		sysVm.getLogs = function(){
			sysVm.logs = [];
			sysVm.pageNumber = sysVm.currentPage;
			AjaxUtil.getData("/awacp/listSystemLogs/"+sysVm.pageNumber+"/"+sysVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						sysVm.totalItems = data.stsResponse.totalCount;
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
					sysVm.logs = tmp;
					$scope.$digest();
					console.log(sysVm.logs);
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "SystemLogCtrl::sysVm.getLogs::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		sysVm.getPageSize();
	}		
})();



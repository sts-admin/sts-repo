(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('SystemLogCtrl', SystemLogCtrl);
	SystemLogCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function SystemLogCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var sysVm = this;
	    sysVm.loading = false;
		$scope.timers = [];
		sysVm.users = [];
		sysVm.logs= [];
		sysVm.sLog = {};
		sysVm.totalItems = -1;
		sysVm.currentPage = 1;
		sysVm.pageNumber = 1;
		sysVm.pageSize = 20;
		sysVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		sysVm.fromDate = {opened:false};
		sysVm.toDate = {opened:false};
		sysVm.fromDatePicker = function(){
			sysVm.fromDate.opened = true;
		}
		sysVm.toDatePicker = function(){
			sysVm.toDate.opened = true;
		}
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
		sysVm.getUsers()
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
		sysVm.clearSearch = function(){
			sysVm.sLog = {};
			sysVm.getLogs();
		}

		sysVm.getUsers = function(){
			sysVm.users = [];
			AjaxUtil.getData("/awacp/listUser/1/-1", Math.random())
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
						sysVm.users = tmp;
					});						
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::sysVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		sysVm.isValidDateRange =function(fDate, lDate){
			if(!lDate) return true;
			var sDate = new Date(fDate);
			var eDate = new Date(lDate);
			return (eDate >= sDate);
		}
		sysVm.isInputGivenForSearch = function(){
			if(!sysVm.sLog.fromDate && !sysVm.sLog.toDate && !sysVm.sLog.uc && !sysVm.sLog.section && !sysVm.sLog.description){
				return false;
			}
			return true;
		}
		sysVm.initSearch = function(){
			$timeout(function(){	
				
				if(sysVm.isInputGivenForSearch()){
					if(!sysVm.isValidDateRange(sysVm.sLog.fromDate, sysVm.sLog.toDate)){
						 AlertService.showAlert(
							'AWACP :: Message!',
							"To date should be lesser than or equals to from date."
						).then(function (){	
							return;
						},
						function (){	
							return;
						});
					}else{						
						if(sysVm.sLog.fromDate){
							sysVm.sLog.fromDate = new Date(sysVm.sLog.fromDate);
						}
						if(sysVm.sLog.toDate){
							sysVm.sLog.toDate = new Date(sysVm.sLog.toDate);
						}
						sysVm.sLog.pageNumber = 1;
						sysVm.sLog.pageSize = sysVm.pageSize;
						var formData = {};						
						formData["systemLog"] = sysVm.sLog;
						sysVm.loading = true;
						AjaxUtil.submitData("/awacp/filterLogs", formData)
						.success(function(data, status, headers){
							sysVm.logs= [];
							sysVm.loading = false;
							if(data && data.stsResponse && data.stsResponse.totalCount){
								sysVm.totalItems = data.stsResponse.totalCount;
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
							}						
							$scope.$digest();						
						})
						.error(function(jqXHR, textStatus, errorThrown){
							sysVm.loading = false;
							jqXHR.errorSource = "SystemLogCtrl::sysVm.initSearch::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});
					}
				}		
			}, 1000);			
		}		
	}		
})();



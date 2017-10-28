(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TruckerClaimsCtrl', TruckerClaimsCtrl);
	TruckerClaimsCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function TruckerClaimsCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var tcVm = this;
		tcVm.action = "Add";
	    
		$scope.timers = [];
		tcVm.claims= [];
		tcVm.claim = {};
		
		tcVm.totalItems = -1;
		tcVm.currentPage = 1;
		tcVm.pageNumber = 1;
		tcVm.pageSize = 20;
		tcVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		tcVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("TRUCKER", size, function(status, size){
				if("success" === status){
					tcVm.pageSize = size;
					tcVm.pageChanged();
				}
			});
		}
		
		tcVm.getPageSize = function(){
			AjaxUtil.getPageSize("TRUCKER", function(status, size){
				if("success" === status){
					tcVm.pageSize = size;
				}
			});
		}
		
		tcVm.pageChanged = function() {
			tcVm.getTruckers();
		}
	}		
})();



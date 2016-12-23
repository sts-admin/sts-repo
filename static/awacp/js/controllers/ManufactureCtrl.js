(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ManufactureCtrl', ManufactureCtrl);
	ManufactureCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ManufactureCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var manuVm = this;
	    manuVm.totalItems = -1;
		manuVm.currentPage = 1;
		manuVm.pageNumber = 1;
		manuVm.pageSize = 5;
		$scope.timers = [];
		manuVm.manufacturers= [];
		manuVm.manufacture = {};
		manuVm.addManufacture = function (title){		
		}		
		manuVm.pageChanged = function() {
		};		
		manuVm.cancelManufacture = function(){
		}		
		manuVm.editManufacture = function(){
			if($state.params.id != undefined){				
			}
		}		
		manuVm.getManufactures = function(){
			manuVm.manufacturers = [];			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



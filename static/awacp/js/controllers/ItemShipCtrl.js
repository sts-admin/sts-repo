(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ItemShipCtrl', ItemShipCtrl);
	ItemShipCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ItemShipCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var itemVm = this;
	    itemVm.totalItems = -1;
		itemVm.currentPage = 1;
		itemVm.pageNumber = 1;
		itemVm.pageSize = 5;
		$scope.timers = [];
		itemVm.manufacturers= [];
		itemVm.manufacture = {};
		itemVm.addManufacture = function (title){		
		}		
		itemVm.pageChanged = function() {
		};		
		itemVm.cancelManufacture = function(){
		}		
		itemVm.editManufacture = function(){
			if($state.params.id != undefined){				
			}
		}		
		itemVm.getManufactures = function(){
			itemVm.manufacturers = [];			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



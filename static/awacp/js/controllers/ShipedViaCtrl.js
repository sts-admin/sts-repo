(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ShipedViaCtrl', ShipedViaCtrl);
	ShipedViaCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ShipedViaCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var shipVm = this;
	    shipVm.totalItems = -1;
		shipVm.currentPage = 1;
		shipVm.pageNumber = 1;
		shipVm.pageSize = 5;
		$scope.timers = [];
		shipVm.ships= [];
		shipVm.ship = {};
		shipVm.addShip = function (title){		
		}		
		shipVm.pageChanged = function() {
		};		
		shipVm.cancelShip = function(){
		}		
		shipVm.editShip = function(){
			if($state.params.id != undefined){				
			}
		}		
		shipVm.getShips = function(){
			shipVm.ships = [];			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



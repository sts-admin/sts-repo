(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TruckerCtrl', TruckerCtrl);
	TruckerCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function TruckerCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var truckerVm = this;
	    truckerVm.totalItems = -1;
		truckerVm.currentPage = 1;
		truckerVm.pageNumber = 1;
		truckerVm.pageSize = 5;
		$scope.timers = [];
		truckerVm.truckers= [];
		truckerVm.trucker = {};
		truckerVm.addTrucker = function (title){
		
		}		
		truckerVm.pageChanged = function() {
		};		
		truckerVm.cancelTrucker = function(){
		}		
		truckerVm.editTrucker = function(){
			if($state.params.id != undefined){				
			}
		}		
		truckerVm.getTruckers = function(){
			truckerVm.truckers = [];			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



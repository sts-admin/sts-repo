(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ContractorCtrl', ContractorCtrl);
	ContractorCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function ContractorCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var conVm = this;
		$scope.timers = [];
		conVm.contractors= [];
		conVm.contractor = [];
		
		conVm.initContractors = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			conVm.contractors = [];
		}
		conVm.addContractor = function(){
			alert("add contractor");
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



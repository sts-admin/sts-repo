(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ArchitectCtrl', ArchitectCtrl);
	ArchitectCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function ArchitectCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
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
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



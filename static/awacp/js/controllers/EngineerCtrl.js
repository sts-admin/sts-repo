(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('EngineerCtrl', EngineerCtrl);
	EngineerCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function EngineerCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var engVm = this;
		engVm.totalItems = 64;
		engVm.currentPage = 4;
		engVm.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
		engVm.pageChanged = function() {
			$log.log('Page changed to: ' + $scope.currentPage);
		};
		$scope.timers = [];
		engVm.engineers = [];
		engVm.engineer = [];
		
		engVm.initEngineers = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			engVm.engineers = [];
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



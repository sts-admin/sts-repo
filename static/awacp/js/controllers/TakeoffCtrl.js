(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TakeoffCtrl', TakeoffCtrl);
	TakeoffCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function TakeoffCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var takeVm = this;
		takeVm.totalItems = 64;
		takeVm.currentPage = 4;
		takeVm.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
		takeVm.pageChanged = function() {
			$log.log('Page changed to: ' + $scope.currentPage);
		};
		$scope.timers = [];
		takeVm.engineers = [];
		takeVm.engineer = [];
		
		takeVm.initEngineers = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			takeVm.engineers = [];
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



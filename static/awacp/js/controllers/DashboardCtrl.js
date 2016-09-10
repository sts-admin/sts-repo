(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('DashboardCtrl', DashboardCtrl);
	DashboardCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile'];
	function DashboardCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile){
		var dashVm = this;
		$scope.timers = [];
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



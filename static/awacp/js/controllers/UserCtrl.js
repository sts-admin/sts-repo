(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('UserCtrl', UserCtrl);
	UserCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'StoreService', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile'];
	function UserCtrl($scope, $state, $location, $http, AjaxUtil, StoreService, $q, $timeout, $window, $rootScope, $interval, $compile){
		var userVm = this;
		userVm.loginForm = {};
		userVm.timers = [];
		userVm.users = [];
		
		userVm.login = function(){
			AjaxUtil.login(userVm.loginForm.userName, userVm.loginForm.password, 'manual')
			 .success(function (data, response, headers) {	
				 var authority = "",  userName ="";	 
				 if(data && data.authorities){ 
					authority = data.authorities[0].authority; 
					userName = data.authorities[0].userName;  
					StoreService.setUserName(userName);
					StoreService.setRole(authority);
					if (data.access_token){
						StoreService.setAccessToken(data.access_token);
					}
					$state.go("dashboard");
				 }
			 })
			.error(function (jqXHR, textStatus, errorThrown) {	
				jqXHR.errorSource = "Login::Error";
				$scope.signInSpinner = false;
				AjaxUtil.toggleSpinner("login_button", "loading_span", $scope.spinnerUrl, "enable");
				AjaxUtil.saveErrorLog(jqXHR, "Unable to login due to communication error.", true);
			});
		}
		
		
		$scope.$on("$destroy", function(){
			for(var i = 0; i < userVm.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



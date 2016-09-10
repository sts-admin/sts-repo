(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('UserCtrl', UserCtrl);
	UserCtrl.$inject = ['$scope', '$state', '$location', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AjaxUtil', 'UserService', 'StoreService', ];
	function UserCtrl($scope, $state, $location, $q, $timeout, $window, $rootScope, $interval, $compile, AjaxUtil, UserService, StoreService){	
		var userVm = this;
		userVm.spinnerUrl = "<img src='images/loading.gif' />";
		userVm.loginForm = {};
		userVm.timers = [];
		userVm.users = [];
		
		userVm.login = function(){
			AjaxUtil.toggleSpinner('login-submit', 'loading_span', userVm.spinnerUrl, "disable");
			UserService.login(userVm.loginForm.userName, userVm.loginForm.password, 'manual')
			 .success(function (data, response, headers) {	
				AjaxUtil.toggleSpinner('login-submit', 'loading_span', userVm.spinnerUrl, "enable");
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
				AjaxUtil.toggleSpinner('login-submit', 'loading_span', userVm.spinnerUrl, "enable");
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



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('UserCtrl', UserCtrl);
	UserCtrl.$inject = ['$scope', '$state', '$location', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AjaxUtil', 'UserService', 'StoreService', 'RoleService', 'AlertService'];
	function UserCtrl($scope, $state, $location, $q, $timeout, $window, $rootScope, $interval, $compile, AjaxUtil, UserService, StoreService, RoleService, AlertService){	
		var userVm = this;
		userVm.users = [];
		userVm.totalItems = 0;
		userVm.currentPage = 1;
		userVm.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
		userVm.pageChanged = function() {
			console.log('Page changed to: ' + userVm.currentPage);
		};
		userVm.spinnerUrl = "<img src='images/loading.gif' />";
		userVm.loginForm = {};
		userVm.user = {};
		userVm.timers = [];
		userVm.users = [];
		userVm.roles = [];
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
					$rootScope.setUpUserMenu();
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
		
		userVm.navigateToAddUser = function(){
			$state.go('add-user');
		}
		
		userVm.initRoles = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			userVm.roles = [];
			RoleService.listRoles(function(jqXHR, status) {			
				if("success" === status){
					$scope.$apply(function(){
						userVm.roles = jqXHR;
					});
				}else{
					jqXHR.errorSource = "RoleCtrl::userVm.listRoles::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		userVm.saveUser = function(){			
			var role = {};
			role["roleName"] = userVm.user.role.roleName;
			userVm.user.role = role;
			var formData = {};
			formData["user"] = userVm.user;
			AjaxUtil.submitData("/awacp/saveUser", formData)
			.success(function(data, status, headers){
				var message = "User Created Successfully.";
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){return},function (){return});
				return;
			})
			.error(function(jqXHR, textStatus, errorThrown){
				var message = "";
				if(1000 == jqXHR.status){
					message = "Duplicate User Code.";
				}else if(1001 == jqXHR.status){
					message = "User ID already taken.";
				}else if(1002 == jqXHR.status){
					message = "Email ID already taken.";
				}
				if(message.length > 0){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "UserCtrl::saveUser::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		userVm.cancelUserAction = function(){
			$state.go("users");
		}
		userVm.getUsers = function(){
			userVm.users = [];
			AjaxUtil.getData("/awacp/listUser", Math.random())
			.success(function(data, status, headers){
				if(data && data.user && data.user.length > 0){
					var tmp = [];
					userVm.totalItems = data.user.length;
					$.each(data.user, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						tmp.push(v);						
					});
					$scope.$apply(function(){
						userVm.users = tmp;
					});					
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::userVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < userVm.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



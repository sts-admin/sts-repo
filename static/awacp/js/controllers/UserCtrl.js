(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('UserCtrl', UserCtrl);
	UserCtrl.$inject = ['$scope', '$state', '$location', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AjaxUtil', 'UserService', 'StoreService', 'AlertService'];
	function UserCtrl($scope, $state, $location, $q, $timeout, $window, $rootScope, $interval, $compile, AjaxUtil, UserService, StoreService, AlertService){	
		var userVm = this;
		userVm.users = [];
		userVm.roles = [{name:"Administrator", id:"role_admin"}, {name:"Sub Administrator", id:"role_subadmin"}, {name:"Executive", id:"role_executive"}, {name:"Guest", id:"role_guest"}];
		userVm.totalItems = 0;
		userVm.currentPage = 1;
		userVm.pageSize = 5;
		userVm.genders = [{name:"male", title:"Male"}, {name:"female", title:"Female"}];
		userVm.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
		userVm.pageChanged = function() {
			console.log('Page changed to: ' + userVm.currentPage);
		};
		userVm.cancelUserAction = function(){
			$state.go("users");
		}
		userVm.spinnerUrl = "<img src='images/loading.gif' />";
		userVm.loginForm = {};
		userVm.user = {};
		userVm.timers = [];
		userVm.users = [];
		userVm.allPermissionsGroup = [];
		userVm.allPermissions = [];
		userVm.enableSubmitBtn = false;
		userVm.login = function(){
			AjaxUtil.toggleSpinner('login-submit', 'loading_span', userVm.spinnerUrl, "disable");
			UserService.login(userVm.loginForm.userName, userVm.loginForm.password, 'manual')
			 .success(function (data, response, headers) {	
				AjaxUtil.toggleSpinner('login-submit', 'loading_span', userVm.spinnerUrl, "enable");
				 var user = {};
				 if(data && data.authorities){
					user["authority"] = data.authorities[0].authority;
					user["userName"] = data.authorities[0].userName;
					user["userCode"] = data.authorities[0].userCode;
					user["userId"] = data.authorities[0].userId;
					user["token"] = data.access_token;
					user["displayName"] = data.authorities[0].userDisplayName;
					alert(JSON.stringify(user, null, 4));
					StoreService.setUser(user);
					$rootScope.setUpUserMenu();
					if("role_admin" === user.authority){
						$state.go("admin");
					}else{
						$state.go("dashboard");
					}					
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
		userVm.saveUser = function(){
			var role = {};
			role["roleName"] = userVm.user.role.roleName;
			userVm.user.role = role;
			var formData = {};
			formData["user"] = userVm.user;
			AjaxUtil.submitData("/awacp/saveUser", formData)
			.success(function(data, status, headers){
				var message = "User Detail Created Successfully, add more?";
				AlertService.showConfirm(	'AWACP :: Alert!', message)
				.then(function (){return},function (){userVm.cancelUserAction();});
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
		
		userVm.getUsers = function(){
			userVm.users = [];
			AjaxUtil.getData("/awacp/listUser/"+userVm.currentPage+"/"+userVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					userVm.totalItems = data.stsResponse.totalCount;
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						tmp.push(data.stsResponse.results);
					}else{
						$.each(data.stsResponse.results, function(k, v){
							v.customName = v.userCode + " - "+ v.firstName;
							tmp.push(v);
						});
					}					
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
		userVm.showUserDetail = function(userId){
			alert("user id = "+ userId);
		}
		userVm.getPermissionsGroup = function(){ //All permissions, not role specific.
			if(!AjaxUtil.isAuthorized(false)){
				return;
			}
			userVm.allPermissionsGroup = [];
			UserService.getPermissionsGroup(function(jqXHR, status) {
				if("success" === status){
					$scope.$apply(function(){
						userVm.allPermissionsGroup = jqXHR;
					});
				}else{
					jqXHR.errorSource = "UserCtrl::userVm.groupPermissionsGroup::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		userVm.checkAll = function() {
			userVm.user["permissionArray"] = [];
			$.each(userVm.allPermissionsGroup, function(k, v){
				$.each(v.permissions, function(index, val){
					userVm.user.permissionArray.push(val.authority);
				});
			});
			userVm.checkSelection();
		}
		userVm.uncheckAll = function() {
			userVm.user["permissionArray"] = [];
			userVm.checkSelection();
		}
		userVm.checkSelection = function(){
			userVm.enableSubmitBtn = userVm.user && userVm.user.permissionArray && userVm.user.permissionArray.length > 0?true:false;			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < userVm.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		userVm.getPermissionsGroup();
	}		
})();



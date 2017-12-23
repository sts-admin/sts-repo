(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('UserCtrl', UserCtrl);
	UserCtrl.$inject = ['$scope', '$state', '$location', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AjaxUtil', 'UserService', 'StoreService', 'AlertService','$sce'];
	function UserCtrl($scope, $state, $location, $q, $timeout, $window, $rootScope, $interval, $compile, AjaxUtil, UserService, StoreService, AlertService, $sce){	
		var userVm = this;
		userVm.users = [];
		userVm.deletedUsers = [];
		userVm.roles = [{name:"Administrator", id:"role_admin"}, {name:"Sub Administrator", id:"role_subadmin"}, {name:"Executive", id:"role_executive"}, {name:"Guest", id:"role_guest"}];
		userVm.totalItems = 0;
		userVm.currentPage = 1;
		userVm.pageSize = 5;
		userVm.genders = [{name:"Male", title:"Male"}, {name:"Female", title:"Female"}];
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
		userVm.allPermissionsGroup = [];
		userVm.allPermissions = [];
		userVm.enableSubmitBtn = false;
		userVm.login = function(){
			AjaxUtil.toggleSpinner('login-submit', 'loading_span', userVm.spinnerUrl, "disable");
			UserService.login(userVm.loginForm.userName, userVm.loginForm.password, 'manual')
			 .success(function (data, response, headers) {	
				StoreService.remove("rtpQueryOptions-takeoff");
				StoreService.remove("rtpQueryOptions-quote");
				StoreService.remove("rtpQueryOptions-j");
				StoreService.remove("rtpQueryOptions-ob");
				AjaxUtil.toggleSpinner('login-submit', 'loading_span', userVm.spinnerUrl, "enable");
				 var user = {};
				 if(data && data.authorities){
					user["authority"] = data.authorities[0].authority;
					user["userName"] = data.authorities[0].userName;
					user["userCode"] = data.authorities[0].userCode;
					user["userId"] = data.authorities[0].userId;
					user["token"] = data.access_token;
					user["displayName"] = data.authorities[0].userDisplayName;
					StoreService.setUser(user);
					$rootScope.user.userCode = data.authorities[0].userCode;
					$rootScope.setUpUserMenu();
					$rootScope.user.userDisplayName = StoreService.userDisplayName();
					$rootScope.user.role = StoreService.getRole();
					$rootScope.user.isLoggedIn = StoreService.isLoggedIn();
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
			var formData = {};
			userVm.user.createdById = StoreService.getUserId();
			userVm.user.auditMessage = "Created user with ID: '"+userVm.user.userNames+"'";
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
						data.stsResponse.results.customName = data.stsResponse.results.userCode + " - "+ data.stsResponse.results.firstName;
						tmp.push(data.stsResponse.results);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
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
		userVm.getDeletedUsers = function(){
			userVm.deletedUsers = [];
			AjaxUtil.getData("/awacp/listArchivedUser/"+userVm.currentPage+"/"+userVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					userVm.totalItems = data.stsResponse.totalCount;
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						tmp.push(data.stsResponse.results);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
							v.customName = v.userCode + " - "+ v.firstName;
							tmp.push(v);
						});
					}					
					$scope.$apply(function(){
						userVm.deletedUsers = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::userVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.dynamicPopover = {
			templateUrl: 'templates/user-privileges.html',
			title: 'User Privileges'
		};
		$scope.htmlPopover = "";
		userVm.showUserDetail = function(userId){
			//alert("user id = "+ userId);
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
			jQuery.each(userVm.allPermissionsGroup, function(k, v){
				jQuery.each(v.permissions, function(index, val){
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
		userVm.updateUser = function(){
			userVm.user.updatedById = StoreService.getUserId();
			userVm.user.auditMessage = "Updated user with ID: '"+userVm.user.userNames+"'";
			var formData = {};
			formData["user"] = userVm.user;
			AjaxUtil.submitData("/awacp/updateUser", formData)
			.success(function (data, status, headers){
				if(userVm.user.id == StoreService.getUserId()){
					$rootScope.setUpUserMenu();
				}
				AlertService.showAlert(	'AWACP :: Alert!','User Detail updated successfully.')
				.then(function (){userVm.cancelUserAction();return;},function (){return;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::userVm.updateUser::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		userVm.deleteUser = function(userId){
			if(userId == StoreService.getUserId()){
				AlertService.showAlert(	'AWACP :: Warning!','You can not delete yourself.')
				.then(function (){return;},function (){return;});
			}else{
				AlertService.showConfirm(	'AWACP :: Confirmation!','Are you sure to delete this user?')
				.then(function (){
					AjaxUtil.deleteData("/awacp/delete/user/"+userId, Math.random())
					.success(function(data, status, headers){
						userVm.getUsers();
					})
					.error(function(jqXHR, textStatus, errorThrown){
						var message = "";
						if(11111 == jqXHR.status){
							message = "Unable to delete, there is only one user";
						}
						if(message.length > 0){
							AlertService.showAlert(	'AWACP :: Warning!', message)
							.then(function (){return},function (){return});
							return;
						}
						jqXHR.errorSource = "UserCtrl::userVm.deleteUser::Error";
						AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
					});
				},function (){return;});
			}
		}
		userVm.archiveUser = function(userId){
			if(userId == StoreService.getUserId()){
				AlertService.showAlert(	'AWACP :: Warning!','You can not delete yourself.')
				.then(function (){return;},function (){return;});
			}else{
				AlertService.showConfirm(	'AWACP :: Confirmation!','Are you sure to delete this user?')
				.then(function (){
					AjaxUtil.deleteData("/awacp/archive/user/"+userId, Math.random())
					.success(function(data, status, headers){
						userVm.getUsers();
					})
					.error(function(jqXHR, textStatus, errorThrown){
						var message = "";
						if(11111 == jqXHR.status){
							message = "Unable to delete, there is only one user";
						}
						if(message.length > 0){
							AlertService.showAlert(	'AWACP :: Warning!', message)
							.then(function (){return},function (){return});
							return;
						}
						jqXHR.errorSource = "UserCtrl::userVm.archiveUser::Error";
						AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
					});
				},function (){return;});
			}
		}
		userVm.editUser = function(id){
			userVm.user = [];
			AjaxUtil.getData("/awacp/getUserWithPermissions/"+id, Math.random())
			.success(function(data, status, headers){
				if(data && data.user){
					userVm.user = data.user;					
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::userVm.editUser::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		userVm.editUser = function(id){
			userVm.user = [];
			AjaxUtil.getData("/awacp/getUserWithPermissions/"+id, Math.random())
			.success(function(data, status, headers){
				if(data && data.user){
					userVm.user = data.user;					
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::userVm.editUser::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		if($state.params && $state.params.id){ //edit user url
			userVm.editUser($state.params.id);
		}
		userVm.getPermissionsGroup();
	}		
})();



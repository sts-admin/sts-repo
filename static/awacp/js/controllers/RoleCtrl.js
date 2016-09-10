(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('RoleCtrl', RoleCtrl);
	RoleCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'RoleService'];
	function RoleCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, RoleService){
		var roleVm = this;
		roleVm.roles = [];
		roleVm.allPermissionsGroup = [];
		roleVm.allPermissions = [];
		roleVm.role = [];
		roleVm.selectedRole = null; 
		roleVm.showUpdateBtn = false;
		
		roleVm.initRoles = function(){
			roleVm.roles = [];
			RoleService.listRoles(function(jqXHR, status) {			
				if("success" === status){
					$scope.$apply(function(){
						roleVm.roles = jqXHR;
					});
				}else{
					jqXHR.errorSource = "RoleCtrl::roleVm.listRoles::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		roleVm.getPermissionsGroup = function(){ //All permissions, not role specific.
			roleVm.allPermissionsGroup = [];
			RoleService.getPermissionsGroup(function(jqXHR, status) {
				if("success" === status){
					$scope.$apply(function(){
						roleVm.allPermissionsGroup = jqXHR;
					});
				}else{
					jqXHR.errorSource = "RoleCtrl::roleVm.groupPermissionsGroup::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		roleVm.getRoleWithPermissions = function(){	 // Get role with its permissions
			if(null == roleVm.selectedRole || roleVm.selectedRole.roleName.length <= 0){
				AlertService.showAlert(	'AWACP :: Alert!','Please select a role.').then(function (){me.logout();},function (){return;});
				return;
			}
			roleVm.allPermissions = [];
			RoleService.getRoleWithPermissions(roleVm.selectedRole.roleName, function (jqXHR, status) {
				if("success" === status){
					$scope.$apply(function(){
						roleVm.role = jqXHR;
					});
					roleVm.getPermissionsGroup();
				}else{
					jqXHR.errorSource = "RoleCtrl::roleVm.getRoleWithPermissions::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		roleVm.updateRolePermissions = function(){	
			alert("permissionArray = "+ JSON.stringify(roleVm.role.permissionArray, null, 4));
			var formData = {};
			formData["role"] = roleVm.role;
			/* RoleService.updateRolePermissions(formData, function (jqXHR, status) {
				if("success" === status){
					AlertService.showAlert(	'AWACP :: Alert!','Permission(s) Updated successfully.')
					.then(function (){return;},function (){return;});
				}else{
					jqXHR.errorSource = "RoleCtrl::roleVm.updateRolePermissions::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			}); */
			AjaxUtil.submitData("/awacp/updateRole", formData)
				.success(function (data, status, headers){
					alert("success");
				})
				.error(function(jqXHR, textStatus, errorThrown){	
					alert("error");
				});
		}
		roleVm.checkSelection = function(){
			roleVm.showUpdateBtn = roleVm.role.permissionArray.length > 0?true:false;			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



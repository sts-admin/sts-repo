(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('RoleCtrl', RoleCtrl);
	RoleCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'RoleService'];
	function RoleCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, RoleService){
		var roleVm = this;
		$scope.timers = [];
		roleVm.roles = [];
		roleVm.allPermissionsGroup = [];
		roleVm.allPermissions = [];
		roleVm.role = {};
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
			roleVm.role = {};
			if(null == roleVm.selectedRole || roleVm.selectedRole.roleName.length <= 0){
				roleVm.allPermissionsGroup = [];
				roleVm.checkSelection();
				AlertService.showAlert(	'AWACP :: Alert!','Please select a role.').then(function (){return},function (){return;});
				return;
			}
			RoleService.getRoleWithPermissions(roleVm.selectedRole.roleName, function (jqXHR, status) {
				if("success" === status){
					if(jqXHR.permissions && !jqXHR.permissions.length){
						var permissionArray = [];
						permissionArray.push(jqXHR.permissionArray);
						jqXHR["permissionArray"] = permissionArray;
					}
					$scope.$apply(function(){
						roleVm.role = jqXHR;
					});
					roleVm.getPermissionsGroup();
					roleVm.checkSelection();
				}else{
					jqXHR.errorSource = "RoleCtrl::roleVm.getRoleWithPermissions::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		roleVm.checkAll = function() {
			roleVm.role["permissionArray"] = [];
			jQuery.each(roleVm.allPermissionsGroup, function(k, v){
				jQuery.each(v.permissions, function(index, val){
					roleVm.role.permissionArray.push(val.authority);
				});
			});
			roleVm.checkSelection();
		}
		roleVm.uncheckAll = function() {
			roleVm.role["permissionArray"] = [];
			roleVm.checkSelection();
		}
		roleVm.updateRolePermissions = function(){	
			var formData = {};
			formData["role"] = roleVm.role;
			console.log(roleVm.role);
			AjaxUtil.submitData("/awacp/updateRole", formData)
			.success(function (data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!','Permission(s) Updated successfully.')
				.then(function (){return;},function (){return;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
			});
		}
		roleVm.checkSelection = function(){
			roleVm.showUpdateBtn = roleVm.role && roleVm.role.permissionArray && roleVm.role.permissionArray.length > 0?true:false;			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



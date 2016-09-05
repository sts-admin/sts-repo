(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('RoleCtrl', RoleCtrl);
	RoleCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function RoleCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var roleVm = this;
		roleVm.roles = [];
		roleVm.allPermissions = [];
		roleVm.role = [];
		roleVm.selectedRole = null;       
		roleVm.initRoles = function(){
			roleVm.roles = [];
			var tmp = [];
			AjaxUtil.getData("/awacp/listRoles", Math.random())
			.success(function (data, status, headers) {					
				if(data && data.role && data.role.length > 0){
					$.each(data.role, function(k, v){
						tmp.push(v);
					});
					$scope.$apply(function(){
						roleVm.roles = tmp;
					});
				}
			})
			.error(function (jqXHR, textStatus, errorThrown) {
				alert("ERROR:"+JSON.stringify(jqXHR, null, 4));
			});
		}
		roleVm.getAllPermissions = function(){ //All permissions, not role specific.
			roleVm.allPermissions = [];AjaxUtil.listAllPermissions(function(jqXHR, status) {		
				if("success" === status){
					$scope.$apply(function(){
						roleVm.allPermissions = jqXHR;
					});
				}else{
					jqXHR.errorSource = "RoleCtrl::roleVm.getAllPermissions::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		roleVm.getRoleWithPermissions = function(){	 // Get role with its permissions
			if(null == roleVm.selectedRole || roleVm.selectedRole.roleName.length <= 0){
				 AlertService.showAlert(
					'AWACP :: Alert!',
					'Please select a role.'
				).then(function (){
					return;
				},
				function (){
					return;
				});
				return;
			}
			roleVm.allPermissions = [];
			AjaxUtil.getRoleWithPermissions(roleVm.selectedRole.roleName, function (jqXHR, status) {		
				if("success" === status){
					$scope.$apply(function(){
						roleVm.role = jqXHR;
					});
					roleVm.getAllPermissions();
				}else{
					jqXHR.errorSource = "RoleCtrl::roleVm.getRoleWithPermissions::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
	
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



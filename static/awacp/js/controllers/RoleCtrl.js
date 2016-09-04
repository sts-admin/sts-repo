(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('RoleCtrl', RoleCtrl);
	RoleCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile'];
	function RoleCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile){
		var roleVm = this;
		roleVm.roles = [];
		roleVm.allPermissions = [];
		roleVm.role = [];
		roleVm.selectedRole = [];
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
		roleVm.getAllPermissions = function(){
			roleVm.allPermissions = [];
			var tmp =[];
			AjaxUtil.getData("/awacp/listAllPermissions", Math.random())
			.success(function (data, status, headers) {		
				if(data && data.permission && data.permission.length > 0){
					$.each(data.permission, function(k, v){
						tmp.push(v);
					});
					$scope.$apply(function(){
						roleVm.allPermissions = tmp;
					});
				}
				//alert(JSON.stringify(roleVm.allPermissions, null, 4));
			})
			.error(function (jqXHR, textStatus, errorThrown) {
				alert("ERROR:"+JSON.stringify(jqXHR, null, 4));
			});
		}
		roleVm.getRoleWithPermissions = function(){			
			roleVm.role = [];
			//alert(roleVm.selectedRole.roleName);
			AjaxUtil.getData("/awacp/listAllPermissions", Math.random())
			.success(function (data, status, headers) {					
				if(data && data.role){
					roleVm.role = data.role;
				}
				//alert(JSON.stringify(roleVm.role, null, 4));
				roleVm.getAllPermissions();
			})
			.error(function (jqXHR, textStatus, errorThrown) {
				alert("ERROR:"+JSON.stringify(jqXHR, null, 4));
			});
		}
	
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



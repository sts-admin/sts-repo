(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('RoleService', function (AjaxUtil) {
		return {
			listRoles:function(callback){
				var result = [];
				AjaxUtil.getData("/awacp/listRoles", Math.random())
				.success(function (data, status, headers) {	
					if(data && data.role && data.role.length > 0){
						$.each(data.role, function(k, v){
							result.push(v);
						});
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(result, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			listAllPermissions:function(callback){
				var result = [];
				AjaxUtil.getData("/awacp/listAllPermissions", Math.random())
				.success(function (data, status, headers) {	
					if(data && data.permission && data.permission.length > 0){
						$.each(data.permission, function(k, v){
							result.push(v);
						});
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(result, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			getPermissionsGroup:function(callback){
				var result = [];
				AjaxUtil.getData("/awacp/groupPermissionsGroup", Math.random())
				.success(function (data, status, headers) {	
					if(data && data.permissionGroup && data.permissionGroup.length > 0){
						$.each(data.permissionGroup, function(k, v){
							result.push(v);
						});
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(result, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			getRoleWithPermissions:function(roleName, callback){
				AjaxUtil.getData("/awacp/getRoleWithPermissions?roleName="+roleName, Math.random())
				.success(function (data, status, headers) {	
					if(data && data.role){
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(data.role, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			updateRolePermissions:function(formData, callback){				
				AjaxUtil.submitData("/awacp/updateRole", formData)
				.success(function (data, status, headers){
					if(data){
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(result, "success");
						}
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){	
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			}
		};
	});
})();







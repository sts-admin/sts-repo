(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('RoleService', function (AjaxUtil) {
		return {
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
							if(v.permissions.length === undefined || v.permissions.length === 'undefined'){
								var tmp = [];
								tmp.push(v.permissions);
								v["permissions"] = tmp;
								result.push(v);
							}else{
								result.push(v);
							}
							
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
			updateUserPermissions:function(formData, callback){				
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







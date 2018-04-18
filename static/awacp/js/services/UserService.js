(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('UserService', function (AjaxUtil, StoreService, $timeout, $rootScope, $state, base, $window, basePath) {
		var groupIndex = [];
			groupIndex["Takeoff"] = 0;
			groupIndex["Quote"] = 1;
			groupIndex["Job Order"] = 2;
			groupIndex["Order Book"] = 3;
			groupIndex["AW"] = 4;
			groupIndex["AWF"] = 5;
			groupIndex["SBC"] = 6;
			groupIndex["SPL"] = 7;
			groupIndex["J"] = 8;
			groupIndex["Q"] = 9;
			groupIndex["BBT"] = 10;
			groupIndex["Claim"] = 11;
			groupIndex["Marketing"] = 12;
			groupIndex["Collection"] = 13;
		return {					
			login: function (username, password, loginType) {
				jQuery.support.cors = true;
				var data ={
				  'username' 	  : username+'`'+loginType,
				  'password' 	  : password,
				  'client_id' 	  : 'awacpservices',
				  'client_secret' : 'awacpservices',
				  'grant_type'    : 'password'
				};
				return jQuery.ajax({
						url: base+'/oauth/token',
						type: "POST",
						data: data,
						contentType: "application/x-www-form-urlencoded",
						crossDomain: true,
						beforeSend: function(jqXHR, settings) {
							jqXHR.url = settings.url;
						}
				});
			},
			updateOnlineStatus:function(userId, flag, callback){
				var url  = "/awacp/updateOnlineStatus?userId="+ userId + "&status="+flag;
				AjaxUtil.getData(url, Math.random())
				.success(function (data, status, headers) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(data, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			logout: function(){
				if(!StoreService.getAccessToken()){
					StoreService.removeAll(); 
					$window.location.href = basePath;
					return;
				}
				var me = this;
				var user = StoreService.getUser();
				var headers = {
					'Authorization' : 'Bearer ' + user.token,
					'Accept' : 'application/json'
				};	
				this.updateOnlineStatus(user.userId, false, function(data, status){
					if("success" === status){
						jQuery.ajaxSetup({
							'headers' : headers,
							dataType : 'json'
						});
						jQuery.ajax(
						{
							url: base+'/logout',
							type: "POST",
							data: Math.random(),
							contentType: "text/plain",
							crossDomain: true, 
							dataType:"text"
						}).success(function (data, status, headers, config) {	
						}).error(function (jqXHR, textStatus, errorThrown) {
							if (jqXHR.readyState == 0) {
								$rootScope.$apply(function(){
									$rootScope.alert.noService = true;
								});						
							}							
						});
					}
				});
				
				StoreService.removeAll(); 
				$window.location.href = basePath;
				$rootScope.user.isLoggedIn = StoreService.isLoggedIn();
			},
			getUserAddress:function(userId, callback){
				AjaxUtil.getData("/js/getAddress?userId="+ userId, Math.random())
				.success(function (data, status, headers) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(data, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			getUserDetail: function(callback){
				AjaxUtil.getData("/ob/getUserDetailByUserEmail?userEmail="+ StoreService.getUserName(), Math.random())
				.success(function (data, status, headers) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(data, "success");
					}					
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}					
				});
			},
			setUser: function(user, callback){
				if(user == null){ //get user and then set in StoreService for further reference
					AjaxUtil.getData( "/js/getUserDetails?userNameOrEmail="+ StoreService.getUserName(), Math.random())
					.success(function (data, status, headers) {
						StoreService.setUser(data.user);
						$rootScope.$apply(function(){
							$rootScope.user.isLoggedIn = StoreService.isLoggedIn();
							$rootScope.user.profileImageUrl = data.user.photoUrl;
						});						
						if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
							callback(data.user, "success");
						}
					})
					.error(function (jqXHR, textStatus, errorThrown) {
						if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
							callback(jqXHR, "error");
						}
					});
				}else{
					StoreService.setUser(user);
					$rootScope.$apply(function(){
						$rootScope.user.isLoggedIn = StoreService.isLoggedIn();
						$rootScope.user.profileImageUrl = user.photoUrl;
					});	
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(user, "success");
					}
				}				
			},
			initializeMenu: function(callback){
				var menus = {};
				var url  = "/awacp/getUserMenusByNameOrEmail?userNameOrEmail="+ StoreService.getUserName();
				AjaxUtil.getData( url, Math.random())
				.success(function (data, status, headers) {
					if(data && data.menu && data.menu.length > 0){
						jQuery.each(data.menu, function(k, v){
							if(v.items.length === undefined || v.items.length === 'undefined'){
								var tmp = []
								tmp.push(v.items);
								menus[v.id] = tmp;
							}else{
								menus[v.id] = v.items;
							}		
						});
					}	
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(menus, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			listAllPermissions:function(callback){
				var result = [];
				AjaxUtil.getData("/awacp/listAllPermissions", Math.random())
				.success(function (data, status, headers) {	
					if(data && data.permission && data.permission.length > 0){
						jQuery.each(data.permission, function(k, v){
							result.push(v);
						});
						if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
							callback(result, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			
			getPermissionsGroup:function(callback){
				var result = [];
				AjaxUtil.getData("/awacp/groupPermissionsGroup", Math.random())
				.success(function (data, status, headers) {	
					if(data && data.permissionGroup && data.permissionGroup.length > 0){
						//result = new Array(data.permissionGroup.length);
						//alert("Length = "+ data.permissionGroup.length);
						jQuery.each(data.permissionGroup, function(k, v){
							if(v.permissions.length === undefined || v.permissions.length === 'undefined'){
								var tmp = [];
								tmp.push(v.permissions);
								v["permissions"] = tmp;
							}
							result[groupIndex[v.groupName]] = v;														
						});
						if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
							callback(result, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			updateUserPermissions:function(formData, callback){				
				AjaxUtil.submitData("/awacp/updateRole", formData)
				.success(function (data, status, headers){
					if(data){
						if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
							callback(result, "success");
						}
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){	
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			}
		};
	});
})();







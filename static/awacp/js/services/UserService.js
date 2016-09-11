(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('UserService', function (AjaxUtil, StoreService, $timeout, $rootScope, $state, base) {
		return {					
			login: function (username, password, loginType) {
				$.support.cors = true;
				var data ={
				  'username' 	  : username+'`'+loginType,
				  'password' 	  : password,
				  'client_id' 	  : 'awacpservices',
				  'client_secret' : 'awacpservices',
				  'grant_type'    : 'password'
				};
				return $.ajax({
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
			logout: function(){
				var me = this;
				var accessToken = StoreService.getAccessToken('token');
				var headers = {
					'Authorization' : 'Bearer ' + accessToken,
					'Accept' : 'application/json'
				};				
				StoreService.removeAll(); 
				$timeout(function(){
					$rootScope.$apply(function(){
						$rootScope.user.isLoggedIn = StoreService.isLoggedIn();
						$rootScope.user.profileImageUrl = StoreService.profileImageUrl()
					});					
				}, 500);
				
				$.ajaxSetup({
					'headers' : headers,
					 dataType : 'json'
				});
				$.ajax({url: base+'/logout',type: "POST",data: Math.random(),contentType: "text/plain",crossDomain: true, dataType:"text"})
				.success(function (data, status, headers, config) {											
					$state.go("/");
				}).error(function (jqXHR, textStatus, errorThrown) {
					if (jqXHR.readyState == 0) {
						$rootScope.$apply(function(){
							$rootScope.alert.noService = true;
						});
					}								
				});
			},
			getUserAddress:function(userId, callback){
				AjaxUtil.getData("/js/getAddress?userId="+ userId, Math.random())
				.success(function (data, status, headers) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(data, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			getUserDetail: function(callback){
				AjaxUtil.getData("/ob/getUserDetailByUserEmail?userEmail="+ StoreService.getUserName(), Math.random())
				.success(function (data, status, headers) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(data, "success");
					}					
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
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
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(data.user, "success");
						}
					})
					.error(function (jqXHR, textStatus, errorThrown) {
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(jqXHR, "error");
						}
					});
				}else{
					StoreService.setUser('user', user);
					$rootScope.$apply(function(){
						$rootScope.user.isLoggedIn = StoreService.isLoggedIn();
						$rootScope.user.profileImageUrl = user.photoUrl;
					});	
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(user, "success");
					}
				}				
			}
		};
	});
})();






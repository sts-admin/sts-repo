(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('StoreService', function (store, $rootScope, $window) {
		return {
			setUser:function(user){
				$window.sessionStorage.setItem('awacp_user', JSON.stringify(user));
			},
			getUser:function(){
				var user = $window.sessionStorage.getItem('awacp_user');
				return user ? JSON.parse(user) : null;
			},
			getUserId:function(){
				var user = this.getUser();
				if(user != null){
					return user.userId;
				}
				return "";
			},
			userDisplayName:function(){
				var user = this.getUser();
				if(user != null){
					return user.displayName;
				}
				return "";
			},
			/*
			profileImageUrl:function(){	
				var user = store.get('awacp_user');
				if(user!=null){
					return user.photoUrl;
				}
				return "";
			},
			setUserCode:function(userCode){
				store.set("awacp_userCode", userCode);
			},
			setUserName:function(userName){
				store.set("awacp_userName", userName);
			},
			
			setAccessToken:function(accessToken){
				store.set("awacp_token", accessToken);
			}
			*/
			getUserCode: function() {
				var user = this.getUser();
				if(user !=null){
					return user.userCode;
				}
				return "";
			},			
			getUserName: function() {
				var user = this.getUser();
				if(user !=null){
					return user.userName;
				}
				return "";
			},
			getAccessToken: function() {
				var user = this.getUser();
				if(user != null){
					return user.token;
				}
				return "";
			},
			isLoggedIn: function() {
				var user = this.getUser();
				return user != null;
			},
			getRole:function(){
				var user = this.getUser();
				if(user !=null){
					return user.authority;
				}
				return "";
			},
			set:function(key, val){
				$window.sessionStorage.setItem(key, val);	
				/*$rootScope.storeKeys.push(key);*/
			},
			remove:function(key){
				$window.sessionStorage.removeItem(key);	
				/*if($rootScope.storeKeys && $rootScope.storeKeys.length > 0){
					for(var i = 0; i < $rootScope.storeKeys.length; i++){
						if($rootScope.storeKeys[i] === key){
							$rootScope.storeKeys.splice(i, 1);
							break;
						}
					}
				}*/
			},
			get:function(key){
				return $window.sessionStorage.getItem(key);	
			},
			removeAll:function(){
				$window.sessionStorage.clear();
				/*if($rootScope.storeKeys && $rootScope.storeKeys.length > 0){
					for(var i = 0; i < $rootScope.storeKeys.length; i++){
						store.remove($rootScope.storeKeys[i]);
					}
				}*/
			}
		};
	});
})();











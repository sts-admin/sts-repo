(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('StoreService', function (store, $rootScope) {
		return {
			setUser:function(user){
				store.set('awacp_user', user);							
			},
			getUser:function(){
				return store.get('awacp_user');
			},
			getUserId:function(){
				var user = store.get('awacp_user');
				if(user != null){
					return user.userId;
				}
				return "";
			},
			userDisplayName:function(){
				var user = store.get('awacp_user');
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
				if(user !=null){
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
				store.set(key, val);	
				$rootScope.storeKeys.push(key);
			},
			remove:function(key){
				store.remove(key);	
				if($rootScope.storeKeys && $rootScope.storeKeys.length > 0){
					for(var i = 0; i < $rootScope.storeKeys.length; i++){
						if($rootScope.storeKeys[i] === key){
							$rootScope.storeKeys.splice(i, 1);
							break;
						}
					}
				}
			},
			get:function(key){
				return store.get(key);	
			},
			removeAll:function(){
				store.remove('awacp_user'); 
				if($rootScope.storeKeys && $rootScope.storeKeys.length > 0){
					for(var i = 0; i < $rootScope.storeKeys.length; i++){
						store.remove($rootScope.storeKeys[i]);
					}
				}
			}
		};
	});
})();











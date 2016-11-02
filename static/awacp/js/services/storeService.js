(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('StoreService', function (store) {
		return {
			setUser:function(user){
				store.set('awacp_user', user);
			},
			getUser:function(){
				return store.get('awacp_user');
			},
			
			userDisplayName:function(){
				var user = store.get('awacp_user');
				if(user!=null){
					return user.userDisplayName;
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
				if(user !=null){
					return user.role;
				}
				return "";
			},
			removeAll:function(){
				store.remove('awacp_user'); 
			}
		};
	});
})();











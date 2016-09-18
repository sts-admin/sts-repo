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
					return user.firstName + "	" +user.lastName;
				}
				return "";
			},
			profileImageUrl:function(){	
				var user = store.get('awacp_user');
				if(user!=null){
					return user.photoUrl;
				}
				return "";
			},
			setUserName:function(userName){
				store.set("awacp_userName", userName);
			},
			getUserName: function() {
				return store.get("awacp_userName");
			},
			setAccessToken:function(accessToken){
				store.set("awacp_token", accessToken);
			},
			getAccessToken: function() {
				return store.get('awacp_token');
			},
			isLoggedIn: function() {
				return !!store.get('awacp_token');
			},
			setRole:function(role){
				store.set("awacp_role", role);
			},
			getRole:function(){
				return store.get("awacp_role");
			},
			removeAll:function(){
				store.remove('awacp_token'); 
				store.remove('awacp_role');
				store.remove('awacp_facebook');
				store.remove('awacp_user');
			}
		};
	});
})();











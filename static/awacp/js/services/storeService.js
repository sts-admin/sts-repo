(function() {
	'use strict';
	angular.module('SS', [])
	.factory('StoreService', function (store) {
		return {
			setUser:function(user){
				store.set('user', user);
			},
			getUser:function(){
				return store.get('user');
			},
			profileImageUrl:function(){	
				var user = store.get('user');
				if(user!=null){
					return user.photoUrl;
				}
				return "";
			},
			setUserName:function(userName){
				store.set("userName", userName);
			},
			getUserName: function() {
				return store.get("userName");
			},
			setAccessToken:function(accessToken){
				store.set("token", accessToken);
			},
			getAccessToken: function() {
				return store.get('token');
			},
			isLoggedIn: function() {
				return !!store.get('token');
			},
			setRole:function(role){
				store.set("role", role);
			},
			getRole:function(){
				return store.get("role");
			},
			removeAll:function(){
				store.remove('token'); 
				store.remove('role');
				store.remove('facebook');
				store.remove('user');
			}
		};
	});
})();







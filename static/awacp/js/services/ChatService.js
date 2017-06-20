(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('ChatService', function (AjaxUtil, StoreService, $timeout, $rootScope, $state, base, $window, basePath) {
		return {
			listUsers: function(listType, callback){
				var users = [];
				var url = "/awacp/listOnlineUsers";
				if(listType === 'offline'){
					url = "/awacp/listOfflineUsers";
				}
				AjaxUtil.getData(url, Math.random())
				.success(function (data, status, headers) {
					if(data && data.userDTO){
						if(jQuery.isArray(data.userDTO)){
							jQuery.each(data.userDTO, function(k, v){
								if(v.userCode === $rootScope.user.userCode){
									return false;
								}
								users.push(v);
							});
						}else{
							users.push(data.userDTO);
						}
					}
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(users, "success");
					}					
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}					
				});
			}
		};
	});
})();







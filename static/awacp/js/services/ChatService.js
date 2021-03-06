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
								var doPush = true;
								if(v.userCode === $rootScope.user.userCode){
									doPush = false;
								}
								if(doPush){
									users.push(v);
								}								
							});
						}else{
							if(data.userDTO.userCode !== $rootScope.user.userCode){
									users.push(data.userDTO);
							}
							
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
			},
			markMessagesAsRead: function(loggedInUserId, otherUserId, callback){
				var url = "/awacp/markMessagesAsRead/"+loggedInUserId+"/"+otherUserId;				
				AjaxUtil.getData(url, Math.random())
				.success(function (data, status, headers) {
					if(data){
						if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
							callback(data, "success");
						}
					}										
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}					
				});
			},
			fetchUnreadMessageCounts: function(userId, callback){
				var messages = [];
				var url = "/awacp/getAllMyUnreadMessagesCount/"+userId;				
				AjaxUtil.getData(url, Math.random())
				.success(function (data, status, headers) {
					if(data && data.chatMessage){
						if(jQuery.isArray(data.chatMessage)){
							jQuery.each(data.chatMessage, function(k, v){
								messages.push(v);							
							});
						}else{
							messages.push(data.chatMessage);							
						}
					}
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(messages, "success");
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







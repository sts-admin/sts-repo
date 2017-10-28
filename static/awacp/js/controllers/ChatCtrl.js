(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ChatCtrl', ChatCtrl);
	ChatCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'ChatService', 'StoreService'];
	function ChatCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, ChatService, StoreService){
		var chatVm = this;
		$scope.timers = [];
		
		chatVm.chatMessage = {};
		
		
		chatVm.sendMessage = function(){
			if(!$rootScope.targetUser){
				return;
			}		
			if(chatVm.chatMessage.message && chatVm.chatMessage.message.length > 0){
				var formData = {};
				chatVm.chatMessage.sourceUserId = StoreService.getUserId();
				chatVm.chatMessage.sourceUserName = $rootScope.user.userDisplayName;
				chatVm.chatMessage.targetUserId = $rootScope.targetUser.id;
				chatVm.chatMessage.targetUserName = $rootScope.targetUser.firstName + " "+ $rootScope.targetUser.lastName;
				formData["chatMessage"] = chatVm.chatMessage;
				AjaxUtil.submitData("/awacp/saveChatMessage", formData)
				.success(function(data, status, headers){
					chatVm.chatMessage.message = "";
					$rootScope.listChatMessage($rootScope.targetUser);
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "UserCtrl::saveUser::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}			
		}
		
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		chatVm.initUsers = function(){
			$rootScope.listOnlineUsers();	
		}
	}		
})();



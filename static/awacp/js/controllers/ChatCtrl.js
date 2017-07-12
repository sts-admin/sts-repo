(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ChatCtrl', ChatCtrl);
	ChatCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'ChatService'];
	function ChatCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, ChatService){
		var chatVm = this;
		$scope.timers = [];
		chatVm.onlineUsers = [];
		chatVm.offlineUsers = [];
		chatVm.chatMessage = {};
		chatVm.listOnlineUsers = function(){
			chatVm.onlineUsers = [];
			ChatService.listUsers("online", function(result, status){
				if("success" === status){
					chatVm.onlineUsers = result;
				}
				console.log(JSON.stringify());
			});
		}
		chatVm.listOfflineUsers = function(){
			chatVm.offlineUsers = [];
			ChatService.listUsers("offline", function(result, status){
				if("success" === status){
					chatVm.offlineUsers = result;
				}
			});
		}
		chatVm.saveChatMessage = function(){
			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		chatVm.initUsers = function(){
			chatVm.listOnlineUsers();
			chatVm.listOfflineUsers();
		}
	}		
})();



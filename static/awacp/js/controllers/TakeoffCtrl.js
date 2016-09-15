(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TakeoffCtrl', TakeoffCtrl);
	TakeoffCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function TakeoffCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var takeVm = this;
		takeVm.totalItems = 0;
		takeVm.currentPage = 1;
		takeVm.users = [];
		takeVm.engineers = [];
		takeVm.architectures = [];
		takeVm.takeoff = {};
		takeVm.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
		takeVm.pageChanged = function() {
			console.log('Page changed to: ' + $scope.currentPage);
		};
		$scope.timers = [];
		takeVm.engineers = [];
		takeVm.engineer = [];
		
		takeVm.initEngineers = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			takeVm.engineers = [];
		}
		takeVm.cancelTakeoffAction = function(){
			$state.go("takeoffs");
		}
		takeVm.getUsers = function(){
			takeVm.users = [];
			AjaxUtil.getData("/awacp/listUser", Math.random())
			.success(function(data, status, headers){
				if(data && data.user && data.user.length > 0){
					$.each(data.user, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						takeVm.users.push(v);
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.initTakeoffMasterInputs = function(){
			takeVm.getUsers();
		}
		takeVm.getEngineers = function(){
			takeVm.engineers = [];
			AjaxUtil.listEngineers(function(result, status){
				if("success" === status){
					takeVm.engineers = result;
				}else{
					jqXHR.errorSource = "TakeoffCtrl::takeVm.initCountries::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		takeVm.getArchitectures = function(){
			takeVm.architectures = [];
			AjaxUtil.listArchitectures(function(result, status){				
				if("success" === status){
					$scope.$apply(function(){
						takeVm.architectures = result;
					});					
				}else{
					jqXHR.errorSource = "TakeoffCtrl::takeVm.getArchitectures::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



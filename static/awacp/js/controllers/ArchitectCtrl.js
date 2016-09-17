(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ArchitectCtrl', ArchitectCtrl);
	ArchitectCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function ArchitectCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var arcVm = this;
		$scope.timers = [];
		arcVm.architects= [];
		arcVm.architect = {};
		
		arcVm.initArchitects = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			arcVm.architects = [];
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		arcVm.cancelArchitectAction = function(){
			$state.go("architects");
		}
		
		arcVm.initCountries = function(){
			arcVm.countries = [];
			AjaxUtil.listCountries(function(result, status){
				if("success" === status){
					arcVm.countries = result;
				}else{
					jqXHR.errorSource = "ContractorCtrl::arcVm.initCountries::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		arcVm.getStates = function(){
			arcVm.states = [];
			AjaxUtil.listStates(arcVm.architect.country.id, function(result, status){				
				if("success" === status){
					$scope.$apply(function(){
						arcVm.states = result;
					});					
				}else{
					jqXHR.errorSource = "ContractorCtrl::arcVm.getStates::Error, countryId = " + arcVm.architect.country.id;
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		arcVm.getUsers = function(){
			arcVm.users = [];
			AjaxUtil.getData("/awacp/listUser", Math.random())
			.success(function(data, status, headers){
				if(data && data.user && data.user.length > 0){
					$.each(data.user, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						arcVm.users.push(v);
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ContractorCtrl::arcVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		arcVm.initArchitectMasterInputs = function(){
			arcVm.initCountries();
			arcVm.getUsers();
		}
		
		arcVm.getArchitects = function(){alert(1)
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			arcVm.architects = [];
			AjaxUtil.getData("/awacp/listArchitects", Math.random())
			.success(function(data, status, headers){
				if(data && data.architect && data.architect.length > 0){
					arcVm.totalItems = data.architect.length;
					$.each(data.architect, function(k, v){
						arcVm.architects.push(v);
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::arcVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		arcVm.addArchitect = function(){
			var formData = {};
			formData["architect"] = arcVm.architect;
			AjaxUtil.submitData("/awacp/saveArchitect", formData)
			.success(function(data, status, headers){
				alert("submit success");
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ContractorCtrl::arcVm.addContractor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
	}		
})();



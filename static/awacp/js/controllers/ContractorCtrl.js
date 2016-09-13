(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ContractorCtrl', ContractorCtrl);
	ContractorCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function ContractorCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var conVm = this;
		conVm.totalItems = 64;
		conVm.currentPage = 4;
		conVm.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
		conVm.pageChanged = function() {
			$log.log('Page changed to: ' + $scope.currentPage);
		};
		$scope.timers = [];
		conVm.contractors= [];
		conVm.contractor = {};
		conVm.countries = [];
		conVm.states = [];
		conVm.users = [];
		conVm.cancelContractorAction = function(){
			$state.go("contractors");
		}
		conVm.initCountries = function(){
			conVm.countries = [];
			AjaxUtil.listCountries(function(result, status){
				if("success" === status){
					conVm.countries = result;
				}else{
					jqXHR.errorSource = "ContractorCtrl::conVm.initCountries::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		conVm.getStates = function(){
			conVm.states = [];
			AjaxUtil.listStates(conVm.contractor.country.id, function(result, status){				
				if("success" === status){
					$scope.$apply(function(){
						conVm.states = result;
					});					
				}else{
					jqXHR.errorSource = "ContractorCtrl::conVm.getStates::Error, countryId = " + conVm.contractor.country.id;
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		conVm.getUsers = function(){
			conVm.users = [];
			AjaxUtil.getData("/awacp/listUser", Math.random())
			.success(function(data, status, headers){
				if(data && data.user && data.user.length > 0){
					$.each(data.user, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						conVm.users.push(v);
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ContractorCtrl::conVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		conVm.initContractors = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			conVm.contractors = [];
		}
		conVm.addContractor = function(){
			console.log(conVm.contractor);
			var formData = {};
			formData["contractor"] = conVm.contractor;
			alert(JSON.stringify(formData, null, 4));
			AjaxUtil.submitData("/awacp/saveContractor", formData)
			.success(function(data, status, headers){
				alert("submit success");
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ContractorCtrl::conVm.addContractor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		conVm.initContractorMasterInputs = function(){
			conVm.initCountries();
			conVm.getUsers();
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('EngineerCtrl', EngineerCtrl);
	EngineerCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function EngineerCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var engVm = this;
		engVm.totalItems = 0;
		engVm.currentPage = 1;
		$scope.timers = [];
		engVm.engineers = [];
		engVm.engineer = {};
		
		engVm.setPage = function (pageNo) {
			engVm.currentPage = pageNo;
		};
		engVm.pageChanged = function() {
			console.log('Page changed to: ' + engVm.currentPage);
		};
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		engVm.cancelEngineerAction = function(){
			$state.go("engineers");
		}
		
		engVm.initCountries = function(){
			engVm.countries = [];
			AjaxUtil.listCountries(function(result, status){
				if("success" === status){
					engVm.countries = result;
				}else{
					jqXHR.errorSource = "ContractorCtrl::engVm.initCountries::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		engVm.getStates = function(){
			engVm.states = [];
			AjaxUtil.listStates(engVm.engineer.country.id, function(result, status){				
				if("success" === status){
					$scope.$apply(function(){
						engVm.states = result;
					});					
				}else{
					jqXHR.errorSource = "ContractorCtrl::engVm.getStates::Error, countryId = " + engVm.engineer.country.id;
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		engVm.getUsers = function(){
			engVm.users = [];
			AjaxUtil.getData("/awacp/listUser", Math.random())
			.success(function(data, status, headers){
				if(data && data.user && data.user.length > 0){
					$.each(data.user, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						engVm.users.push(v);
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ContractorCtrl::engVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		engVm.initEngineerMasterInputs = function(){
			engVm.initCountries();
			engVm.getUsers();
		}
		
		engVm.getEngineers = function(){
			alert("getEngineers");
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			engVm.engineers = [];
			AjaxUtil.getData("/awacp/listEngineers", Math.random())
			.success(function(data, status, headers){
				if(data && data.engineer && data.engineer.length > 0){
					var tmp = [];
					engVm.totalItems = data.engineer.length;
					$.each(data.engineer, function(k, v){
						tmp.push(v);
					});
					$scope.$apply(function(){
						engVm.engineers = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::engVm.getEngineers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		engVm.addEngineer = function(){
			var formData = {};
			formData["engineer"] = engVm.engineer;
			AjaxUtil.submitData("/awacp/saveEngineer", formData)
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Message!','Engineer added successfully.')
				.then(function (){					
					return
				},function (){return;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ContractorCtrl::engVm.addContractor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
	}		
})();



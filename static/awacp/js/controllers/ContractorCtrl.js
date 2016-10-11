(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ContractorCtrl', ContractorCtrl);
	ContractorCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function ContractorCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var conVm = this;
	    conVm.totalItems = -1;
		conVm.currentPage = 1;
		conVm.pageNumber = 1;
		conVm.pageSize = 5;
		$scope.timers = [];
		conVm.contractors= [];
		conVm.contractor = {};
		conVm.countries = [];
		conVm.states = [];
		conVm.users = [];
		
		conVm.pageChanged = function() {
			conVm.getContractors();
		};

		
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
			AjaxUtil.getData("/awacp/listUser/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						tmp.push(v);
					});
					$scope.$apply(function(){
						conVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::conVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		conVm.addContractor = function(){
			var formData = {};
			formData["contractor"] = conVm.contractor;
			AjaxUtil.submitData("/awacp/saveContractor", formData)
			.success(function(data, status, headers){
				var message = "Contractor Detail Created Successfully, add more?";
				AlertService.showConfirm(	'AWACP :: Alert!', message)
				.then(function (){return},function (){conVm.cancelContractorAction();});
				return;
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
		
		conVm.editContractor = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["contractor"] = conVm.contractor;
				AjaxUtil.getData("/awacp/getContractor/"+$state.params.id, formData)
				.success(function(data, status, headers){
					if(data && data.contractor){
						data.contractor.customName = data.contractor.userCode + " - "+ data.contractor.firstName;
						$scope.$apply(function(){
							conVm.contractor = data.contractor;							
						});
						conVm.initCountries();
						conVm.getStates();
						conVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "ContractorCtrl::bidVm.getContractors::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		conVm.getContractors = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			conVm.pageNumber = conVm.currentPage;
			AjaxUtil.getData("/awacp/listContractors/"+conVm.pageNumber+"/"+conVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						conVm.totalItems = data.stsResponse.totalCount;
					});
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						tmp.push(data.stsResponse.results);
					}else{
						$.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
						});
					}
					$scope.$apply(function(){
						conVm.contractors = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::conVm.getContractors::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		conVm.editContractor();
	}		
})();



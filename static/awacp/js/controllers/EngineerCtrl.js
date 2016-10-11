(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('EngineerCtrl', EngineerCtrl);
	EngineerCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function EngineerCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var engVm = this;
		engVm.totalItems = 0;
		engVm.currentPage = 1;
		engVm.pageSize = 5;
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
			AjaxUtil.getData("/awacp/listUser/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						tmp.push(data.stsResponse.results);
					}else{
						$.each(data.stsResponse.results, function(k, v){
							v.customName = v.userCode + " - "+ v.firstName;
							tmp.push(v);
						});
					}	
					
					$scope.$apply(function(){
						engVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::engVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		engVm.initEngineerMasterInputs = function(){
			engVm.initCountries();
			engVm.getUsers();
		}
		
	    engVm.editEngineer = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["engineer"] = engVm.engineer;
				AjaxUtil.getData("/awacp/getEngineer/"+$state.params.id, formData)
				.success(function(data, status, headers){
					if(data && data.engineer){
						data.engineer.customName = data.engineer.userCode + " - "+ data.engineer.firstName;
						$scope.$apply(function(){
							engVm.engineer = data.engineer;							
						});
						engVm.initCountries();
						engVm.getStates();
						engVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "EngineerCtrl::bidVm.getEngineers::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		
		engVm.getEngineers = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			engVm.engineers = [];
			AjaxUtil.getData("/awacp/listEngineers/"+engVm.currentPage+"/"+engVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					engVm.totalItems = 	data.stsResponse.totalCount;
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
				var message = "Engineer Detail Created Successfully, add more?";
				AlertService.showConfirm(	'AWACP :: Alert!', message)
				.then(function (){return},function (){engVm.cancelEngineerAction();});
				return;
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ContractorCtrl::engVm.addContractor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		engVm.editEngineer();
		
	}		
})();



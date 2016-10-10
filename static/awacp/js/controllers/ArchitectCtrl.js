(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ArchitectCtrl', ArchitectCtrl);
	ArchitectCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function ArchitectCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var arcVm = this;
		$scope.timers = [];
		arcVm.totalItems = -1;
		arcVm.currentPage = 1;
		arcVm.pageNumber = 1;
		arcVm.pageSize = 5;
		arcVm.architects= [];
		arcVm.architect = {};
		arcVm.pageChanged = function() {
			arcVm.getArchitects();
		};		
		arcVm.cancelArchitectAction = function(){
			$state.go("architects");
		}		
		arcVm.initCountries = function(){
			arcVm.countries = [];
			AjaxUtil.listCountries(function(result, status){
				if("success" === status){
					arcVm.countries = result;
				}else{
					jqXHR.errorSource = "ArchitectCtrl::arcVm.initCountries::Error";
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
					jqXHR.errorSource = "ArchitectCtrl::arcVm.getStates::Error, countryId = " + arcVm.architect.country.id;
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		arcVm.getUsers = function(){
			arcVm.users = [];
			AjaxUtil.getData("/awacp/listUser/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						tmp.push(v);
					});
					$scope.$apply(function(){
						arcVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::arcVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		arcVm.initArchitectMasterInputs = function(){
			arcVm.initCountries();
			arcVm.getUsers();
		}
		
		arcVm.getArchitects = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			arcVm.architects = [];
			AjaxUtil.getData("/awacp/listArchitects/"+arcVm.pageNumber+"/"+arcVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && arcVm.totalItems <= 0){//Already set
					$scope.$apply(function(){
						arcVm.totalItems = data.stsResponse.totalCount;
					});
				}
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];					
					$.each(data.stsResponse.results, function(k, v){
						tmp.push(v);						
					});
					$scope.$apply(function(){
						arcVm.architects = tmp;
					});
				}				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ArchitectCtrl::arcVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		arcVm.addArchitect = function(){
			var formData = {};
			formData["architect"] = arcVm.architect;
			AjaxUtil.submitData("/awacp/saveArchitect", formData)
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Message!','Architect added successfully.')
				.then(function (){					
					return
				},function (){return;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ArchitectCtrl::arcVm.addArchitect::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



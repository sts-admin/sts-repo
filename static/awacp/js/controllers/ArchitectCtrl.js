(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ArchitectCtrl', ArchitectCtrl);
	ArchitectCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function ArchitectCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var arcVm = this;
		$scope.timers = [];
		arcVm.totalItems = 0;
		arcVm.currentPage = 1;
		arcVm.architects= [];
		arcVm.architect = {};
		arcVm.setPage = function (pageNo) {
			arcVm.currentPage = pageNo;
		};
		arcVm.pageChanged = function() {
			console.log('Page changed to: ' + arcVm.currentPage);
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
				jqXHR.errorSource = "ArchitectCtrl::arcVm.getUsers::Error";
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
			AjaxUtil.getData("/awacp/listArchitects", Math.random())
			.success(function(data, status, headers){
				if(data && data.architect && data.architect.length > 0){
					var tmp = [];
					arcVm.totalItems = data.architect.length;
					$.each(data.architect, function(k, v){
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



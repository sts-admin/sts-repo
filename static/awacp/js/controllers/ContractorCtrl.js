(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ContractorCtrl', ContractorCtrl);
	ContractorCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function ContractorCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var conVm = this;
		conVm.action = "Add";
	    conVm.totalItems = -1;
		conVm.currentPage = 1;
		conVm.pageNumber = 1;
		conVm.pageSize = 5;
		$scope.timers = [];
		conVm.contractors= [];
		conVm.contractor = {};
		conVm.users = [];
		
		conVm.pageChanged = function() {
			conVm.getContractors();
		};

		
		conVm.cancelContractorAction = function(){
			$state.go("contractors");
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
			var message = "Contractor Detail Created Successfully, add more?";
			var url = "/awacp/saveContractor";
			var update = false;
			if(conVm.contractor && conVm.contractor.id){
				message = "Contractor Detail Updated Successfully";
				conVm.contractor.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateContractor";
				update = true;
			}else{
				conVm.contractor.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["contractor"] = conVm.contractor;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){conVm.cancelContractorAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){conVm.cancelContractorAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ContractorCtrl::conVm.addContractor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		conVm.initContractorMasterInputs = function(){
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
							conVm.action = conVm.contractor && conVm.contractor.id?"Update":"Add";							
						});
						conVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "ContractorCtrl::conVm.editContractor::Error";
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
					if($.isArray(data.stsResponse.results)) {
						$.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
						});					
					} else {
					    tmp.push(data.stsResponse.results);
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



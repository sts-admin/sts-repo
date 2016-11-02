(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('GeneralContractorCtrl', GeneralContractorCtrl);
	GeneralContractorCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function GeneralContractorCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var gcVm = this;
		gcVm.action = "Add";
	    gcVm.totalItems = -1;
		gcVm.currentPage = 1;
		gcVm.pageNumber = 1;
		gcVm.pageSize = 5;
		$scope.timers = [];
		gcVm.generalContractors= [];
		gcVm.generalContractor = {};
		gcVm.users = [];
		
		gcVm.pageChanged = function() {
			gcVm.getGcs();
		};

		
		gcVm.cancelGcAction = function(){
			$state.go("gcs");
		}
		gcVm.getUsers = function(){
			gcVm.users = [];
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
						gcVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "GeneralContractorfCtrl::gcVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		gcVm.addGc = function(){
			var message = "General Contractor Detail Created Successfully, add more?";
			var url = "/awacp/saveGc";
			var update = false;
			if(gcVm.generalContractor && gcVm.generalContractor.id){
				message = "General Contractor Detail Updated Successfully";
				gcVm.generalContractor.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateGc";
				update = true;
			}else{
				gcVm.generalContractor.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["generalContractor"] = gcVm.generalContractor;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){gcVm.cancelGcAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){gcVm.cancelGcAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A General Contractor with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "GeneralContractorCtrl::gcVm.addGc::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		gcVm.initGcsMasterInputs = function(){
			gcVm.getUsers();
		}
		
		gcVm.editGc = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["generalContractor"] = gcVm.generalContractor;
				AjaxUtil.getData("/awacp/getGc/"+$state.params.id, formData)
				.success(function(data, status, headers){
					if(data && data.generalContractor){
						data.generalContractor.customName = data.generalContractor.userCode + " - "+ data.generalContractor.firstName;
						$scope.$apply(function(){
							gcVm.generalContractor = data.generalContractor;	
							gcVm.action = gcVm.generalContractor && gcVm.generalContractor.id?"Update":"Add";							
						});
						gcVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "GeneralContractorCtrl::gcVm.editGc::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		gcVm.getGcs = function(){
			gcVm.pageNumber = gcVm.currentPage;
			AjaxUtil.getData("/awacp/listGcs/"+gcVm.pageNumber+"/"+gcVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						gcVm.totalItems = data.stsResponse.totalCount;
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
						gcVm.generalContractors = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "GeneralContractorCtrl::gcVm.getGcs::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		gcVm.editGc();
	}		
})();



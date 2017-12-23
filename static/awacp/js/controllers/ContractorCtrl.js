(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ContractorCtrl', ContractorCtrl);
	ContractorCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function ContractorCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var conVm = this;
		conVm.action = "Add";
		$scope.timers = [];
		conVm.contractors= [];
		conVm.contractor = {};
		conVm.users = [];
		conVm.totalItems = -1;
		conVm.currentPage = 1;
		conVm.pageNumber = 1;
		conVm.pageSize = 20;
		conVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		conVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("CONTRACTOR", size, function(status, size){
				if("success" === status){
					conVm.pageSize = size;
					conVm.pageChanged();
				}
			});
		}
		
		conVm.getPageSize = function(){
			AjaxUtil.getPageSize("CONTRACTOR", function(status, size){
				if("success" === status){
					conVm.pageSize = size;
				}
			});
		}
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
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						var t = data.stsResponse.results;
						t.customName = t.userCode + " - "+ t.firstName;
						tmp.push(t);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
							v.customName = v.userCode + " - "+ v.firstName;
							tmp.push(v);
						});
					}	
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
				conVm.contractor.updatedById = StoreService.getUser().userId;
				conVm.contractor.auditMessage = "Updated Contractor with item name '"+ conVm.contractor.name + "'";
				url = "/awacp/updateContractor";
				update = true;
			}else{
				conVm.contractor.createdByUserCode = StoreService.getUser().userCode;
				conVm.contractor.createdById = StoreService.getUser().userId;
				conVm.contractor.auditMessage = "Created Contractor with item name '"+ conVm.contractor.name + "'";
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
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Contractor with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}else if(1003 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Contractor with this name already exist, please use a different name.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "ContractorCtrl::conVm.addContractor::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}				
			});
		}
		conVm.initContractorMasterInputs = function(){
			conVm.getUsers();
		}
		
		conVm.editContractor = function(){
			if($state.params.id != undefined){
				AjaxUtil.getData("/awacp/getContractor/"+$state.params.id, Math.random())
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
		conVm.deleteContractor = function(id){
			AjaxUtil.getData("/awacp/deleteContractor/"+id, Math.random())
			.success(function(data, status, headers){
				conVm.totalItems = (conVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Contractor Detail Deleted Successfully.')
					.then(function (){conVm.getContractors();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Contractor Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ContractorCtrl::conVm.deleteContractor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		conVm.getContractors = function(){
			conVm.contractors = [];
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
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
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



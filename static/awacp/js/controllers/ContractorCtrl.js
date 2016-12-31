(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ContractorCtrl', ContractorCtrl);
	ContractorCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function ContractorCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var truckerVm = this;
		truckerVm.action = "Add";
		$scope.timers = [];
		truckerVm.contractors= [];
		truckerVm.contractor = {};
		truckerVm.users = [];
		truckerVm.totalItems = -1;
		truckerVm.currentPage = 1;
		truckerVm.pageNumber = 1;
		truckerVm.pageSize = 20;
		truckerVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		truckerVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("CONTRACTOR", size, function(status, size){
				if("success" === status){
					truckerVm.pageSize = size;
					truckerVm.pageChanged();
				}
			});
		}
		
		truckerVm.getPageSize = function(){
			AjaxUtil.getPageSize("CONTRACTOR", function(status, size){
				if("success" === status){
					truckerVm.pageSize = size;
				}
			});
		}
		truckerVm.pageChanged = function() {
			truckerVm.getContractors();
		};

		
		truckerVm.cancelContractorAction = function(){
			$state.go("contractors");
		}
		truckerVm.getUsers = function(){
			truckerVm.users = [];
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
						truckerVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::truckerVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		truckerVm.addContractor = function(){
			var message = "Contractor Detail Created Successfully, add more?";
			var url = "/awacp/saveContractor";
			var update = false;
			if(truckerVm.contractor && truckerVm.contractor.id){
				message = "Contractor Detail Updated Successfully";
				truckerVm.contractor.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateContractor";
				update = true;
			}else{
				truckerVm.contractor.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["contractor"] = truckerVm.contractor;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){truckerVm.cancelContractorAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){truckerVm.cancelContractorAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Contractor with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ContractorCtrl::truckerVm.addContractor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		truckerVm.initContractorMasterInputs = function(){
			truckerVm.getUsers();
		}
		
		truckerVm.editContractor = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["contractor"] = truckerVm.contractor;
				AjaxUtil.getData("/awacp/getContractor/"+$state.params.id, formData)
				.success(function(data, status, headers){
					if(data && data.contractor){
						data.contractor.customName = data.contractor.userCode + " - "+ data.contractor.firstName;
						$scope.$apply(function(){
							truckerVm.contractor = data.contractor;	
							truckerVm.action = truckerVm.contractor && truckerVm.contractor.id?"Update":"Add";							
						});
						truckerVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "ContractorCtrl::truckerVm.editContractor::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		truckerVm.deleteContractor = function(id){
			AjaxUtil.getData("/awacp/deleteContractor/"+id, Math.random())
			.success(function(data, status, headers){
				truckerVm.totalItems = (truckerVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Contractor Detail Deleted Successfully.')
					.then(function (){truckerVm.getContractors();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Contractor Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ContractorCtrl::truckerVm.deleteContractor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		truckerVm.getContractors = function(){
			truckerVm.contractors = [];
			truckerVm.pageNumber = truckerVm.currentPage;
			AjaxUtil.getData("/awacp/listContractors/"+truckerVm.pageNumber+"/"+truckerVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						truckerVm.totalItems = data.stsResponse.totalCount;
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
						truckerVm.contractors = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::truckerVm.getContractors::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		truckerVm.editContractor();
	}		
})();



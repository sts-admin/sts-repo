(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('GeneralContractorCtrl', GeneralContractorCtrl);
	GeneralContractorCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function GeneralContractorCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var gcVm = this;
		gcVm.action = "Add";
	   	$scope.timers = [];
		gcVm.generalContractors= [];
		gcVm.generalContractor = {};
		gcVm.users = [];
		
		gcVm.totalItems = -1;
		gcVm.currentPage = 1;
		gcVm.pageNumber = 1;
		gcVm.pageSize = 20;
		gcVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		gcVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("GC", size, function(status, size){
				if("success" === status){
					gcVm.pageSize = size;
					gcVm.pageChanged();
				}
			});
		}
		
		gcVm.getPageSize = function(){
			AjaxUtil.getPageSize("GC", function(status, size){
				if("success" === status){
					gcVm.pageSize = size;
				}
			});
		}
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
				gcVm.generalContractor.updatedById = StoreService.getUser().userId;
				gcVm.generalContractor.auditMessage = "Updated GC with name '"+ gcVm.generalContractor.name + "'";
				url = "/awacp/updateGc";
				update = true;
			}else{
				gcVm.generalContractor.createdByUserCode = StoreService.getUser().userCode;
				gcVm.generalContractor.createdById = StoreService.getUser().userId;
				gcVm.generalContractor.auditMessage = "Created GC with name '"+ gcVm.generalContractor.name + "'";
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
				}else if(1003 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A General Contractor with this name already exist, please use a different name.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "GeneralContractorCtrl::gcVm.addGc::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}				
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
		gcVm.deleteGc = function(id){
			AjaxUtil.getData("/awacp/deleteGc/"+id, Math.random())
			.success(function(data, status, headers){
				gcVm.totalItems = (gcVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'General Contractor Detail Deleted Successfully.')
					.then(function (){gcVm.getGCs();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete General Contractor Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "GeneralContractorCtrl::gcVm.deleteGC::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		gcVm.getGcs = function(){
			gcVm.generalContractors = [];
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
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
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



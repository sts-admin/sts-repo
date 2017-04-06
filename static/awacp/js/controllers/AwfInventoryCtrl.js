(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('AwfInventoryCtrl', AwfInventoryCtrl);
	AwfInventoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function AwfInventoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var awfInvVm = this;
		awfInvVm.action = "Add";
	    
		$scope.timers = [];
		awfInvVm.awfInventories= [];
		awfInvVm.awfInventory = {};
		
		awfInvVm.totalItems = -1;
		awfInvVm.currentPage = 1;
		awfInvVm.pageNumber = 1;
		awfInvVm.pageSize = 20;
		awfInvVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		awfInvVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("AWF_INV", size, function(status, size){
				if("success" === status){
					awfInvVm.pageSize = size;
					awfInvVm.pageChanged();
				}
			});
		}
		
		awfInvVm.getPageSize = function(){
			AjaxUtil.getPageSize("AWF_INV", function(status, size){
				if("success" === status){
					awfInvVm.pageSize = size;
				}
			});
		}
		
		awfInvVm.pageChanged = function() {
			awfInvVm.getAwfInventories();
		}		
		awfInvVm.cancelAwfInventoryAction = function(){
			$state.go("awf-view");		
		}
		awfInvVm.addOrUpdateAwfInventory = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');
			awfInvVm.addAwfInventory();
		}
		
		
		awfInvVm.addAwfInventory = function(){
			var message = "AWF Inventory Detail Created Successfully, add more?";
			var url = "/awacp/saveAwfInventory";
			var update = false;
			if(awfInvVm.awfInventory && awfInvVm.awfInventory.id){
				message = "AWF Inventory Detail Updated Successfully";
				awfInvVm.awfInventory.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateAwfInventory";
				update = true;
			}else{
				awfInvVm.awfInventory.createdById = StoreService.getUser().userId;
				awfInvVm.awfInventory.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["awfInventory"] = awfInvVm.awfInventory;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				awfInvVm.awfInventory = {};
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){awfInvVm.cancelAwfInventoryAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){awfInvVm.cancelAwfInventoryAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				jqXHR.errorSource = "AwfInventoryCtrl.js::awfInvVm.addAwfInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		awfInvVm.editAwfInventory = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["awfInventory"] = awfInvVm.awfInventory;
				AjaxUtil.getData("/awacp/getAwfInventory/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.awfInventory){
						$scope.$apply(function(){
							awfInvVm.awfInventory = data.awfInventory;	
							awfInvVm.action = awfInvVm.awfInventory && awfInvVm.awfInventory.id?"Update":"Add";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "AwfInventoryCtrl.js::awfInvVm.editAwfInventory::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		awfInvVm.deleteAwfInventory = function(id){
			AjaxUtil.getData("/awacp/deleteAwfInventory/"+id, Math.random())
			.success(function(data, status, headers){
				awfInvVm.totalItems = (awfInvVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'AWF Inventory Detail Deleted Successfully.')
					.then(function (){awfInvVm.getAwfInventories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete AWF Inventory Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "AwfInventoryCtrl::awfInvVm.deleteAwfInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		awfInvVm.getAwfInventories = function(){
			awfInvVm.awfInventories = [];
			awfInvVm.pageNumber = awfInvVm.currentPage;
			AjaxUtil.getData("/awacp/listAwfInventories/"+awfInvVm.pageNumber+"/"+awfInvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						awfInvVm.totalItems = data.stsResponse.totalCount;
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
						awfInvVm.awfInventories = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AwfInventoryCtrl::awfInvVm.getAwfInventories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		awfInvVm.editAwfInventory();
	}		
})();



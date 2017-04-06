(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('SplInventoryCtrl', SplInventoryCtrl);
	SplInventoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function SplInventoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var splInvVm = this;
		splInvVm.action = "Add";
	    
		$scope.timers = [];
		splInvVm.splInventories= [];
		splInvVm.splInventory = {};
		
		splInvVm.totalItems = -1;
		splInvVm.currentPage = 1;
		splInvVm.pageNumber = 1;
		splInvVm.pageSize = 20;
		splInvVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		splInvVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("SPL_INV", size, function(status, size){
				if("success" === status){
					splInvVm.pageSize = size;
					splInvVm.pageChanged();
				}
			});
		}
		
		splInvVm.getPageSize = function(){
			AjaxUtil.getPageSize("SPL_INV", function(status, size){
				if("success" === status){
					splInvVm.pageSize = size;
				}
			});
		}
		
		splInvVm.pageChanged = function() {
			splInvVm.getSplInventories();
		}		
		splInvVm.cancelSplInventoryAction = function(){
			$state.go("spl-view");		
		}
		splInvVm.addOrUpdateSplInventory = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');
			splInvVm.addSplInventory();
		}
		
		
		splInvVm.addSplInventory = function(){
			var message = "SPL Inventory Detail Created Successfully, add more?";
			var url = "/awacp/saveSplInventory";
			var update = false;
			if(splInvVm.splInventory && splInvVm.splInventory.id){
				message = "SPL Inventory Detail Updated Successfully";
				splInvVm.splInventory.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateSplInventory";
				update = true;
			}else{
				splInvVm.splInventory.createdById = StoreService.getUser().userId;
				splInvVm.splInventory.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["splInventory"] = splInvVm.splInventory;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				splInvVm.splInventory = {};
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){splInvVm.cancelSplInventoryAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){splInvVm.cancelSplInventoryAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				jqXHR.errorSource = "SplInventoryCtrl::splInvVm.addSplInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		splInvVm.editSplInventory = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["splInventory"] = splInvVm.splInventory;
				AjaxUtil.getData("/awacp/getSplInventory/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.splInventory){
						$scope.$apply(function(){
							splInvVm.splInventory = data.splInventory;	
							splInvVm.action = splInvVm.splInventory && splInvVm.splInventory.id?"Update":"Add";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "SplInventoryCtrl::splInvVm.editSplInventory::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		splInvVm.deleteSplInventory = function(id){
			AjaxUtil.getData("/awacp/deleteSplInventory/"+id, Math.random())
			.success(function(data, status, headers){
				splInvVm.totalItems = (splInvVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'SPL Inventory Detail Deleted Successfully.')
					.then(function (){splInvVm.getSplInventories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete SPL Inventory Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "SplInventoryCtrl.js::splInvVm.deleteSplInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		splInvVm.getSplInventories = function(){
			splInvVm.splInventories = [];
			splInvVm.pageNumber = splInvVm.currentPage;
			AjaxUtil.getData("/awacp/listSplInventories/"+splInvVm.pageNumber+"/"+splInvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						splInvVm.totalItems = data.stsResponse.totalCount;
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
						splInvVm.splInventories = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "SplInventoryCtrl::splInvVm.getSplInventories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		splInvVm.editSplInventory();
	}		
})();



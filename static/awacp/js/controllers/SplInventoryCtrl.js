(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('SplInventoryCtrl', SplInventoryCtrl);
	SplInventoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function SplInventoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var splInvVm = this;
		splInvVm.action = "Add";
	    splInvVm.totalItems = -1;
		splInvVm.currentPage = 1;
		splInvVm.pageNumber = 1;
		splInvVm.pageSize = 1;
		$scope.timers = [];
		splInvVm.jinvs= [];
		splInvVm.jinv = {};
		
		splInvVm.pageChanged = function() {
			splInvVm.getJInventories();
		}		
		splInvVm.cancelJInventoryAction = function(){
			$state.go("jinvs");		
		}
		splInvVm.addOrUpdateJInventory = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');
		}
		
		
		splInvVm.addJInventory = function(){
			var message = "J Inventory Detail Created Successfully, add more?";
			var url = "/awacp/saveJInventory";
			var update = false;
			if(splInvVm.jinv && splInvVm.jinv.id){
				message = "J Inventory Detail Updated Successfully";
				splInvVm.jinv.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateJInventory";
				update = true;
			}else{
				splInvVm.jinv.createdById = StoreService.getUser().userId;
				splInvVm.jinv.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["jinv"] = splInvVm.jinv;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){splInvVm.cancelJInventoryAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){splInvVm.cancelJInventoryAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				jqXHR.errorSource = "SplInventoryCtrl.js::splInvVm.addJInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		splInvVm.editJInventory = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["jinv"] = splInvVm.jinv;
				AjaxUtil.getData("/awacp/getJInventory/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.jinv){
						$scope.$apply(function(){
							splInvVm.jinv = data.jinv;	
							splInvVm.action = splInvVm.jinv && splInvVm.jinv.id?"Update":"Add";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "SplInventoryCtrl.js::splInvVm.editJInventory::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		splInvVm.deleteJInventory = function(id){
			AjaxUtil.getData("/awacp/deleteJInventory/"+id, Math.random())
			.success(function(data, status, headers){
				splInvVm.totalItems = (splInvVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'J Inventory Detail Deleted Successfully.')
					.then(function (){splInvVm.getJInventories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete J Inventory Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "SplInventoryCtrl.js::splInvVm.deleteJInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		splInvVm.getJInventories = function(){
			splInvVm.jinvs = [];
			splInvVm.pageNumber = splInvVm.currentPage;
			AjaxUtil.getData("/awacp/listJInventories/"+splInvVm.pageNumber+"/"+splInvVm.pageSize, Math.random())
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
						splInvVm.jinvs = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::splInvVm.getJInventories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		splInvVm.editJInventory();
	}		
})();



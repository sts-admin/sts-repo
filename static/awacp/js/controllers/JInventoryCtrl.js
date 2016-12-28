(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('JInventoryCtrl', JInventoryCtrl);
	JInventoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function JInventoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var jinvVm = this;
		jinvVm.action = "Add";
	    jinvVm.totalItems = -1;
		jinvVm.currentPage = 1;
		jinvVm.pageNumber = 1;
		jinvVm.pageSize = 1;
		$scope.timers = [];
		jinvVm.jinvs= [];
		jinvVm.jinv = {};
		
		jinvVm.pageChanged = function() {
			jinvVm.getJInventories();
		}		
		jinvVm.cancelJInventoryAction = function(){
			$state.go("jinvs");		
		}
		jinvVm.addOrUpdateJInventory = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');
		}
		
		
		jinvVm.addJInventory = function(){
			var message = "J Inventory Detail Created Successfully, add more?";
			var url = "/awacp/saveJInventory";
			var update = false;
			if(jinvVm.jinv && jinvVm.jinv.id){
				message = "J Inventory Detail Updated Successfully";
				jinvVm.jinv.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateJInventory";
				update = true;
			}else{
				jinvVm.jinv.createdById = StoreService.getUser().userId;
				jinvVm.jinv.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["jinv"] = jinvVm.jinv;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){jinvVm.cancelJInventoryAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){jinvVm.cancelJInventoryAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				jqXHR.errorSource = "JInventoryCtrl::jinvVm.addJInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		jinvVm.editJInventory = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["jinv"] = jinvVm.jinv;
				AjaxUtil.getData("/awacp/getJInventory/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.jinv){
						$scope.$apply(function(){
							jinvVm.jinv = data.jinv;	
							jinvVm.action = jinvVm.jinv && jinvVm.jinv.id?"Update":"Add";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "JInventoryCtrl::jinvVm.editJInventory::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		jinvVm.deleteJInventory = function(id){
			AjaxUtil.getData("/awacp/deleteJInventory/"+id, Math.random())
			.success(function(data, status, headers){
				jinvVm.totalItems = (jinvVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'J Inventory Detail Deleted Successfully.')
					.then(function (){jinvVm.getJInventories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete J Inventory Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "JInventoryCtrl::jinvVm.deleteJInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		jinvVm.getJInventories = function(){
			jinvVm.jinvs = [];
			jinvVm.pageNumber = jinvVm.currentPage;
			AjaxUtil.getData("/awacp/listJInventories/"+jinvVm.pageNumber+"/"+jinvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						jinvVm.totalItems = data.stsResponse.totalCount;
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
						jinvVm.jinvs = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::jinvVm.getJInventories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		jinvVm.editJInventory();
	}		
})();



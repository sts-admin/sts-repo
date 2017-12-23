(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('InvMultiplierCtrl', InvMultiplierCtrl);
	InvMultiplierCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function InvMultiplierCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var invMultiVm = this;
	   
		$scope.timers = [];
		invMultiVm.invMultipliers= [];
		invMultiVm.invMultiplier = {};
		invMultiVm.action = "Add";
		invMultiVm.totalItems = -1;
		invMultiVm.currentPage = 1;
		invMultiVm.pageNumber = 1;
		invMultiVm.pageSize = 20;
		invMultiVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		invMultiVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("INV_MULTIPLIER", size, function(status, size){
				if("success" === status){
					invMultiVm.pageSize = size;
					invMultiVm.pageChanged();
				}
			});
		}
		
		invMultiVm.getPageSize = function(){
			AjaxUtil.getPageSize("INV_MULTIPLIER", function(status, size){
				if("success" === status){
					invMultiVm.pageSize = size;
				}
			});
		}
		invMultiVm.addInvMultiplier = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/inv-multiplier-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.inventoryName = "";
					$scope.multiplierValue = "";
					$scope.value = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.inventoryName || $scope.inventoryName.length <= 0){
							$scope.message = "Please Enter Multiplier Detail.";
							return;
						}
						if(!$scope.multiplierValue || $scope.multiplierValue.length <= 0){
							$scope.message = "Please Enter Multiplier Value.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, invMultiplier = {};
						invMultiplier["createdById"] = StoreService.getUser().userId;
						invMultiplier["inventoryName"] = $scope.inventoryName;
						invMultiplier["multiplierValue"] = $scope.multiplierValue;
						invMultiplier["createdById"] = StoreService.getUser().userId;
						invMultiplier["createdByUserCode"] = StoreService.getUser().userCode;
						invMultiplier["auditMessage"] = "Created INV Multiplier for INV '"+ $scope.inventoryName + "'";
						
						formData["invMultiplier"] = invMultiplier;
						AjaxUtil.submitData("/awacp/saveInvMultiplier", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Multiplier Detail Added Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								invMultiVm.getInvMultipliers();
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');	
							if(1200 == jqXHR.status){
								$scope.message = "Duplicate Multiplier";
							}else{
								$scope.message = "";
								qXHR.errorSource = "InvMultiplierCtrl::invMultiVm.editInvMultiplier::Error";
								AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
							}
							
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
				}
			});
			return defer.promise;
		}		
		invMultiVm.pageChanged = function() {
			invMultiVm.getInvMultipliers();
		};		
		invMultiVm.cancelInvMultiplier = function(){
		}
		invMultiVm.deleteInvMultiplier = function(id){
			AjaxUtil.getData("/awacp/deleteInvMultiplier/"+id, Math.random())
			.success(function(data, status, headers){
				invMultiVm.totalItems = (invMultiVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Multiplier Detail Deleted Successfully.')
					.then(function (){invMultiVm.getInvMultipliers();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete Multiplier Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "InvMultiplierCtrl::invMultiVm.editInvMultiplier::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		invMultiVm.editInvMultiplier = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/inv-multiplier-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.invMultiplier = {};
					$scope.title = title;
					$scope.inventoryName = "";
					$scope.message = "";
					$scope.multiplierValue = "";
					$scope.save = function (){
						if(!$scope.inventoryName || $scope.inventoryName.length <= 0){
							$scope.message = "Please Enter Multiplier Address Detail.";
							return;
						}
						if(!$scope.multiplierValue || $scope.multiplierValue.length <= 0){
							$scope.message = "Please Enter Multiplier Value.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.invMultiplier.inventoryName = $scope.inventoryName;
						$scope.invMultiplier.multiplierValue = $scope.multiplierValue;
						$scope.invMultiplier.updatedById = StoreService.getUser().userId;
						$scope.invMultiplier.updatedByUserCode = StoreService.getUser().userCode;
						$scope.invMultiplier.auditMessage = "Updated INV Multiplier for INV '"+ $scope.inventoryName + "'";
						formData["invMultiplier"] = $scope.invMultiplier;
						AjaxUtil.submitData("/awacp/updateInvMultiplier", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Multiplier Detail Updated Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								invMultiVm.getInvMultipliers();
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){		
								jQuery(".actions").removeAttr('disabled');
								jQuery(".spinner").css('display','none');							
							if(1200 == jqXHR.status){
								$scope.message = "Duplicate Multiplier";
							}else{
								$scope.message = "";
								qXHR.errorSource = "InvMultiplierCtrl::invMultiVm.editInvMultiplier::Error";
								AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
							}
							
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editInvMultiplier = function(id){
						AjaxUtil.getData("/awacp/getInvMultiplier/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.invMultiplier){
								$scope.invMultiplier = data.invMultiplier;
								$scope.inventoryName = data.invMultiplier.inventoryName;
								$scope.multiplierValue = data.invMultiplier.multiplierValue;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "InvMultiplierCtrl::invMultiVm.editInvMultiplier::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editInvMultiplier(id);
				}
			});
			return defer.promise;
		}	
		invMultiVm.getInvMultipliers = function(){
			invMultiVm.invMultipliers = [];
			invMultiVm.pageNumber = invMultiVm.currentPage;
			AjaxUtil.getData("/awacp/listInvMultipliers/"+invMultiVm.pageNumber+"/"+invMultiVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						invMultiVm.totalItems = data.stsResponse.totalCount;
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
						invMultiVm.invMultipliers = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "InvMultiplierCtrl::invMultiVm.getInvMultipliers::Error";
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



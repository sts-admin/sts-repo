(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ItemShipCtrl', ItemShipCtrl);
	ItemShipCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ItemShipCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var itemVm = this;
	    
		$scope.timers = [];
		itemVm.ItemShippeds= [];
		itemVm.ItemShipped = {};
		itemVm.action = "Add";
		itemVm.totalItems = -1;
		itemVm.currentPage = 1;
		itemVm.pageNumber = 1;
		itemVm.pageSize = 20;
		itemVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		itemVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("ITEM_SHIPPED", size, function(status, size){
				if("success" === status){
					itemVm.pageSize = size;
					itemVm.pageChanged();
				}
			});
		}
		
		itemVm.getPageSize = function(){
			AjaxUtil.getPageSize("ITEM_SHIPPED", function(status, size){
				if("success" === status){
					itemVm.pageSize = size;
				}
			});
		}
		itemVm.addItemShip = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/itemship-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.item = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.item || $scope.item.length <= 0){
							$scope.message = "Please Enter Item Shipping Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, itemShipped = {};
						itemShipped["createdById"] = StoreService.getUser().userId;
						itemShipped["item"] = $scope.item;
						itemShipped["createdByUserCode"] = StoreService.getUser().userCode;
						itemShipped["auditMessage"] = "Added Item Ship Detail for the item# "+ $scope.item;
						
						formData["itemShipped"] = itemShipped;
						AjaxUtil.submitData("/awacp/saveItemShipped", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Item Shipping Detail Added Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								itemVm.getItemShippeds();
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ItemShipCtrl::itemVm.addItemShip::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
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
		itemVm.pageChanged = function() {
			itemVm.getItemShippeds();
		};		
		itemVm.cancelItemShipped = function(){
		}
		itemVm.deleteItemShipped = function(id){
			AjaxUtil.getData("/awacp/deleteItemShipped/"+id, Math.random())
			.success(function(data, status, headers){
				itemVm.totalItems = (itemVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Item Shipping Detail Deleted Successfully.')
					.then(function (){itemVm.getItemShippeds();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete Item Shipping Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ItemShippedCtrl::itemVm.deleteItemShipped::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		itemVm.editItemShipped = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/itemship-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.itemShipped = {};
					$scope.title = title;
					$scope.item = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.item || $scope.item.length <= 0){
							$scope.message = "Please Enter Item Shipping Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.itemShipped.item = $scope.item;
						$scope.itemShipped.updatedById = StoreService.getUser().userId;
						$scope.itemShipped.updatedByUserCode = StoreService.getUser().userCode;
						$scope.itemShipped.auditMessage = "Updated Item Ship Detail for the item# "+ $scope.item;
						formData["itemShipped"] = $scope.itemShipped;
						AjaxUtil.submitData("/awacp/updateItemShipped", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Shipping Detail Updated Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								itemVm.getItemShippeds();
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ItemShippedCtrl::itemVm.updateItemShipped::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editItemShipped = function(id){
						AjaxUtil.getData("/awacp/getItemShipped/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.itemShipped){
								$scope.itemShipped = data.itemShipped;
								$scope.item = data.itemShipped.item;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "ItemShippedCtrl::itemVm.editItemShipped::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editItemShipped(id);
				}
			});
			return defer.promise;
		}	
		itemVm.getItemShippeds = function(){
			itemVm.ItemShippeds = [];
			itemVm.pageNumber = itemVm.currentPage;
			AjaxUtil.getData("/awacp/listItemShippeds/"+itemVm.pageNumber+"/"+itemVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						itemVm.totalItems = data.stsResponse.totalCount;
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
						itemVm.ItemShippeds = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ItemShippedCtrl::itemVm.getItemShippeds::Error";
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



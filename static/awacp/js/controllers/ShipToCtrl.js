(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ShipToCtrl', ShipToCtrl);
	ShipToCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ShipToCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var shipToVm = this;
	    
		$scope.timers = [];
		shipToVm.shipTos= [];
		shipToVm.shipTo = {};
		shipToVm.action = "Add";
		shipToVm.totalItems = -1;
		shipToVm.currentPage = 1;
		shipToVm.pageNumber = 1;
		shipToVm.pageSize = 20;
		shipToVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		shipToVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("SHIP_TO", size, function(status, size){
				if("success" === status){
					shipToVm.pageSize = size;
					shipToVm.pageChanged();
				}
			});
		}
		
		shipToVm.getPageSize = function(){
			AjaxUtil.getPageSize("SHIP_TO", function(status, size){
				if("success" === status){
					shipToVm.pageSize = size;
				}
			});
		}
		shipToVm.addShipTo = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/shipto-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.shipToAddress = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.shipToAddress || $scope.shipToAddress.length <= 0){
							$scope.message = "Please Enter Shipping Address Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, shipTo = {};
						shipTo["createdById"] = StoreService.getUser().userId;
						shipTo["shipToAddress"] = $scope.shipToAddress;
						shipTo["createdByUserCode"] = StoreService.getUser().userCode;
						shipTo["auditMessage"] = "Added Ship To Detail '"+ $scope.shipToAddress + "'";
						formData["shipTo"] = shipTo;
						AjaxUtil.submitData("/awacp/saveShipTo", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Shipping Detail Added Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								shipToVm.getShipTos();
								$scope.message = "";								
							}, 1000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ShipToCtrl::shipToVm.addShipTo::Error";
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
		shipToVm.pageChanged = function() {
			shipToVm.getShipTos();
		};		
		shipToVm.cancelShipTo = function(){
		}
		shipToVm.deleteShipTo = function(id){
			AjaxUtil.getData("/awacp/deleteShipTo/"+id, Math.random())
			.success(function(data, status, headers){
				shipToVm.totalItems = (shipToVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Ship To Detail Deleted Successfully.')
					.then(function (){shipToVm.getShipTos();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete Ship To Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ShipToCtrl::shipToVm.editShipTo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		shipToVm.editShipTo = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/shipto-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.shipTo = {};
					$scope.title = title;
					$scope.shipToAddress = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.shipToAddress || $scope.shipToAddress.length <= 0){
							$scope.message = "Please Enter Shipping Address Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.shipTo.shipToAddress = $scope.shipToAddress;
						$scope.shipTo.updatedById = StoreService.getUser().userId;
						$scope.shipTo.updatedByUserCode = StoreService.getUser().userCode;
						$scope.shipTo.auditMessage = "Updated Ship to detail '"+ $scope.shipToAddress + "'";
						formData["shipTo"] = $scope.shipTo;
						AjaxUtil.submitData("/awacp/updateShipTo", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Shipping Detail Updated Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								shipToVm.getShipTos();
								$scope.message = "";
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ShipToCtrl::shipToVm.updateShipTo::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editShipTo = function(id){
						AjaxUtil.getData("/awacp/getShipTo/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.shipTo){
								$scope.shipTo = data.shipTo;
								$scope.shipToAddress = data.shipTo.shipToAddress;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "ShipToCtrl::shipToVm.editShipTo::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editShipTo(id);
				}
			});
			return defer.promise;
		}	
		shipToVm.getShipTos = function(){
			shipToVm.shipTos = [];
			shipToVm.pageNumber = shipToVm.currentPage;
			AjaxUtil.getData("/awacp/listShipTos/"+shipToVm.pageNumber+"/"+shipToVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						shipToVm.totalItems = data.stsResponse.totalCount;
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
						shipToVm.shipTos = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ShipToCtrl::shipToVm.getShipTos::Error";
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



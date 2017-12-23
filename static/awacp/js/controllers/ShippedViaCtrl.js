(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ShippedViaCtrl', ShippedViaCtrl);
	ShippedViaCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ShippedViaCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var shipViaVm = this;
	    
		$scope.timers = [];
		shipViaVm.shippedVias= [];
		shipViaVm.shippedVia = {};
		shipViaVm.action = "Add";
		shipViaVm.totalItems = -1;
		shipViaVm.currentPage = 1;
		shipViaVm.pageNumber = 1;
		shipViaVm.pageSize = 20;
		shipViaVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		shipViaVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("SHIP_VIA", size, function(status, size){
				if("success" === status){
					shipViaVm.pageSize = size;
					shipViaVm.pageChanged();
				}
			});
		}
		
		shipViaVm.getPageSize = function(){
			AjaxUtil.getPageSize("SHIP_VIA", function(status, size){
				if("success" === status){
					shipViaVm.pageSize = size;
				}
			});
		}
		shipViaVm.addShipVia = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/shippedvia-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.shippedViaAddress = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.shippedViaAddress || $scope.shippedViaAddress.length <= 0){
							$scope.message = "Please Enter Shipping Via Address Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, shippedVia = {};
						shippedVia["createdById"] = StoreService.getUser().userId;
						shippedVia["shippedViaAddress"] = $scope.shippedViaAddress;
						shippedVia["createdByUserCode"] = StoreService.getUser().userCode;
						shippedVia["auditMessage"] = "Added Shipped Via '" + $scope.shippedViaAddress + "'"; 
						formData["shippedVia"] = shippedVia;
						AjaxUtil.submitData("/awacp/saveShippedVia", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Shipping Via Detail Added Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								shipViaVm.getShippedVias();
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ShippedViaCtrl::shipViaVm.addShipVia::Error";
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
		shipViaVm.pageChanged = function() {
			shipViaVm.getShippedVias();
		};		
		shipViaVm.cancelShippedVia = function(){
		}
		shipViaVm.deleteShippedVia = function(id){
			AjaxUtil.getData("/awacp/deleteShippedVia/"+id, Math.random())
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!', 'Shipped Via Detail Deleted Successfully.')
					.then(function (){shipViaVm.getShippedVias();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Shipped Via Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ShippedViaCtrl::shipViaVm.deleteShippedVia::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		shipViaVm.editShippedVia = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/shippedvia-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.shippedVia = {};
					$scope.title = title;
					$scope.shippedViaAddress = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.shippedViaAddress || $scope.shippedViaAddress.length <= 0){
							$scope.message = "Please Enter Shipped Via Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.shippedVia.shippedViaAddress = $scope.shippedViaAddress;
						$scope.shippedVia.updatedById = StoreService.getUser().userId;
						$scope.shippedVia.updatedByUserCode = StoreService.getUser().userCode;
						$scope.shippedVia.auditMessage = "Updated Shipped Via '"+$scope.shippedViaAddress+"'";
						formData["shippedVia"] = $scope.shippedVia;
						AjaxUtil.submitData("/awacp/updateShippedVia", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Shipped Via Detail Updated Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								shipViaVm.getShippedVias();
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ShippedViaCtrl::shipViaVm.editShippedVia::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editShippedVia = function(id){
						AjaxUtil.getData("/awacp/getShippedVia/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.shippedVia){
								$scope.shippedVia = data.shippedVia;
								$scope.shippedViaAddress = data.shippedVia.shippedViaAddress;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "ShippedViaCtrl::$scope.editShippedVia::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editShippedVia(id);
				}
			});
			return defer.promise;
		}	
		shipViaVm.getShippedVias = function(){
			shipViaVm.shippedVias = [];
			shipViaVm.pageNumber = shipViaVm.currentPage;
			AjaxUtil.getData("/awacp/listShippedVias/"+shipViaVm.pageNumber+"/"+shipViaVm.pageSize, Math.random())
			.success(function(data, status, headers){
				console.log(data.stsResponse);
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						shipViaVm.totalItems = data.stsResponse.totalCount;
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
						shipViaVm.shippedVias = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ShippedViaCtrl::shipViaVm.getShippedVias::Error";
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



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ShipToCtrl', ShipToCtrl);
	ShipToCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ShipToCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var shipToVm = this;
	    shipToVm.totalItems = -1;
		shipToVm.currentPage = 1;
		shipToVm.pageNumber = 1;
		shipToVm.pageSize = 5;
		$scope.timers = [];
		shipToVm.shipTos= [];
		shipToVm.shipTo = {};
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
							$scope.message = "Please enter Shipping Address Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, shipTo = {};
						shipTo["createdById"] = StoreService.getUser().id;
						shipTo["shipToAddress"] = $scope.shipToAddress;
						shipTo["createdByUserCode"] = StoreService.getUser().userCode;
						formData["shipTo"] = shipTo;
						AjaxUtil.submitData("/awacp/saveShipTo", formData)
						.success(function(data, status, headers){
							$(".actions").removeAttr('disabled');
							$(".spinner").css('display','none');
							$scope.message = "Shipping Detail Added Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								shipToVm.getShipTos();
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							$(".actions").removeAttr('disabled');
							$(".spinner").css('display','none');
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
		};		
		shipToVm.cancelShipTo = function(){
		}		
		shipToVm.editShipTo = function(){
			if($state.params.id != undefined){				
			}
		}		
		shipToVm.getShipTos = function(){
			shipToVm.shipTos = [];
			shipToVm.pageNumber = shipToVm.currentPage;
			AjaxUtil.getData("/awacp/listShipTos/"+shipToVm.pageNumber+"/"+shipToVm.pageSize, Math.random())
			.success(function(data, status, headers){
				console.log(data.stsResponse);
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



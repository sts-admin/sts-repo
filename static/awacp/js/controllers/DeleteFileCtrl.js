(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('DeleteFileCtrl', DeleteFileCtrl);
	DeleteFileCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function DeleteFileCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var deleteVm = this;
	    deleteVm.totalItems = -1;
		deleteVm.currentPage = 1;
		deleteVm.pageNumber = 1;
		deleteVm.pageSize = 5;
		$scope.timers = [];
		deleteVm.files= [];
		deleteVm.file = {};
		deleteVm.addShipTo = function (title){
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
								deleteVm.getShipTos();
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							$(".actions").removeAttr('disabled');
							$(".spinner").css('display','none');
							jqXHR.errorSource = "ShipToCtrl::deleteVm.addShipTo::Error";
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
		deleteVm.pageChanged = function() {
		};		
		deleteVm.cancelShipTo = function(){
		}		
		deleteVm.editShipTo = function(){
			if($state.params.id != undefined){				
			}
		}		
		deleteVm.getShipTos = function(){
			deleteVm.shipTos = [];
			deleteVm.pageNumber = deleteVm.currentPage;
			AjaxUtil.getData("/awacp/listShipTos/"+deleteVm.pageNumber+"/"+deleteVm.pageSize, Math.random())
			.success(function(data, status, headers){
				console.log(data.stsResponse);
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						deleteVm.totalItems = data.stsResponse.totalCount;
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
						deleteVm.shipTos = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ShipToCtrl::deleteVm.getShipTos::Error";
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



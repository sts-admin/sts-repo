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
					$scope.save = function (){
						if($scope.shipToAddress.length <= 0)return;
						var formData = {}, shipTo = {};
						shipTo["shipToAddress"] = $scope.shipToAddress;
						shipTo["createdByUserCode"] = StoreService.getUser().userCode;
						formData["shipTo"] = shipTo;
						AjaxUtil.submitData("/awacp/saveShipTo", formData)
						.success(function(data, status, headers){
							alert("Shipping Detail Added Successfully");
							modalInstance.dismiss();
							shipToVm.getShipTos();
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
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
			shipToVm.specs = [];			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



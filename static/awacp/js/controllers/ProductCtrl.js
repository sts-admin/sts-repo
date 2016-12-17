(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ProductCtrl', ProductCtrl);
	ProductCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ProductCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var prodVm = this;
	    prodVm.totalItems = -1;
		prodVm.currentPage = 1;
		prodVm.pageNumber = 1;
		prodVm.pageSize = 5;
		$scope.timers = [];
		prodVm.products= [];
		prodVm.product = {};
		prodVm.addProduct = function (title){
				var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/product-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.productName = "";
					$scope.save = function (){
						if($scope.productName.length <= 0)return;
						var formData = {}, product = {};
						product["productName"] = $scope.productName;
						product["createdByUserCode"] = StoreService.getUser().userCode;
						formData["product"] = product;
						AjaxUtil.submitData("/awacp/saveProduct", formData)
						.success(function(data, status, headers){
							alert("Product Detail Added Successfully");
							modalInstance.dismiss();
							prodVm.getShipTos();
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "ShipToCtrl::prodVm.addProduct::Error";
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
		prodVm.pageChanged = function() {
		};		
		prodVm.cancelShipTo = function(){
		}		
		prodVm.editShipTo = function(){
			if($state.params.id != undefined){				
			}
		}		
		prodVm.getShipTos = function(){
			prodVm.specs = [];			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('QuoteNotesCtrl', QuoteNotesCtrl);
	QuoteNotesCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function QuoteNotesCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var qnoteVm = this;
	    qnoteVm.totalItems = -1;
		qnoteVm.currentPage = 1;
		qnoteVm.pageNumber = 1;
		qnoteVm.pageSize = 5;
		$scope.timers = [];
		qnoteVm.products= [];
		qnoteVm.product = {};
		qnoteVm.addQuoteNote = function (title){
				var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/qnote-add.html',
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
							qnoteVm.getShipTos();
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "ShipToCtrl::qnoteVm.addProduct::Error";
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
		qnoteVm.pageChanged = function() {
		};		
		qnoteVm.cancelShipTo = function(){
		}		
		qnoteVm.editShipTo = function(){
			if($state.params.id != undefined){				
			}
		}		
		qnoteVm.getShipTos = function(){
			qnoteVm.specs = [];			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



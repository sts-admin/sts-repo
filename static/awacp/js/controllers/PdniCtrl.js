(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('PdniCtrl', PdniCtrl);
	PdniCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function PdniCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var pdniVm = this;
	    pdniVm.totalItems = -1;
		pdniVm.currentPage = 1;
		pdniVm.pageNumber = 1;
		pdniVm.pageSize = 5;
		$scope.timers = [];
		pdniVm.specs= [];
		pdniVm.pdni = {};
		pdniVm.addPdni = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/pdni-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.pdniName = "";
					$scope.save = function (){
						if($scope.pdni.length <= 0)return;
						var formData = {}, pdni = {};
						pdni["pdni"] = $scope.pdniName;
						pdni["createdByUserCode"] = StoreService.getUser().userCode;
						formData["pdni"] = pdni;
						AjaxUtil.submitData("/awacp/saveSpecification", formData)
						.success(function(data, status, headers){
							alert("Specification Detail Created Successfully");
							modalInstance.dismiss();
							pdniVm.getSpecs();
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "PdniCtrl::conVm.pdniVm.pdniVm::Error";
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
		
		pdniVm.pageChanged = function() {
		};

		
		pdniVm.cancelPdniAction = function(){
		}
		
		pdniVm.editPdni = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["contractor"] = pdniVm.contractor;
				AjaxUtil.getData("/awacp/getSpecification/"+$state.params.id, formData)
				.success(function(data, status, headers){
					if(data && data.pdni){
						$scope.$apply(function(){
							pdniVm.pdni = data.pdni;							
						});						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "PdniCtrl::pdniVm.editSpecification::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		pdniVm.getPdnis = function(){
			pdniVm.specs = [];
			pdniVm.pageNumber = pdniVm.currentPage;
			AjaxUtil.getData("/awacp/listSpecifications/"+pdniVm.pageNumber+"/"+pdniVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						pdniVm.totalItems = data.stsResponse.totalCount;
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
						pdniVm.specs = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "PdniCtrl::pdniVm.getSpecifications::Error";
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



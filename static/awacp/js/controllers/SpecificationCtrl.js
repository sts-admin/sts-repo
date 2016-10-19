(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('SpecificationCtrl', SpecificationCtrl);
	SpecificationCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function SpecificationCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var specVm = this;
	    specVm.totalItems = -1;
		specVm.currentPage = 1;
		specVm.pageNumber = 1;
		specVm.pageSize = 5;
		$scope.timers = [];
		specVm.specs= [];
		specVm.spec = {};
		specVm.addSpec = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/spec-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.specification = "";
					$scope.save = function (){
						if($scope.specification.length <= 0)return;
						var formData = {}, specification = {};
						specification["specification"] = $scope.specification;
						specification["createdByUserCode"] = StoreService.getUser().userCode;
						formData["specification"] = specification;
						AjaxUtil.submitData("/awacp/saveSpecification", formData)
						.success(function(data, status, headers){
							var message = "Specification Detail Created Successfully";
							AlertService.showAlert(	'AWACP :: Alert!', message)
							.then(function (){return;},function (){return false;});
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "SpecificationCtrl::conVm.specVm.specVm::Error";
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
		
		specVm.pageChanged = function() {
			specVm.getContractors();
		};

		
		specVm.cancelSpecAction = function(){
		}
		
		specVm.editSpecification = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["contractor"] = specVm.contractor;
				AjaxUtil.getData("/awacp/getSpecification/"+$state.params.id, formData)
				.success(function(data, status, headers){
					if(data && data.specification){
						$scope.$apply(function(){
							specVm.spec = data.specification;							
						});						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "SpecificationCtrl::specVm.editSpecification::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		specVm.getSpecs = function(){
			specVm.specs = [];
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			specVm.pageNumber = specVm.currentPage;
			AjaxUtil.getData("/awacp/listSpecifications/"+specVm.pageNumber+"/"+specVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						specVm.totalItems = data.stsResponse.totalCount;
					});
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if($.isArray(data.stsResponse.results)) {
						$.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
						});					
					} else {
					    tmp.push(data.stsResponse.results);
					}
					$scope.$apply(function(){
						specVm.specs = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "SpecificationCtrl::specVm.getSpecifications::Error";
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



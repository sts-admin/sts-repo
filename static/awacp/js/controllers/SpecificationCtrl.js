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
					$scope.detail = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.detail || $scope.detail.length <= 0){
							$scope.message = "Please enter Shipping Address Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');	
						var formData = {}, spec = {};
						spec["detail"] = $scope.detail;
						spec["createdById"] = StoreService.getUser().id;
						spec["createdByUserCode"] = StoreService.getUser().userCode;
						formData["spec"] = spec;
						AjaxUtil.submitData("/awacp/saveSpecification", formData)
						.success(function(data, status, headers){
							$scope.message = "Specification Added Successfully";
							$(".actions").removeAttr('disabled');
							$(".spinner").css('display','none');
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								specVm.getSpecs();
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							$(".actions").removeAttr('disabled');
							$(".spinner").css('display','none');
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
					if(data && data.spec){
						$scope.$apply(function(){
							specVm.spec = data.spec;							
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
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
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



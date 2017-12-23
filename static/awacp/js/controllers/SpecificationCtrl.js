(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('SpecificationCtrl', SpecificationCtrl);
	SpecificationCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function SpecificationCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var specVm = this;
	    
		$scope.timers = [];
		specVm.specs= [];
		specVm.spec = {};
		specVm.totalItems = -1;
		specVm.currentPage = 1;
		specVm.pageNumber = 1;
		specVm.pageSize = 20;
		specVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		specVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("SPEC", size, function(status, size){
				if("success" === status){
					specVm.pageSize = size;
					specVm.pageChanged();
				}
			});
		}
		
		specVm.getPageSize = function(){
			AjaxUtil.getPageSize("SPEC", function(status, size){
				if("success" === status){
					specVm.pageSize = size;
				}
			});
		}
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
							$scope.message = "Please Enter Specification Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');	
						var formData = {}, spec = {};
						spec["detail"] = $scope.detail;
						spec["createdById"] = StoreService.getUser().userId;
						spec["createdByUserCode"] = StoreService.getUser().userCode;
						spec["auditMessage"] = "Added Spec with detail '"+ $scope.detail + "'";
						formData["spec"] = spec;
						AjaxUtil.submitData("/awacp/saveSpecification", formData)
						.success(function(data, status, headers){
							$scope.message = "Specification Added Successfully";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$timeout(function(){
								modalInstance.dismiss();
								specVm.getSpecs();
								$scope.message = "";								
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "SpecificationCtrl::specVm.specVm.specVm::Error";
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
			specVm.getSpecs();
		};
		
		specVm.cancelSpecAction = function(){
		}
		
		specVm.deleteSpec = function(id){
			AjaxUtil.getData("/awacp/deleteSpec/"+id, Math.random())
			.success(function(data, status, headers){
				specVm.totalItems = (specVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Specification Detail Deleted Successfully.')
					.then(function (){specVm.getSpecs();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Specification Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "SpecificationToCtrl::specVm.deleteSpec::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		specVm.editSpec = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/spec-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.spec = {};
					$scope.title = title;
					$scope.detail = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.detail || $scope.detail.length <= 0){
							$scope.message = "Please Enter Specification Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.spec.detail = $scope.detail;
						$scope.spec.updatedByUserCode = StoreService.getUser().userCode;
						$scope.spec.updatedById = StoreService.getUser().userId;
						$scope.spec.auditMessage = "Updated Spec with detail '"+ $scope.detail + "'";
						formData["spec"] = $scope.spec;
						AjaxUtil.submitData("/awacp/updateSpecification", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Specification Detail Updated Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								specVm.getSpecs();
								$scope.message = "";								
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "SpecificationCtrl::specVm.updateSpec::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editSpec = function(id){
						AjaxUtil.getData("/awacp/getSpecification/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.spec){
								$scope.spec = data.spec;
								$scope.detail = data.spec.detail;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "SpecificationCtrl::specVm.editSpec::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editSpec(id);
				}
			});
			return defer.promise;
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



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('FactoryCtrl', FactoryCtrl);
	FactoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function FactoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var facVm = this;
	    
		$scope.timers = [];
		facVm.factories= [];
		facVm.factory = {};
		facVm.totalItems = -1;
		facVm.currentPage = 1;
		facVm.pageNumber = 1;
		facVm.pageSize = 20;
		facVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		facVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("FACTORY", size, function(status, size){
				if("success" === status){
					facVm.pageSize = size;
					facVm.pageChanged();
				}
			});
		}
		
		facVm.getPageSize = function(){
			AjaxUtil.getPageSize("FACTORY", function(status, size){
				if("success" === status){
					facVm.pageSize = size;
				}
			});
		}
		facVm.addFactory = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/factory-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;					
					$scope.factoryCode = "";
					$scope.factoryName = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.factoryName || $scope.factoryName.length <= 0){
							$scope.message = "Please Enter Factory Name.";
							return;
						}
						if(!$scope.factoryCode || $scope.factoryCode.length <= 0){
							$scope.message = "Please Enter Factory Code.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');	
						var formData = {}, factory = {};
						factory["factoryName"] = $scope.factoryName;
						factory["factoryCode"] = $scope.factoryCode;
						factory["createdById"] = StoreService.getUser().userId;
						factory["createdByUserCode"] = StoreService.getUser().userCode;
						factory["auditMessage"] = "Created factory with name '" + $scope.factoryName;
						formData["factory"] = factory;
						AjaxUtil.submitData("/awacp/saveFactory", formData)
						.success(function(data, status, headers){
							$scope.message = "Factory Detail Added Successfully";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$timeout(function(){
								modalInstance.dismiss();
								facVm.getFactories();
								$scope.message = "";								
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "FactoryCtrl::facVm.addFactory::Error";
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
		
		facVm.pageChanged = function() {
			facVm.getFactories();
		};
		
		facVm.cancelFactoryAction = function(){
		}
		
		facVm.deleteFactory = function(id){
			AjaxUtil.getData("/awacp/deleteFactory/"+id, Math.random())
			.success(function(data, status, headers){
				facVm.totalItems = (facVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Factory Detail Deleted Successfully.')
					.then(function (){facVm.getFactories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete factory detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "FactoryCtrl::facVm.deleteFactory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		facVm.editFactory = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/factory-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.factory = {};
					$scope.title = title;
					$scope.factoryName = "";
					$scope.factoryCode = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.factoryName || $scope.factoryName.length <= 0){
							$scope.message = "Please Enter Factory Name.";
							return;
						}
						if(!$scope.factoryCode || $scope.factoryCode.length <= 0){
							$scope.message = "Please Enter Factory Code.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.factory.factoryName = $scope.factoryName;
						$scope.factory.factoryCode = $scope.factoryCode;
						$scope.factory.updatedByUserCode = StoreService.getUser().userCode;
						$scope.factory.auditMessage = "Updated factory with name '"+$scope.factoryName+"'";
						formData["factory"] = $scope.factory;
						AjaxUtil.submitData("/awacp/saveFactory", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Factory Detail Updated Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								facVm.getFactories();
								$scope.message = "";								
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "FactoryCtrl::facVm.updateFactory::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editFactory = function(id){
						AjaxUtil.getData("/awacp/getFactory/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.factory){
								$scope.factory = data.factory;
								$scope.factoryName = data.factory.factoryName;
								$scope.factoryCode = data.factory.factoryCode;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "FactoryCtrl::facVm.editFactory::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editFactory(id);
				}
			});
			return defer.promise;
		}	
		
		facVm.getFactories = function(){
			facVm.factories = [];
			facVm.pageNumber = facVm.currentPage;
			AjaxUtil.getData("/awacp/listFactories/"+facVm.pageNumber+"/"+facVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						facVm.totalItems = data.stsResponse.totalCount;
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
						facVm.factories = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "FactoryCtrl::facVm.getFactories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		facVm.initFactories = function(){
			facVm.getFactories();
			facVm.getPageSize();
		}
	}		
})();



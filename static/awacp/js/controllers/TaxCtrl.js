(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TaxCtrl', TaxCtrl);
	TaxCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function TaxCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var taxVm = this;
	   
		$scope.timers = [];
		taxVm.taxes= [];
		taxVm.taxEntry = {};
		taxVm.action = "Add";
		taxVm.totalItems = -1;
		taxVm.currentPage = 1;
		taxVm.pageNumber = 1;
		taxVm.pageSize = 20;
		taxVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		taxVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("TAX", size, function(status, size){
				if("success" === status){
					taxVm.pageSize = size;
					taxVm.pageChanged();
				}
			});
		}
		
		taxVm.getPageSize = function(){
			AjaxUtil.getPageSize("TAX", function(status, size){
				if("success" === status){
					taxVm.pageSize = size;
				}
			});
		}
		taxVm.addTaxEntry = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/taxEntry-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.city = "";
					$scope.rate = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.city || $scope.city.length <= 0){
							$scope.message = "Please Enter City/State/Country Detail.";
							return;
						}
						if(!$scope.rate || $scope.rate.length <= 0){
							$scope.message = "Please Enter Tax Rate";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, taxEntry = {};
						taxEntry["createdById"] = StoreService.getUser().userId;
						taxEntry["city"] = $scope.city;
						taxEntry["rate"] = $scope.rate;
						taxEntry["createdByUserCode"] = StoreService.getUser().userCode;
						taxEntry["auditMessage"] = "Added Tax entry";
						formData["taxEntry"] = taxEntry;
						AjaxUtil.submitData("/awacp/saveTaxEntry", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Tax Detail Added Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								taxVm.getTaxEntries();
								$scope.message = "";								
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "TaxCtrl::taxVm.addTaxEntry::Error";
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
		taxVm.pageChanged = function() {
			taxVm.getTaxEntries();
		};		
		taxVm.cancelTaxEntry = function(){
		}
		taxVm.deleteTaxEntry = function(id){
			AjaxUtil.getData("/awacp/deleteTaxEntry/"+id, Math.random())
			.success(function(data, status, headers){
				taxVm.totalItems = (taxVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Tax Entry Deleted Successfully.')
					.then(function (){taxVm.getTaxEntries();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete Tax Entry.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "TaxCtrl::taxVm.editTaxEntry::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		taxVm.editTaxEntry = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/taxEntry-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.taxEntry = {};
					$scope.title = title;
					$scope.city = "";
					$scope.rate = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.city || $scope.city.length <= 0){
							$scope.message = "Please Enter City/State/Country Detail.";
							return;
						}
						if(!$scope.rate || $scope.rate.length <= 0){
							$scope.message = "Please Enter Tax Rate";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.taxEntry.city = $scope.city;
						$scope.taxEntry.rate = $scope.rate;
						$scope.taxEntry.updatedById = StoreService.getUser().userId;
						$scope.taxEntry.auditMessage = "Updated Tax Entry";
						formData["taxEntry"] = $scope.taxEntry;
						AjaxUtil.submitData("/awacp/updateTaxEntry", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Tax Detail Updated Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								taxVm.getTaxEntries();
								$scope.message = "";								
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "TaxCtrl::taxVm.updateTaxEntry::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editTaxEntry = function(id){
						AjaxUtil.getData("/awacp/getTaxEntry/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.taxEntry){
								$scope.taxEntry = data.taxEntry;
								$scope.city = data.taxEntry.city;
								$scope.rate = data.taxEntry.rate;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "TaxCtrl::taxVm.editTaxEntry::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editTaxEntry(id);
				}
			});
			return defer.promise;
		}	
		taxVm.getTaxEntries = function(){
			taxVm.taxes = [];
			taxVm.pageNumber = taxVm.currentPage;
			AjaxUtil.getData("/awacp/listTaxEntries/"+taxVm.pageNumber+"/"+taxVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						taxVm.totalItems = data.stsResponse.totalCount;
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
						taxVm.taxes = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TaxCtrl::taxVm.getTaxEntries::Error";
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



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ManufactureCtrl', ManufactureCtrl);
	ManufactureCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ManufactureCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var manuVm = this;
	   
		$scope.timers = [];
		manuVm.manufactures= [];
		manuVm.manufacture = {};
		manuVm.action = "Add";
		manuVm.totalItems = -1;
		manuVm.currentPage = 1;
		manuVm.pageNumber = 1;
		manuVm.pageSize = 20;
		manuVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		manuVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("M&D", size, function(status, size){
				if("success" === status){
					manuVm.pageSize = size;
					manuVm.pageChanged();
				}
			});
		}
		
		manuVm.getPageSize = function(){
			AjaxUtil.getPageSize("M&D", function(status, size){
				if("success" === status){
					manuVm.pageSize = size;
				}
			});
		}
		manuVm.addManufacture = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/manufacture-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.productName = "";
					$scope.wsMultiplier = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.productName || $scope.productName.length <= 0){
							$scope.message = "Please Enter Manufacture Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, mnD = {};
						mnD["createdById"] = StoreService.getUser().userId;
						mnD["productName"] = $scope.productName;
						mnD["wsMultiplier"] = $scope.wsMultiplier;
						mnD["createdByUserCode"] = StoreService.getUser().userCode;
						mnD["auditMessage"] = "Created Manufacturer with name '"+$scope.productName+"'";
						
						formData["mnD"] = mnD;
						AjaxUtil.submitData("/awacp/saveMnD", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Manufacture Detail Added Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								manuVm.getManufactures();
								$scope.message = "";
							}, 1000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ManufactureCtrl::manuVm.Manufacture::Error";
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
		manuVm.pageChanged = function() {
			manuVm.getManufactures();
		};		
		manuVm.cancelManufacture = function(){
		}
		manuVm.deleteManufacture = function(id){
			AjaxUtil.getData("/awacp/deleteMnD/"+id, Math.random())
			.success(function(data, status, headers){
				manuVm.totalItems = (manuVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Manufacture Detail Deleted Successfully.')
					.then(function (){manuVm.getManufactures();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete Ship To Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ManufactureCtrl::manuVm.editManufacture::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		manuVm.editManufacture = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/manufacture-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.manufacture = {};
					$scope.title = title;
					$scope.productName = 
					$scope.wsMultiplier = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.productName || $scope.productName.length <= 0){
							$scope.message = "Please Enter Manufacture Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.manufacture.productName = $scope.productName;
						$scope.manufacture.wsMultiplier = $scope.wsMultiplier;
						$scope.manufacture.updatedById = StoreService.getUser().userId;
						$scope.manufacture.updatedByUserCode = StoreService.getUser().userCode;
						$scope.manufacture.auditMessage = "Updated Manufacturer with name '"+$scope.productName+"'";
						formData["mnD"] = $scope.manufacture;
						AjaxUtil.submitData("/awacp/updateMnD", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Manufacture Detail Updated Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								manuVm.getManufactures();
								$scope.message = "";								
							}, 1000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ManufactureCtrl::manuVm.updateManufacture::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editManufacture = function(id){
						AjaxUtil.getData("/awacp/getMnD/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.mnD){
								$scope.manufacture = data.mnD;
								$scope.productName = data.mnD.productName;
								$scope.wsMultiplier = data.mnD.wsMultiplier;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "ManufactureCtrl::manuVm.editManufacture::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editManufacture(id);
				}
			});
			return defer.promise;
		}	
		manuVm.getManufactures = function(){
			manuVm.manufactures = [];
			manuVm.pageNumber = manuVm.currentPage;
			AjaxUtil.getData("/awacp/listMnDs/"+manuVm.pageNumber+"/"+manuVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						manuVm.totalItems = data.stsResponse.totalCount;
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
						manuVm.manufactures = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ManufactureCtrl::manuVm.getManufactures::Error";
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



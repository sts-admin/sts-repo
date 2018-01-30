(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('PdniCtrl', PdniCtrl);
	PdniCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function PdniCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var pdniVm = this;
		
		$scope.timers = [];
		pdniVm.pdnis= [];
		pdniVm.pdni = {};
		pdniVm.action = "Add";
		pdniVm.totalItems = -1;
		pdniVm.currentPage = 1;
		pdniVm.pageNumber = 1;
		pdniVm.pageSize = 20;
		pdniVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		pdniVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("PNDI", size, function(status, size){
				if("success" === status){
					pdniVm.pageSize = size;
					pdniVm.pageChanged();
				}
			});
		}
		
		pdniVm.getPageSize = function(){
			AjaxUtil.getPageSize("PNDI", function(status, size){
				if("success" === status){
					pdniVm.pageSize = size;
				}
			});
		}
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
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.pdniName || $scope.pdniName.length <= 0){
							$scope.message = "Please Enter PDNI Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, pdni = {};
						pdni["createdById"] = StoreService.getUser().userId;
						pdni["pdniName"] = $scope.pdniName;
						pdni["createdByUserCode"] = StoreService.getUser().userCode;
						pdni["auditMessage"] = "Added PDNI with name '"+$scope.pdniName+"'";
						formData["pdni"] = pdni;
						AjaxUtil.submitData("/awacp/savePdni", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "PDNI Detail Added Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								pdniVm.getPdnis();
							}, 1000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "PdniCtrl::pdniVm.addPdni::Error";
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
			pdniVm.getPdnis();
		};		
		pdniVm.cancelPdni = function(){
		}
		pdniVm.deletePdni = function(id){
			AjaxUtil.getData("/awacp/deletePdni/"+id, Math.random())
			.success(function(data, status, headers){
				pdniVm.totalItems = (pdniVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'PDNI Detail Deleted Successfully.')
					.then(function (){pdniVm.getPdnis();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete PDNI Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "PdniCtrl::pdniVm.editPdni::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		pdniVm.editPdni = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/pdni-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.pdni = {};
					$scope.title = title;
					$scope.pdniName = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.pdniName || $scope.pdniName.length <= 0){
							$scope.message = "Please Enter PDNI Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.pdni.pdniName = $scope.pdniName;
						$scope.pdni.updatedById = StoreService.getUser().userId;
						$scope.pdni.updatedByUserCode = StoreService.getUser().userCode;
						$scope.pdni.auditMessage = "Updated PDNI with name '"+$scope.pdniName+"'";
						formData["pdni"] = $scope.pdni;
						AjaxUtil.submitData("/awacp/updatePdni", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "PDNI Detail Updated Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								pdniVm.getPdnis();
							}, 1000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "PdniCtrl::pdniVm.updatePdni::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.getPdniDetail = function(id){
						AjaxUtil.getData("/awacp/getPdni/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.pdni){
								$scope.pdni = data.pdni;
								$scope.pdniName = data.pdni.pdniName;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "PdniCtrl::pdniVm.editPdni::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.getPdniDetail(id);
				}
			});
			return defer.promise;
		}	
		pdniVm.getPdnis = function(){
			pdniVm.pdnis = [];
			pdniVm.pageNumber = pdniVm.currentPage;
			AjaxUtil.getData("/awacp/listPdnis/"+pdniVm.pageNumber+"/"+pdniVm.pageSize, Math.random())
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
						pdniVm.pdnis = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "PdniCtrl::pdniVm.getPdnis::Error";
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



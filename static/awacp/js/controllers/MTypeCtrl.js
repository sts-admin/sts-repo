(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('MTypeCtrl', MTypeCtrl);
	MTypeCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function MTypeCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var mTypeVm = this;
		mTypeVm.mndId;
		$scope.timers = [];
		mTypeVm.mTypes= [];
		mTypeVm.mType = {};
		mTypeVm.action = "Add";
		mTypeVm.totalItems = -1;
		mTypeVm.currentPage = 1;
		mTypeVm.pageNumber = 1;
		mTypeVm.pageSize = 20;
		mTypeVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		mTypeVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("MTYPE", size, function(status, size){
				if("success" === status){
					mTypeVm.pageSize = size;
					mTypeVm.pageChanged();
				}
			});
		}
		
		mTypeVm.getPageSize = function(){
			AjaxUtil.getPageSize("MTYPE", function(status, size){
				if("success" === status){
					mTypeVm.pageSize = size;
				}
			});
		}
		mTypeVm.setMessage = function (id, mndId, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/mtype-message-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.mndTypeId = id;
					$scope.mndId = mndId;
					$scope.mnDType = {};
					$scope.title = title;
					$scope.message = "";
					$scope.infoMessage = "";
					$scope.save = function (){
						if(!$scope.message || $scope.message.length <= 0){
							$scope.message = "Please Enter Message";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						
						AjaxUtil.getData("/awacp/setMessage?message="+$scope.message+"&id="+$scope.mndTypeId, Math.random())
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.infoMessage = "Message Detail Updated Successfully";
							$timeout(function(){
								$scope.mnDType = {};
								$scope.message = "";
								$scope.infoMessage = "";
								modalInstance.dismiss();
								mTypeVm.getMTypes($scope.mndId);
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.mnDType = {};
							$scope.message = "";
							$scope.infoMessage = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "MTypeCtrl::mTypeVm.setMessage::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.getMessage = function(id){
						AjaxUtil.getData("/awacp/getMnDType/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.mnDType){
								$scope.mnDType = data.mnDType;
								$scope.message =  data.mnDType.message;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "MTypeCtrl::mTypeVm.editMessage::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.getMessage($scope.mndTypeId);
				}
			});
			return defer.promise;
		}		
		mTypeVm.addMType = function (mndId, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/mtype-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.mndId = mndId;
					$scope.productName = "";
					$scope.model = "";
					$scope.rangeOne = "";
					$scope.rangeTwo = "";
					$scope.rangeThree = "";
					$scope.save = function (){
						if(!$scope.productName || $scope.productName.length <= 0){
							$scope.message = "Please Enter Manufacturing Type Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, mnDType = {};
						mnDType["createdById"] = StoreService.getUser().userId;
						mnDType["productName"] = $scope.productName;
						mnDType["mndId"] = $scope.mndId;
						mnDType["model"] = $scope.model;
						mnDType["rangeOne"] = $scope.rangeOne;
						mnDType["rangeTwo"] = $scope.rangeTwo;
						mnDType["rangeThree"] = $scope.rangeThree;
						mnDType["createdByUserCode"] = StoreService.getUser().userCode;
						mnDType["auditMessage"] = "Created MnD Type for product '"+$scope.productName+"'";
						formData["mnDType"] = mnDType;
						AjaxUtil.submitData("/awacp/saveMnDType", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Product Type Detail Added Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								mTypeVm.getMTypes(mndId);
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "MTypeCtrl::mTypeVm.mType::Error";
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
		mTypeVm.pageChanged = function() {
			mTypeVm.getManufactures();
		};		
		mTypeVm.cancelManufacture = function(){
		}
		mTypeVm.deleteMnDType = function(id, mndId){
			AjaxUtil.getData("/awacp/deleteMnDType/"+id, Math.random())
			.success(function(data, status, headers){
				mTypeVm.totalItems = (mTypeVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Product Type Detail Deleted Successfully.')
					.then(function (){mTypeVm.getMTypes(mndId);},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete Product Type Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "MTypeCtrl::mTypeVm.deleteMnDType::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		mTypeVm.editMType = function (id, mndId, title){
			mTypeVm.mndId = mndId;
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/mType-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.mnDType = {};
					$scope.title = title;
					$scope.productName = "";
					$scope.model = "";
					$scope.rangeOne = "";
					$scope.rangeTwo = "";
					$scope.rangeThree = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.productName || $scope.productName.length <= 0){
							$scope.message = "Please Enter Product Type Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						
						var formData = {};
						$scope.mnDType.productName = $scope.productName;
						$scope.mnDType.model = $scope.model;
						$scope.mnDType.rangeOne = $scope.rangeOne;
						$scope.mnDType.rangeTwo = $scope.rangeTwo;
						$scope.mnDType.rangeThree = $scope.rangeThree;
						$scope.mnDType.updatedById = StoreService.getUser().userId;
						$scope.mnDType.updatedByUserCode = StoreService.getUser().userCode;
						$scope.mnDType.auditMessage = "Updated MnD Type for product '"+$scope.productName+"'";
						formData["mnDType"] = $scope.mnDType;
						AjaxUtil.submitData("/awacp/updateMnDType", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Product Type Detail Updated Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								mTypeVm.getMTypes(mTypeVm.mndId);
							}, 3000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "MTypeCtrl::mTypeVm.updateMType::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editMType = function(id){
						AjaxUtil.getData("/awacp/getMnDType/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.mnDType){
								$scope.mnDType = data.mnDType;
								$scope.productName = data.mnDType.productName;
								$scope.model =  data.mnDType.model;
								$scope.rangeOne =  data.mnDType.rangeOne;
								$scope.rangeTwo =  data.mnDType.rangeTwo;
								$scope.rangeThree =  data.mnDType.rangeThree;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "MTypeCtrl::mTypeVm.editMType::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editMType(id);
				}
			});
			return defer.promise;
		}	
		mTypeVm.getMTypes = function(id){
			mTypeVm.mTypes = [];
			mTypeVm.pageNumber = mTypeVm.currentPage;
			AjaxUtil.getData("/awacp/listMnDTypes/"+id+"/"+mTypeVm.pageNumber+"/"+mTypeVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						mTypeVm.totalItems = data.stsResponse.totalCount;
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
						mTypeVm.mTypes = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "MTypeCtrl::mTypeVm.getMTypes::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		mTypeVm.updateFlag = function(id, flag){
			AjaxUtil.getData("/awacp/setFlag?flag="+flag+"&id="+id, Math.random())
			.success(function(data, status, headers){
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "MTypeCtrl::mTypeVm.updateFlag::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		if($state.params.id != undefined){
			mTypeVm.mndId = $state.params.id;
			mTypeVm.getMTypes($state.params.id);
		}
	}		
})();



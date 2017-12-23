(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ProductCtrl', ProductCtrl);
	ProductCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function ProductCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var prodVm = this;
	   
		$scope.timers = [];
		prodVm.products= [];
		prodVm.product = {};
		prodVm.action = "Add";
		prodVm.totalItems = -1;
		prodVm.currentPage = 1;
		prodVm.pageNumber = 1;
		prodVm.pageSize = 20;
		prodVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		prodVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("PRODUCT", size, function(status, size){
				if("success" === status){
					prodVm.pageSize = size;
					prodVm.pageChanged();
				}
			});
		}
		
		prodVm.getPageSize = function(){
			AjaxUtil.getPageSize("PRODUCT", function(status, size){
				if("success" === status){
					prodVm.pageSize = size;
				}
			});
		}
		prodVm.addProduct = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/product-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.productName = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.productName || $scope.productName.length <= 0){
							$scope.message = "Please Enter Product Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, product = {};
						product["createdById"] = StoreService.getUser().userId;
						product["productName"] = $scope.productName;
						product["createdByUserCode"] = StoreService.getUser().userCode;
						product["auditMessage"] = "Added product with name '"+$scope.productName+"'";
						formData["product"] = product;
						AjaxUtil.submitData("/awacp/saveProduct", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Product Detail Added Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								prodVm.getProducts();
								$scope.message = "";								
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							if(1003 == jqXHR.status){
								AlertService.showAlert(	'AWACP :: Alert!', "A Product with this name already exist, please use a different name.")
								.then(function (){return},function (){return});
								return;
							}else{
								jqXHR.errorSource = "ProductCtrl::engVm.addProduct::Error";
								AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
							}
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
		prodVm.pageChanged = function() {
			prodVm.getProducts();
		};		
		prodVm.cancelProduct = function(){
		}
		prodVm.deleteProduct = function(id){
			AjaxUtil.getData("/awacp/deleteProduct/"+id, Math.random())
			.success(function(data, status, headers){
				prodVm.totalItems = (prodVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Product Detail Deleted Successfully.')
					.then(function (){prodVm.getProducts();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to delete Product Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ProductCtrl::prodVm.editProduct::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		prodVm.editProduct = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/product-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.product = {};
					$scope.title = title;
					$scope.productName = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.productName || $scope.productName.length <= 0){
							$scope.message = "Please Enter Product Address Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.product.productName = $scope.productName;
						$scope.product.updatedById = StoreService.getUser().userId;
						$scope.product.updatedByUserCode = StoreService.getUser().userCode;
						$scope.product.auditMessage = "Updated product with name '"+$scope.productName+"'";
						formData["product"] = $scope.product;
						AjaxUtil.submitData("/awacp/updateProduct", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Product Detail Updated Successfully";
							$timeout(function(){
								modalInstance.dismiss();
								prodVm.getProducts();
								$scope.message = "";								
							}, 2000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "ProductCtrl::prodVm.updateProduct::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editProduct = function(id){
						AjaxUtil.getData("/awacp/getProduct/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.product){
								$scope.product = data.product;
								$scope.productName = data.product.productName;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "ProductCtrl::prodVm.editProduct::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editProduct(id);
				}
			});
			return defer.promise;
		}	
		prodVm.getProducts = function(){
			prodVm.products = [];
			prodVm.pageNumber = prodVm.currentPage;
			AjaxUtil.getData("/awacp/listProducts/"+prodVm.pageNumber+"/"+prodVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						prodVm.totalItems = data.stsResponse.totalCount;
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
						prodVm.products = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ProductCtrl::prodVm.getProducts::Error";
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



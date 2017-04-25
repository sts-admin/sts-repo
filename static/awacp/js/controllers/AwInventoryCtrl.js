(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('AwInventoryCtrl', AwInventoryCtrl);
	AwInventoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService', 'FileService'];
	function AwInventoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService, FileService){
		var awInvVm = this;
		awInvVm.action = "Add";
	    
		$scope.timers = [];
		awInvVm.awInventories= [];
		awInvVm.awInventory = {};
		
		awInvVm.totalItems = -1;
		awInvVm.currentPage = 1;
		awInvVm.pageNumber = 1;
		awInvVm.pageSize = 20;
		awInvVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		
		awInvVm.documents = [];
		
		awInvVm.listDocuments = function (source, sourceId){
			awInvVm.documents = [];
			AjaxUtil.getData("/awacp/listFilesBySource/"+ source + "/"+sourceId, Math.random())
			.success(function(data, status, headers){
				if(data && data.file && data.file.length > 0){										
					$.each(data.file, function(k, v){
						awInvVm.documents.push(v);
					});
				}
				$scope.$digest();
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UploadService::listDocuments::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		};
		
		awInvVm.toggleImageContainer = function(invId){
			awInvVm.listDocuments("inv_aw_image_doc", invId);
			for(var i = 0; i < awInvVm.awInventories.length; i++){
				if(awInvVm.awInventories[i].id == invId){
					awInvVm.awInventories[i].openImageContainer = !awInvVm.awInventories[i].openImageContainer;
				}else{
					awInvVm.awInventories[i].openImageContainer = false;
				}
			}
		}
		
		awInvVm.showFileListingView = function(source, sourceId, title, size, filePattern){
			title = "AW Inventory Item Image Upload";
			$rootScope.fileViewSource = "templates/file-upload.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern);
		}
		
		awInvVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("AW_INV", size, function(status, size){
				if("success" === status){
					awInvVm.pageSize = size;
					awInvVm.pageChanged();
				}
			});
		}
		
		awInvVm.getPageSize = function(){
			AjaxUtil.getPageSize("AW_INV", function(status, size){
				if("success" === status){
					awInvVm.pageSize = size;
				}
			});
		}
		
		awInvVm.pageChanged = function() {
			awInvVm.getAwInventories();
		}		
		awInvVm.cancelAwInventoryAction = function(){
			$state.go("aw-view");		
		}
		awInvVm.addOrUpdateAwInventory = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');
			awInvVm.addAwInventory();
		}
		
		
		awInvVm.addAwInventory = function(){
			var message = "AW Inventory Detail Created Successfully, add more?";
			var url = "/awacp/saveAwInventory";
			var update = false;
			if(awInvVm.awInventory && awInvVm.awInventory.id){
				message = "AW Inventory Detail Updated Successfully";
				awInvVm.awInventory.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateAwInventory";
				update = true;
			}else{
				awInvVm.awInventory.createdById = StoreService.getUser().userId;
				awInvVm.awInventory.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["awInventory"] = awInvVm.awInventory;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				awInvVm.awInventory = {};
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){awInvVm.cancelAwInventoryAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){awInvVm.cancelAwInventoryAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				jqXHR.errorSource = "AwInventoryCtrl.js::awInvVm.addAwInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		awInvVm.editAwInventory = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["awInventory"] = awInvVm.awInventory;
				AjaxUtil.getData("/awacp/getAwInventory/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.awInventory){
						$scope.$apply(function(){
							awInvVm.awInventory = data.awInventory;	
							awInvVm.action = awInvVm.awInventory && awInvVm.awInventory.id?"Update":"Add";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "AwInventoryCtrl.js::awInvVm.editAwInventory::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		awInvVm.deleteAwInventory = function(id){
			AjaxUtil.getData("/awacp/deleteAwInventory/"+id, Math.random())
			.success(function(data, status, headers){
				awInvVm.totalItems = (awInvVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'AW Inventory Detail Deleted Successfully.')
					.then(function (){awInvVm.getAwInventories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete AW Inventory Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "AwInventoryCtrl::awInvVm.deleteAwInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		awInvVm.getAwInventories = function(){
			awInvVm.awInventories = [];
			awInvVm.pageNumber = awInvVm.currentPage;
			AjaxUtil.getData("/awacp/listAwInventories/"+awInvVm.pageNumber+"/"+awInvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						awInvVm.totalItems = data.stsResponse.totalCount;
					});
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
							v.openImageContainer = false;
							tmp.push(v);
						});					
					} else {
						data.stsResponse.results.openImageContainer = false;
					    tmp.push(data.stsResponse.results);
					}
					$scope.$apply(function(){
						awInvVm.awInventories = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AwInventoryCtrl::awInvVm.getAwInventories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		awInvVm.editAwInventory();
	}		
})();



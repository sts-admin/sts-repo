(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('AwfInventoryCtrl', AwfInventoryCtrl);
	AwfInventoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService', 'FileService'];
	function AwfInventoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService, FileService){
		var awfInvVm = this;
		awfInvVm.action = "Add";
	    
		$scope.timers = [];
		awfInvVm.awfInventories= [];
		awfInvVm.awfInventory = {};
		
		awfInvVm.totalItems = -1;
		awfInvVm.currentPage = 1;
		awfInvVm.pageNumber = 1;
		awfInvVm.pageSize = 20;
		awfInvVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		
		awfInvVm.orderBooks = [];
		
		awfInvVm.documents = [];
		
		awfInvVm.listDocuments = function (source, sourceId){
			awfInvVm.documents = [];
			AjaxUtil.getData("/awacp/listFilesBySource/"+ source + "/"+sourceId, Math.random())
			.success(function(data, status, headers){
				if(data && data.file && data.file.length > 0){										
					$.each(data.file, function(k, v){
						awfInvVm.documents.push(v);
					});
				}
				$scope.$digest();
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UploadService::listDocuments::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		};
		
		awfInvVm.toggleImageContainer = function(invId){
			awfInvVm.listDocuments("inv_awf_image_doc", invId);
			for(var i = 0; i < awfInvVm.awfInventories.length; i++){
				if(awfInvVm.awfInventories[i].id == invId){
					awfInvVm.awfInventories[i].openImageContainer = !awfInvVm.awfInventories[i].openImageContainer;
				}else{
					awfInvVm.awfInventories[i].openImageContainer = false;
				}
			}
		}
		
		awfInvVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			$rootScope.fileViewSource = "templates/file-upload.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					awfInvVm.updateFileUploadCount(source, sourceId, filePattern);
				}
			});
		}
		
		awfInvVm.updateFileUploadCount = function(source, sourceId, docType){
			if(awfInvVm.awfInventories && awfInvVm.awfInventories.length > 0){
				for(var i = 0; i < awfInvVm.awfInventories.length; i++){
					if(awfInvVm.awfInventories[i].id === sourceId){
						if(source.includes("inv_awf_image_doc")){
							awfInvVm.awfInventories[i].imageCount = (parseInt(awfInvVm.awfInventories[i].imageCount) + 1);
						}					
						break;
					}
				}
			}
		}
		
		awfInvVm.setCurrentPageSize =function(identifier, size){
			AjaxUtil.setPageSize(identifier, size, function(status, size){
				if("success" === status){
					awfInvVm.pageSize = size;
					if(identifier === 'AWF_ORDERS'){
						awfInvVm.generateReport();
					}else{
						awfInvVm.pageChanged();
					}
				}
			});
		}
		
		awfInvVm.getPageSize = function(identifier){
			AjaxUtil.getPageSize(identifier, function(status, size){
				if("success" === status){					
					awfInvVm.pageSize = size;
					$scope.$digest();
				}
			});
		}
		
		awfInvVm.pageChanged = function() {
			awfInvVm.getAwfInventories();
		}	
		awfInvVm.setPageSize = function(){
			var state = $state.current.name;
			if(state === 'awf-orders'){
				awfInvVm.getPageSize('AWF_ORDERS');
			}else if(state === 'awf-view'){
				awfInvVm.getPageSize('AWF_INV');
			}
		}
			
		awfInvVm.cancelAwfInventoryAction = function(){
			$state.go("awf-view");		
		}
		awfInvVm.addOrUpdateAwfInventory = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');
			awfInvVm.addAwfInventory();
		}
		
		
		awfInvVm.addAwfInventory = function(){
			var message = "AWF Inventory Detail Created Successfully, add more?";
			var url = "/awacp/saveAwfInventory";
			var update = false;
			if(awfInvVm.awfInventory && awfInvVm.awfInventory.id){
				message = "AWF Inventory Detail Updated Successfully";
				awfInvVm.awfInventory.updatedById = StoreService.getUser().userId;
				awfInvVm.awfInventory.updatedByUserCode = StoreService.getUser().userCode;
				awfInvVm.awfInventory.auditMessage = "Updated AWF inventory with item name '"+ awfInvVm.awfInventory.item + "'";
				url = "/awacp/updateAwfInventory";
				update = true;
			}else{
				awfInvVm.awfInventory.createdById = StoreService.getUser().userId;
				awfInvVm.awfInventory.createdByUserCode = StoreService.getUser().userCode;
				awfInvVm.awfInventory.auditMessage = "Created AWF inventory with item name '"+ awfInvVm.awfInventory.item + "'";
			}
			var formData = {};
			formData["awfInventory"] = awfInvVm.awfInventory;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				awfInvVm.awfInventory = {};
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){awfInvVm.cancelAwfInventoryAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){awfInvVm.cancelAwfInventoryAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				jqXHR.errorSource = "AwfInventoryCtrl.js::awfInvVm.addAwfInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		awfInvVm.editAwfInventory = function(){
			var formData = {};
			formData["awfInventory"] = awfInvVm.awfInventory;
			AjaxUtil.getData("/awacp/getAwfInventory/"+$state.params.id, Math.random())
			.success(function(data, status, headers){
				if(data && data.awfInventory){
					$scope.$apply(function(){
						awfInvVm.awfInventory = data.awfInventory;	
						awfInvVm.action = awfInvVm.awfInventory && awfInvVm.awfInventory.id?"Update":"Add";							
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AwfInventoryCtrl.js::awfInvVm.editAwfInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		awfInvVm.deleteAwfInventory = function(id){
			AjaxUtil.getData("/awacp/deleteAwfInventory/"+id, Math.random())
			.success(function(data, status, headers){
				awfInvVm.totalItems = (awfInvVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'AWF Inventory Detail Deleted Successfully.')
					.then(function (){awfInvVm.getAwfInventories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete AWF Inventory Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "AwfInventoryCtrl::awfInvVm.deleteAwfInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		awfInvVm.getAwfInventories = function(){
			awfInvVm.awfInventories = [];
			awfInvVm.pageNumber = awfInvVm.currentPage;
			AjaxUtil.getData("/awacp/listAwfInventories/"+awfInvVm.pageNumber+"/"+awfInvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						awfInvVm.totalItems = data.stsResponse.totalCount;
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
						awfInvVm.awfInventories = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AwfInventoryCtrl::awfInvVm.getAwfInventories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		if($state.params.id != undefined){
			awfInvVm.editAwfInventory();
		}
		awfInvVm.orderPageChanged = function() {
			awfInvVm.generateReport();
		}
		awfInvVm.generateReport = function(){
			awfInvVm.orderBooks = [];
			awfInvVm.pageNumber = awfInvVm.currentPage;
			AjaxUtil.getData("/awacp/listInventoryOrders/awf/"+awfInvVm.pageNumber+"/"+awfInvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						awfInvVm.totalItems = data.stsResponse.totalCount;
					});
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
							if(v.invItems && !jQuery.isArray(v.invItems)) {
								var items = [];
								items.push(v.invItems);
								v.invItems = items;
							}
							tmp.push(v);
						});					
					} else {
					    var items = [];
						items.push(data.stsResponse.results.invItems);
						data.stsResponse.results.invItems = items;
					    tmp.push(data.stsResponse.results);
					}
					$scope.$apply(function(){
						awfInvVm.orderBooks = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AwInventoryCtrl::awfInvVm.generateReport::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		awfInvVm.setPageSize();	
		
	}		
})();



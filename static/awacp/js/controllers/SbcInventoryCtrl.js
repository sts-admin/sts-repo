(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('SbcInventoryCtrl', SbcInventoryCtrl);
	SbcInventoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService', 'FileService'];
	function SbcInventoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService, FileService){
		var sbcInvVm = this;
		sbcInvVm.action = "Add";
	    
		$scope.timers = [];
		sbcInvVm.sbcInventories= [];
		sbcInvVm.sbcInventory = {};
		
		sbcInvVm.totalItems = -1;
		sbcInvVm.currentPage = 1;
		sbcInvVm.pageNumber = 1;
		sbcInvVm.pageSize = 20;
		sbcInvVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		
		
		sbcInvVm.documents = [];
		sbcInvVm.orderBooks = [];
		
		sbcInvVm.listDocuments = function (source, sourceId){
			sbcInvVm.documents = [];
			AjaxUtil.getData("/awacp/listFilesBySource/"+ source + "/"+sourceId, Math.random())
			.success(function(data, status, headers){
				if(data && data.file && data.file.length > 0){										
					$.each(data.file, function(k, v){
						sbcInvVm.documents.push(v);
					});
				}
				$scope.$digest();
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UploadService::listDocuments::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		};
		
		sbcInvVm.toggleImageContainer = function(invId){
			sbcInvVm.listDocuments("inv_awf_image_doc", invId);
			for(var i = 0; i < sbcInvVm.sbcInventories.length; i++){
				if(sbcInvVm.sbcInventories[i].id == invId){
					sbcInvVm.sbcInventories[i].openImageContainer = !sbcInvVm.sbcInventories[i].openImageContainer;
				}else{
					sbcInvVm.sbcInventories[i].openImageContainer = false;
				}
			}
		}
		
		sbcInvVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			$rootScope.fileViewSource = "templates/file-upload.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					sbcInvVm.updateFileUploadCount(source, sourceId, filePattern);
				}
			});
		}
		
		sbcInvVm.updateFileUploadCount = function(source, sourceId, docType){
			if(sbcInvVm.sbcInventories && sbcInvVm.sbcInventories.length > 0){
				for(var i = 0; i < sbcInvVm.sbcInventories.length; i++){
					if(sbcInvVm.sbcInventories[i].id === sourceId){
						if(source.includes("inv_sbc_image_doc")){
							sbcInvVm.sbcInventories[i].imageCount = (parseInt(sbcInvVm.sbcInventories[i].imageCount) + 1);
						}					
						break;
					}
				}
			}
		}
		
		sbcInvVm.setCurrentPageSize =function(identifier, size){
			AjaxUtil.setPageSize(identifier, size, function(status, size){
				if("success" === status){
					sbcInvVm.pageSize = size;
					if(identifier === 'SBC_ORDERS'){
						sbcInvVm.generateReport();
					}else{
						sbcInvVm.pageChanged();
					}
				}
			});
		}
		
		sbcInvVm.getPageSize = function(identifier){
			AjaxUtil.getPageSize(identifier, function(status, size){
				if("success" === status){
					sbcInvVm.pageSize = size;
					$scope.$digest();
				}
			});
		}
		
		sbcInvVm.pageChanged = function() {
			sbcInvVm.getSbcInventories();
		}
		sbcInvVm.orderPageChanged = function() {
			sbcInvVm.generateReport();
		}		
		sbcInvVm.cancelSbcInventoryAction = function(){
			$state.go("sbc-view");		
		}
		sbcInvVm.addOrUpdateSbcInventory = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');
			sbcInvVm.addSbcInventory();
		}
		
		
		sbcInvVm.addSbcInventory = function(){
			var message = "SBC Inventory Detail Created Successfully, add more?";
			var url = "/awacp/saveSbcInventory";
			var update = false;
			if(sbcInvVm.sbcInventory && sbcInvVm.sbcInventory.id){
				message = "SBC Inventory Detail Updated Successfully";
				sbcInvVm.sbcInventory.updatedById = StoreService.getUser().userId;
				sbcInvVm.sbcInventory.updatedByUserCode = StoreService.getUser().userCode;
				sbcInvVm.sbcInventory.auditMessage = "Updated SBC Inventory with name '"+ sbcInvVm.sbcInventory.item + "'";
				url = "/awacp/updateSbcInventory";
				update = true;
			}else{
				sbcInvVm.sbcInventory.createdById = StoreService.getUser().userId;
				sbcInvVm.sbcInventory.createdByUserCode = StoreService.getUser().userCode;
				sbcInvVm.sbcInventory.auditMessage = "Added SBC Inventory with name '"+ sbcInvVm.sbcInventory.item + "'";
			}
			var formData = {};
			formData["sbcInventory"] = sbcInvVm.sbcInventory;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				sbcInvVm.sbcInventory = {};
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){sbcInvVm.cancelSbcInventoryAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){sbcInvVm.cancelSbcInventoryAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				jqXHR.errorSource = "SbcInventoryCtrl.js::sbcInvVm.addSbcInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		sbcInvVm.editSbcInventory = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["sbcInventory"] = sbcInvVm.sbcInventory;
				AjaxUtil.getData("/awacp/getSbcInventory/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.sbcInventory){
						$scope.$apply(function(){
							sbcInvVm.sbcInventory = data.sbcInventory;	
							sbcInvVm.action = sbcInvVm.sbcInventory && sbcInvVm.sbcInventory.id?"Update":"Add";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "SbcInventoryCtrl.js::sbcInvVm.editSbcInventory::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		sbcInvVm.deleteSbcInventory = function(id){
			AjaxUtil.getData("/awacp/deleteSbcInventory/"+id, Math.random())
			.success(function(data, status, headers){
				sbcInvVm.totalItems = (sbcInvVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'SBC Inventory Detail Deleted Successfully.')
					.then(function (){sbcInvVm.getSbcInventories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete SBC Inventory Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "SbcInventoryCtrl.js.js::sbcInvVm.deleteSbcInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		sbcInvVm.getSbcInventories = function(){
			sbcInvVm.sbcInventories = [];
			sbcInvVm.pageNumber = sbcInvVm.currentPage;
			AjaxUtil.getData("/awacp/listSbcInventories/"+sbcInvVm.pageNumber+"/"+sbcInvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						sbcInvVm.totalItems = data.stsResponse.totalCount;
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
						sbcInvVm.sbcInventories = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "SbcInventoryCtrl.js::sbcInvVm.getSbcInventories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		sbcInvVm.editSbcInventory();
		
		sbcInvVm.generateReport = function(){
			sbcInvVm.orderBooks = [];
			sbcInvVm.pageNumber = sbcInvVm.currentPage;
			AjaxUtil.getData("/awacp/listInventoryOrders/sbc/"+sbcInvVm.pageNumber+"/"+sbcInvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						sbcInvVm.totalItems = data.stsResponse.totalCount;
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
						sbcInvVm.orderBooks = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "SbcInventoryCtrl::sbcInvVm.generateReport::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		sbcInvVm.setPageSize = function(){
			var state = $state.current.name;
			if(state === 'sbc-orders'){
				sbcInvVm.getPageSize('SBC_ORDERS');
			}else if(state === 'sbc-view'){
				sbcInvVm.getPageSize('SBC_INV');
			}
		}
		sbcInvVm.setPageSize();
	}		
})();



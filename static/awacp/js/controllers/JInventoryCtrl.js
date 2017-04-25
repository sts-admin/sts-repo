(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('JInventoryCtrl', JInventoryCtrl);
	JInventoryCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService', 'FileService'];
	function JInventoryCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService, FileService){
		var jinvVm = this;
		jinvVm.action = "Add";
	   
		$scope.timers = [];
		jinvVm.jinvs= [];
		jinvVm.jInventory = {};
		jinvVm.totalItems = -1;
		jinvVm.currentPage = 1;
		jinvVm.pageNumber = 1;
		jinvVm.pageSize = 20;
		jinvVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		
		jinvVm.documents = [];
		
		jinvVm.listDocuments = function (source, sourceId){
			jinvVm.documents = [];
			AjaxUtil.getData("/awacp/listFilesBySource/"+ source + "/"+sourceId, Math.random())
			.success(function(data, status, headers){
				if(data && data.file && data.file.length > 0){										
					$.each(data.file, function(k, v){
						jinvVm.documents.push(v);
					});
				}
				$scope.$digest();
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UploadService::listDocuments::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		};
		
		jinvVm.toggleImageContainer = function(invId){
			jinvVm.listDocuments("inv_j_image_doc", invId);
			for(var i = 0; i < jinvVm.jinvs.length; i++){
				if(jinvVm.jinvs[i].id == invId){
					jinvVm.jinvs[i].openImageContainer = !jinvVm.jinvs[i].openImageContainer;
				}else{
					jinvVm.jinvs[i].openImageContainer = false;
				}
			}
		}
		
		jinvVm.showFileListingView = function(source, sourceId, title, size, filePattern){
			$rootScope.fileViewSource = "templates/file-upload.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern);
		}
		jinvVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("J_INV", size, function(status, size){
				if("success" === status){
					jinvVm.pageSize = size;
					jinvVm.pageChanged();
				}
			});
		}
		
		jinvVm.getPageSize = function(){
			AjaxUtil.getPageSize("J_INV", function(status, size){
				if("success" === status){
					jinvVm.pageSize = size;
				}
			});
		}
		jinvVm.pageChanged = function() {
			jinvVm.getJInventories();
		}		
		jinvVm.cancelJInventoryAction = function(){
			$state.go("j-view");		
		}
		jinvVm.addOrUpdateJInventory = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');
			jinvVm.addJInventory();
		}
		
		
		jinvVm.addJInventory = function(){
			var message = "J Inventory Detail Created Successfully, add more?";
			var url = "/awacp/saveJInventory";
			var update = false;
			if(jinvVm.jInventory && jinvVm.jInventory.id){
				message = "J Inventory Detail Updated Successfully";
				jinvVm.jInventory.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateJInventory";
				update = true;
			}else{
				jinvVm.jInventory.createdById = StoreService.getUser().userId;
				jinvVm.jInventory.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			formData["jInventory"] = jinvVm.jInventory;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				jinvVm.jInventory = {};
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){jinvVm.cancelJInventoryAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){jinvVm.cancelJInventoryAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				jqXHR.errorSource = "JInventoryCtrl::jinvVm.addJInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		jinvVm.editJInventory = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["jInventory"] = jinvVm.jInventory;
				AjaxUtil.getData("/awacp/getJInventory/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.jInventory){
						$scope.$apply(function(){
							jinvVm.jInventory = data.jInventory;	
							jinvVm.action = jinvVm.jInventory && jinvVm.jInventory.id?"Update":"Add";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "JInventoryCtrl::jinvVm.editJInventory::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		jinvVm.deleteJInventory = function(id){
			AjaxUtil.getData("/awacp/deleteJInventory/"+id, Math.random())
			.success(function(data, status, headers){
				jinvVm.totalItems = (jinvVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'J Inventory Detail Deleted Successfully.')
					.then(function (){jinvVm.getJInventories();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete J Inventory Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "JInventoryCtrl::jinvVm.deleteJInventory::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		jinvVm.getJInventories = function(){
			jinvVm.jinvs = [];
			jinvVm.pageNumber = jinvVm.currentPage;
			AjaxUtil.getData("/awacp/listJInventories/"+jinvVm.pageNumber+"/"+jinvVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						jinvVm.totalItems = data.stsResponse.totalCount;
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
						jinvVm.jinvs = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::jinvVm.getJInventories::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		jinvVm.editJInventory();
	}		
})();



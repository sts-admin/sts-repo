(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('DeleteFileCtrl', DeleteFileCtrl);
	DeleteFileCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function DeleteFileCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var deleteVm = this;
	    deleteVm.totalItems = -1;
		deleteVm.currentPage = 1;
		deleteVm.pageNumber = 1;
		deleteVm.pageSize = 5;
		$scope.timers = [];
		deleteVm.files= [];
		deleteVm.file = {};
		deleteVm.section = {};
		deleteVm.sectionId = "";
		deleteVm.fileCount = 0;
		deleteVm.sections = [{id:'TAKEOFF', name:'TAKEOFF'}, {id:'QUOTE', name:'QUOTE'}, {id:'JOBORDER', name:'JOB ORDER'}, {id:'ORDERBOOK', name:'ORDE RBOOK'}, {id:'CLAIMS', name:'CLAIMS'}, {id:'USERS', name:'USERS'}, {id:'DRLB', name:'DRLB'}];
		deleteVm.section = deleteVm.sections[0];
		
		deleteVm.removeFileFromList = function(id){
			for(var i = 0; i < deleteVm.files.length; i++){
				if(deleteVm.files[i].id == id){
					deleteVm.files.splice( i, 1 );
					$scope.$digest();
					break;
				}
			}
		}
	
		deleteVm.searchFiles = function(){
			if(deleteVm.section && deleteVm.sectionId){
				deleteVm.files = [];
				deleteVm.fileCount = 0;
				var url = "/awacp/listFilesBySourceMatchAndId/"+deleteVm.section.id + "/" + deleteVm.sectionId;				
				AjaxUtil.getData(url, Math.random())
				.success(function(data, status, headers){
					if(data && data.file){
						deleteVm.files = data.file;
						deleteVm.fileCount = deleteVm.files.length;
						$scope.$digest();
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "DeleteFileCtrl::deleteVm.searchFiles::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}
		}
		deleteVm.deleteFile = function(file){
			if(file){
				AlertService.showConfirm(	'AWACP :: Confirmation!', "Are you sure to delete?")
					.then(function (){
						var url = "/awacp/archiveFile/"+file.id;				
						AjaxUtil.getData(url, Math.random())
						.success(function(data, status, headers){
							if(data && data.result){
								if("success" === data.result){
									deleteVm.removeFileFromList(file.id);
									AlertService.showAlert(	'AWACP :: Alert!', "File deleted successfully")
									.then(function (){return false;},function (){return false;});
								}else{
									AlertService.showAlert(	'AWACP :: Alert!', "Unable to delete the file")
									.then(function (){return false;},function (){return false;});
								}
							}
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "DeleteFileCtrl::deleteVm.deleteFile::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});
				},
				function (){
					return false;
				});
				
			}			
		}
		deleteVm.openFile = function(file){
			var path = $rootScope.resourceReadPath + file.createdName + file.extension;
			$window.open(path);
		}

		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



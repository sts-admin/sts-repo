(function() {
	'use strict';
	angular.module('awacpApp.services')
		.factory("FileService",["$q", "$uibModal", "AjaxUtil", "Upload", "StoreService", "$rootScope", "AlertService", function ($q, $uibModal, AjaxUtil, Upload, StoreService, $rootScope, AlertService){
			return {
				showFileViewDialog:function(source, sourceId, sourceTitle, size, filePattern, viewSource, callback){
					var defer = $q.defer();
					var modalInstance = $uibModal.open({
						animation: true,
						size: size,
						templateUrl: 'templates/file-dialog.html',
						windowClass:'alert-zindex',
						
						controller: function ($scope, $uibModalInstance){
							$scope.uploadForm = {};
							$scope.file = null;
							$scope.filePattern = filePattern;
							$scope.fileAcceptPattern = filePattern;
							$scope.viewSource = viewSource;
							$scope.title = sourceTitle;
							$scope.source = source;
							$scope.sourceId = sourceId;
							$scope.size = size;
							$scope.documents = [];
							$scope.hasFile = false;
							
							
							//resetSelection:start
							$scope.resetSelection = function(){
								$scope.file = null;
								$scope.hasFile = false;
							};
							//resetSelection:end
							//listDocuments:start
							$scope.listDocuments = function (source, sourceId){
								$scope.documents = [];
								AjaxUtil.getData("/awacp/listFilesBySource/"+ source + "/"+sourceId, Math.random())
								.success(function(data, status, headers){
									if(data && data.file && data.file.length > 0){										
										jQuery.each(data.file, function(k, v){
											$scope.documents.push(v);
										});
									}
									$scope.$digest();
								})
								.error(function(jqXHR, textStatus, errorThrown){
									jqXHR.errorSource = "UploadService::listDocuments::Error";
									AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
								});
							};
							//listDocuments:end
							//showFileList:start
							$scope.showFileList = function(){
								$rootScope.fileViewSource = "templates/file-listing.html";
								$scope.title = "File List";
								$scope.listDocuments($scope.source, $scope.sourceId);
							};
							//showFileList:end
							//showUploadForm:start
							$scope.showUploadForm = function(){
								$rootScope.fileViewSource = "templates/file-upload.html";
								$scope.title = "Upload File";								
							};
							if(viewSource && viewSource === 'list-view'){
								$scope.showFileList();
							}else{
								$scope.showUploadForm();
							}
							//showUploadForm:end
							
							//close:start
							$scope.close = function (){
								modalInstance.dismiss();
								defer.reject();
							};
							//close:end
							//fileUpload :start
							$scope.fileUpload = function (file){
								if(!file){
									return;
								}
								if(file){
									var fileData = new FormData();
									fileData.append('attachment', file);
									AjaxUtil.uploadData("/awacp/uploadFile", fileData)
									.success(function(data, status, headers){
										var url = "/awacp/updateFileSource?userId="+StoreService.getUser().userId+"&fileSource="+$scope.source+"&fileSourceId="+$scope.sourceId+"&fileId="+data.file.id;
										
										AjaxUtil.getData(url, Math.random())
										.success(function(data, status, headers){
											AlertService.showAlert(	'AWACP :: Alert!', "File uploaded successfully")
											.then(function (){
												if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
													callback(data, "success");
												}
												return false;
											},function (){return false;});
											modalInstance.dismiss();
											defer.resolve();
										})
										.error(function(jqXHR, textStatus, errorThrown){
											jqXHR.errorSource = "UploadService::upload.getUsers::Error";
											AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
										});										
									})
									.error(function(jqXHR, textStatus, errorThrown){
										modalInstance.dismiss();
										defer.resolve();
										jqXHR.errorSource = "UploadService::upload::Error";
										AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
									});
								}else{
									modalInstance.dismiss();
									defer.resolve();
								}
							};
							//fileUpload :end
							//fileDownload :start
							$scope.fileDownload = function(source, fileId){
								//window.location.href = "http://localhost:8080/awacpservices/awacp/get/file/" + fileId;
								AjaxUtil.downloadData("/awacp/get/file/"+sourceId, Math.random())
								.success(function(data, status, headers){
									alert("success");
								})
								.error(function(jqXHR, textStatus, errorThrown){
									alert("ERROR: "+ JSON.stringify(jqXHR, null, 4));
								});
							}; //fileDownload :end	
						}
					});
					return defer.promise;
				}
			};
		}
	]);
})();







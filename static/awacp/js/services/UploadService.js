(function() {
	'use strict';
	angular.module('awacpApp.services')
		.factory("UploadService",["$q", "$uibModal", "AjaxUtil", "Upload", "StoreService", "$rootScope", function ($q, $uibModal, AjaxUtil, Upload, StoreService, $rootScope){
			return {
				showFileListingView:function(source, sourceId, sourceTitle, size){
					var defer = $q.defer();
					var modalInstance = $uibModal.open({
						animation: true,
						size: size,
						templateUrl: 'templates/file-dialog.html',
						windowClass:'alert-zindex',
						controller: function ($scope, $uibModalInstance){
							$scope.title = sourceTitle;
							$scope.source = source;
							$scope.sourceId = sourceId;
							$scope.size = size;
							$scope.documents = [];
							$scope.showUploadForm = function(){
								$rootScope.fileUploadSource = "templates/file-upload.html";
								this.showFileUpload($scope.source, $scope.sourceId, $scope.title, $scope.size);
							};
							$scope.listDocuments = function (source, sourceId){
								$scope.documents = [];
								AjaxUtil.getData("/awacp/listFilesBySource/"+ source + "/"+sourceId, Math.random())
								.success(function(data, status, headers){
									if(data && data.file && data.file.length > 0){				
										$.each(data.file, function(k, v){
											$scope.documents.push(v);
										});
									}
								})
								.error(function(jqXHR, textStatus, errorThrown){
									jqXHR.errorSource = "UploadService::listDocuments::Error";
									AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
								});
							};
							$scope.close = function (){
								modalInstance.dismiss();
								defer.reject();
							};
							$scope.listDocuments($scope.source, $scope.sourceId);
						}
					});
					return defer.promise;
				},				
				showFileUpload:function(source, sourceId, sourceTitle, size){
					var defer = $q.defer();
					var modalInstance = $uibModal.open({
						animation: true,
						size: size,
						templateUrl: 'templates/takeoff-file-upload.html',
						windowClass:'alert-zindex',
						controller: function ($scope, $uibModalInstance){
							$scope.file = null;
							$scope.filePattern = ".pdf";
							$scope.fileAcceptPattern = ".pdf";
							$scope.title = sourceTitle;
							$scope.source = source;
							$scope.sourceId = sourceId;
							$scope.resetSelection = function(){
								$scope.file = null;
							};
							$scope.upload = function (){
								if ($scope.uploadForm.file.$valid && $scope.file) {
									var fileData = new FormData();
									fileData.append('attachment', $scope.file);
									AjaxUtil.uploadData("/awacp/uploadFile", fileData)
									.success(function(data, status, headers){
										var url = "/awacp/updateFileSource?fileSource="+$scope.source+"&fileSourceId="+$scope.sourceId+"&fileId="+data.file.id;
										AjaxUtil.getData(url, Math.random())
										.success(function(data, status, headers){
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
										alert("error "+ JSON.stringify(jqXHR, null, 4));
										jqXHR.errorSource = "UploadService::upload::Error";
										AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
									});
									
									/*Upload.upload({
										url: 'http://localhost:8080/awacpservices/awacp/uploadFileExample',
										fields: {'fileSource': $scope.source, 'fileSourceId':$scope.fileSourceId}, // additional data to send
										withCredentials:true,
										headers: {'Authorization': 'Bearer ' + StoreService.getAccessToken(), 'Accept' : 'application/json'}, 
										file: $scope.file
									}).progress(function (evt) {
										
										var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
										console.log('progress: ' + progressPercentage + '% ' + evt.config.file.name);
									}).success(function (data, status, headers, config) {
										console.log('file ' + config.file.name + 'uploaded. Response: ' + data);
									});*/
								}else{
									modalInstance.dismiss();
									defer.resolve();
								}
								
								
							};
							$scope.cancel = function (){
								modalInstance.dismiss();
								defer.reject();
							};
						}
					});
					return defer.promise;
				}
			};
		}
	]);
})();







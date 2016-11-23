(function() {
	'use strict';
	angular.module('awacpApp.services')
		.factory("UploadService",["$q", "$uibModal", "AjaxUtil", function ($q, $uibModal, AjaxUtil){
			return {				
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
									alert('$scope.source = '+ $scope.source + ", $scope.sourceId = "+ $scope.sourceId);
									fileData.append('fileSource', $scope.source);
									fileData.append('fileSourceId', $scope.sourceId);
									fileData.append('attachment', $scope.file);
									AjaxUtil.uploadData("/awacp/uploadFile", fileData)
									.success(function(data, status, headers){
										alert("data.fileInfo.status = "+ JSON.stringify(data, null, 4));
										modalInstance.dismiss();
										defer.resolve();
									})
									.error(function(jqXHR, textStatus, errorThrown){
										modalInstance.dismiss();
										defer.resolve();
										alert("error "+ JSON.stringify(jqXHR, null, 4));
										jqXHR.errorSource = "UploadService::upload::Error";
										AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
									});
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







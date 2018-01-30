(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('FsrCtrl', FsrCtrl);
	FsrCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService', 'FileService'];
	function FsrCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService, FileService){
		var fsrVm = this;
		fsrVm.currDate = new Date();
		fsrVm.orderBook = {};
	    
		$scope.timers = [];
		fsrVm.followups= [];
		fsrVm.followup = {};
		fsrVm.claim= {};
		
		fsrVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					fsrVm.updateFileUploadCount(source, sourceId, filePattern);
				}
			});
		}
		fsrVm.updateFileUploadCount = function(source, sourceId, docType){
			if(fsrVm.claims && fsrVm.claims.length > 0){
				for(var i = 0; i < fsrVm.claims.length; i++){
					if(fsrVm.claims[i].id === sourceId){
						if(source.includes("TC_PDF") || source.includes("FC_PDF")){
							fsrVm.claims[i].pdfDocCount = (parseInt(fsrVm.claims[i].pdfDocCount) + 1);
						}			
						break;
					}
				}
			}
		}
		
		fsrVm.getClaim = function(id, type, callback){
			fsrVm.followup = {};
			var url = "";
			if(type === 'trucker'){
				url = "/awacp/getTruckerClaim/"+id;
			}else if(type === 'factory'){
				url = "/awacp/getFactoryClaim/"+id;
			}
			AjaxUtil.getData(url, Math.random())
			.success(function(data, status, headers){
				if(data && data.truckerClaim){										
					fsrVm.claim = data.truckerClaim;
					fsrVm.claim.type = 'trucker';
				}else if(data && data.factoryClaim){										
					fsrVm.claim = data.factoryClaim;
					fsrVm.claim.type = 'factory';
					
				}
				$scope.$digest();
				if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
					callback(data, "success");
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "FsrCtrl::fsrVm.getClaim::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		fsrVm.listFollowups = function(id, type){
			var url = "/awacp/getFollowupsByClaim/"+id+"/"+type;
			fsrVm.followups= [];
			AjaxUtil.getData(url, Math.random())
			.success(function(data, status, headers){
				if(data && data.claimFollowup){
					var tmp = [];
					if(jQuery.isArray(data.claimFollowup)) {
						jQuery.each(data.claimFollowup, function(k, v){
							tmp.push(v);
						});					
					} else {
					    tmp.push(data.claimFollowup);
					}
					fsrVm.followups = tmp;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "FsrCtrl::fsrVm.listFollowups::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		fsrVm.saveFollowup = function(){
			jQuery(".fu-add-action").attr('disabled','disabled');
			jQuery("#fu-add-spinner").css('display','block');	
			var update = false;
			var message = "Follow up detail added Successfully.";
			if(fsrVm.followup && fsrVm.followup.id){
				message = "Follow up detail updated Successfully";
				fsrVm.followup.userCode = StoreService.getUser().userCode;
				fsrVm.followup.updatedById = StoreService.getUserId();
				fsrVm.followup.auditMessage = "Updated Claim followup ";
				update = true;
			}else{
				fsrVm.followup.userCode = StoreService.getUser().userCode;
				fsrVm.followup.createdById = StoreService.getUserId();
				fsrVm.followup.auditMessage = "Created Claim followup ";
			}
			if(!fsrVm.followup.status){
				fsrVm.followup.status = "PENDING";
			}
			var formData = {};			
			fsrVm.followup.claimId = fsrVm.claim.id;
			formData["claimFollowup"] = fsrVm.followup;
			AjaxUtil.submitData("/awacp/saveClaimFollowup", formData)
			.success(function(data, status, headers){
				jQuery(".fu-add-action").removeAttr('disabled');
				jQuery("#fu-add-spinner").css('display','none');
				fsrVm.followup = {};
				fsrVm.followup.claimSource = data.claimFollowup.claimSource;
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){return false;},function (){return false;});
				fsrVm.listFollowups(fsrVm.claim.id, data.claimFollowup.claimSource);			
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".fu-add-action").removeAttr('disabled');
				jQuery("#fu-add-spinner").css('display','none');
				jqXHR.errorSource = "FsrCtrl::fsrVm.saveFollowup::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		var state = $state.current.name;
		if(state === 'fsr-factory' || state === 'fsr-trucker'){
			var type = null;
			if(state === 'fsr-factory'){
				type = 'factory';
			}else if(state === 'fsr-trucker'){
				type = 'trucker';
			}
			if($state.params.claimId != null && type != null){
				fsrVm.getClaim($state.params.claimId, type, function(result, sts){
					if("trucker" === type){
						fsrVm.followup.claimSource = 'TC';
						fsrVm.listFollowups($state.params.claimId, 'TC');
					}else if("factory" === type){
						fsrVm.followup.claimSource = 'FC';
						fsrVm.listFollowups($state.params.claimId, 'FC');
					}
				});
			}			
		}
	}		
})();



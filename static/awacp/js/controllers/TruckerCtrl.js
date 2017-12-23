(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TruckerCtrl', TruckerCtrl);
	TruckerCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function TruckerCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var truckerVm = this;
		truckerVm.action = "Add";
	    
		$scope.timers = [];
		truckerVm.truckers= [];
		truckerVm.trucker = {};
		truckerVm.users = [];
		
		truckerVm.totalItems = -1;
		truckerVm.currentPage = 1;
		truckerVm.pageNumber = 1;
		truckerVm.pageSize = 20;
		truckerVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		truckerVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("TRUCKER", size, function(status, size){
				if("success" === status){
					truckerVm.pageSize = size;
					truckerVm.pageChanged();
				}
			});
		}
		
		truckerVm.getPageSize = function(){
			AjaxUtil.getPageSize("TRUCKER", function(status, size){
				if("success" === status){
					truckerVm.pageSize = size;
				}
			});
		}
		
		truckerVm.pageChanged = function() {
			truckerVm.getTruckers();
		}		
		truckerVm.cancelTruckerAction = function(){
			$state.go("truckers");		
		}
		truckerVm.addOrUpdateTrucker = function(){
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');	
			truckerVm.saveTruckerLogo();
		}
		truckerVm.saveTruckerLogo = function(){
			var truckerLogoFile ="";						
			if(jQuery('input[name="truckerLogoFile"]') && jQuery('input[name="truckerLogoFile"]').get(0)){
				truckerLogoFile = jQuery('input[name="truckerLogoFile"]').get(0).files[0];
			}
			if(truckerLogoFile){
				var fileData = new FormData();
				fileData.append('attachment', truckerLogoFile);
				AjaxUtil.uploadData("/awacp/uploadFile", fileData)	
				.success(function (data, status, headers) {	
					var logo = {};
					logo["id"] = data.file.id;
					truckerVm.trucker["logo"] = logo;
					jQuery("#truckerLogoFile").val('').clone(true);
					truckerVm.addTrucker();					
				}).error(function (jqXHR, textStatus, errorThrown) {
					jQuery(".actions").removeAttr('disabled');
					jQuery(".spinner").css('display','none');
					jqXHR.errorSource = "UserCtrl::saveProfileImage::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});		
			}else{
				truckerVm.addTrucker();
			}
		}
		
		truckerVm.addTrucker = function(){
			var message = "Trucker Detail Created Successfully, add more?";
			var url = "/awacp/saveTrucker";
			var update = false;
			if(truckerVm.trucker && truckerVm.trucker.id){
				message = "Trucker Detail Updated Successfully";
				truckerVm.trucker.updatedById = StoreService.getUser().userId;
				truckerVm.trucker.updatedByUserCode = StoreService.getUser().userCode;
				truckerVm.trucker.auditMessage = "Added Trucker with name '"+truckerVm.trucker.name+"'";
				url = "/awacp/updateTrucker";
				update = true;
			}else{
				truckerVm.trucker.createdById = StoreService.getUser().userId;
				truckerVm.trucker.createdByUserCode = StoreService.getUser().userCode;
				truckerVm.trucker.auditMessage = "Updated Trucker with name '"+truckerVm.trucker.name+"'";
			}
			var formData = {};
			formData["trucker"] = truckerVm.trucker;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){truckerVm.cancelTruckerAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){truckerVm.cancelTruckerAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Trucker with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "TruckerCtrl::truckerVm.addTrucker::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		
		truckerVm.editTrucker = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["trucker"] = truckerVm.trucker;
				AjaxUtil.getData("/awacp/getTrucker/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.trucker){
						$scope.$apply(function(){
							truckerVm.trucker = data.trucker;	
							truckerVm.action = truckerVm.trucker && truckerVm.trucker.id?"Update":"Add";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "TruckerCtrl::truckerVm.editTrucker::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		truckerVm.deleteTrucker = function(id){
			AjaxUtil.getData("/awacp/deleteTrucker/"+id, Math.random())
			.success(function(data, status, headers){
				truckerVm.totalItems = (truckerVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Trucker Detail Deleted Successfully.')
					.then(function (){truckerVm.getTruckers();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Trucker Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "TruckerCtrl::truckerVm.deleteTrucker::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		truckerVm.getTruckers = function(){
			truckerVm.truckers = [];
			truckerVm.pageNumber = truckerVm.currentPage;
			AjaxUtil.getData("/awacp/listTruckers/"+truckerVm.pageNumber+"/"+truckerVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						truckerVm.totalItems = data.stsResponse.totalCount;
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
						truckerVm.truckers = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "UserCtrl::truckerVm.getTruckers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		truckerVm.editTrucker();
	}		
})();



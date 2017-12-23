(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('MarketingTemplateCtrl', MarketingTemplateCtrl);
	MarketingTemplateCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function MarketingTemplateCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var mktTmpVm = this;
		mktTmpVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		mktTmpVm.action = "Add";
		$scope.timers = [];
		mktTmpVm.totalItems = -1;
		mktTmpVm.currentPage = 1;
		mktTmpVm.pageNumber = 1;
		mktTmpVm.pageSize = 20;
		mktTmpVm.marketingTemplates= [];
		mktTmpVm.marketingTemplate = {};
		mktTmpVm.editorContent = "";
		mktTmpVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("MARKETING_TEMPLATE", size, function(status, size){
				if("success" === status){
					mktTmpVm.pageSize = size;
					mktTmpVm.pageChanged();
				}
			});
		}
		
		mktTmpVm.getPageSize = function(){
			AjaxUtil.getPageSize("MARKETING_TEMPLATE", function(status, size){
				if("success" === status){
					mktTmpVm.pageSize = size;
				}
			});
		}
		mktTmpVm.pageChanged = function() {
			mktTmpVm.getArchitects();
		};		
		mktTmpVm.cancelMarketingTemplateAction = function(){
			$state.go("marketing-templates");
		}		
		mktTmpVm.initArchitectMasterInputs = function(){
			mktTmpVm.getUsers();
		}		
		mktTmpVm.editMarketingTemplate = function(){
			if($state.params.id != undefined){
				var formData = {};
				AjaxUtil.getData("/awacp/getMarketingTemplate/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.marketingTemplate){
						$scope.$apply(function(){
							mktTmpVm.marketingTemplate = data.marketingTemplate;	
							mktTmpVm.editorContent = mktTmpVm.marketingTemplate.contentHtml;
							mktTmpVm.action = mktTmpVm.marketingTemplate && mktTmpVm.marketingTemplate.id?"Update":"Add";							
						});						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "MarketingTemplateCtrl::mktTmpVm.editMarketingTemplate::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		mktTmpVm.deleteMarketingTemplate = function(id){
			AjaxUtil.getData("/awacp/deleteMarketingTemplate/"+id, Math.random())
			.success(function(data, status, headers){
				mktTmpVm.totalItems = (mktTmpVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Marketing Template Detail Deleted Successfully.')
					.then(function (){mktTmpVm.getMarketingTemplates();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Marketing Template Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "MarketingTemplateCtrl::mktTmpVm.deleteMarketingTemplate::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		mktTmpVm.getMarketingTemplates = function(){
			mktTmpVm.marketingTemplates = [];
			mktTmpVm.pageNumber = mktTmpVm.currentPage;
			AjaxUtil.getData("/awacp/listMarketingTemplates/"+mktTmpVm.pageNumber+"/"+mktTmpVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						mktTmpVm.totalItems = data.stsResponse.totalCount;
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
						mktTmpVm.marketingTemplates = tmp;
					});
				}		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "MarketingTemplateCtrl::mktTmpVm.getMarketingTemplates::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		mktTmpVm.addMarketingTemplate = function(){
			if(!mktTmpVm.editorContent || mktTmpVm.editorContent.length <= 0){
				AlertService.showAlert(	'AWACP :: Alert!', "Please add email content and then try again.")
					.then(function (){return false;},function (){return false;});
					return;
			}
			if(mktTmpVm.editorContent.length > 2000){
				AlertService.showAlert(	'AWACP :: Alert!', "Content length exceeding allowed 2000 characters limit.")
					.then(function (){return false;},function (){return false;});
					return;
			}
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');	
			var message = "Marketing Template Detail Created Successfully, add more?";
			var url = "/awacp/saveMarketingTemplate";
			var update = false;
			mktTmpVm.marketingTemplate.contentHtml = mktTmpVm.editorContent;
			if(mktTmpVm.marketingTemplate && mktTmpVm.marketingTemplate.id){
				message = "Marketing Template Detail Updated Successfully";				
				mktTmpVm.marketingTemplate.updatedByUserCode = StoreService.getUser().userCode;
				mktTmpVm.marketingTemplate.updatedById = StoreService.getUser().userId;
				mktTmpVm.marketingTemplate.auditMessage = "Updated Marketing template '"+ mktTmpVm.marketingTemplate.templateName + "'";
				url = "/awacp/updateMarketingTemplate";
				update = true;
			}else{
				mktTmpVm.marketingTemplate.createdByUserCode = StoreService.getUser().userCode;
				mktTmpVm.marketingTemplate.createdById = StoreService.getUser().userId;
				mktTmpVm.marketingTemplate.auditMessage = "Created Marketing template '"+ mktTmpVm.marketingTemplate.templateName + "'";
			}
			var formData = {};			
			formData["marketingTemplate"] = mktTmpVm.marketingTemplate;
			
			
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){mktTmpVm.cancelMarketingTemplateAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){mktTmpVm.cancelMarketingTemplateAction();});
					return;
				}
				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(5000 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "An Marketing Template with this name already exist, please use a different name.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "MarketingTemplateCtrl::mktTmpVm.addMarketingTemplate::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		mktTmpVm.editMarketingTemplate();
		mktTmpVm.getPageSize();
	}		
})();



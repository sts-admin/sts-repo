(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('AppSettingCtrl', AppSettingCtrl);
	AppSettingCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function AppSettingCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var asVm = this;
		asVm.action = "App Setting :: Save Site Information";
		asVm.target = "siteinfo";
		asVm.currentForm = "templates/site-info.html";
		$scope.timers = [];
		asVm.siteInfo = {};
		asVm.siteColor = {};
		asVm.siteEmailAccount = {};
		asVm.menuItem = {};		
		asVm.activeIndex = 0;
		
		asVm.setActiveTab = function(index){
			asVm.activeIndex = index;
			if(asVm.activeIndex == 0){
				asVm.action = "App Setting :: Save Site Information";
				asVm.editSiteInfo();
			}else if(asVm.activeIndex == 1){
				asVm.action = "App Setting :: Save Site Color";
				asVm.editSiteColor();
			}else if(asVm.activeIndex == 2){
				asVm.action = "App Setting :: Save Site Email Account";
				asVm.editSiteEmailAccount();				
			}else if(asVm.activeIndex == 3){
				asVm.action = "App Setting :: Save Menu Item";
				asVm.editSiteMenuItem();
			}
		}
		asVm.saveFile = function(file, callback){
			var fileData = new FormData();
			fileData.append('attachment', file);
			AjaxUtil.uploadData("/awacp/uploadFile", fileData)
			.success(function(data, sts, headers){
				if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
					callback(data, "success");
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.saveFile::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.addOrUpdateAppSetting = function(src){
			if(src === 'siteinfo'){
				asVm.saveLoginLogo();
			}else if(src === 'sitecolor'){
				asVm.saveSiteColor();
			}else if(src === 'siteemailaccount'){
				asVm.saveSiteEmailAccount();
			}else if(src === 'sitemenuitem'){
				asVm.saveSiteMenuItem();
			}
		}
		asVm.saveLoginLogo = function(){
			var loginLogoFile = null;
			if(jQuery('input[name="loginLogoFile"]') && jQuery('input[name="loginLogoFile"]').get(0)){
				loginLogoFile = jQuery('input[name="loginLogoFile"]').get(0).files[0];
			}
			if(loginLogoFile){
				asVm.saveFile(loginLogoFile, function(data, sts){
					if("success" === sts){
						var loginLogo = {};
						loginLogo.id = data.file.id;
						asVm.siteInfo.loginLogo = loginLogo;
						asVm.saveHeaderLogo();
					}else{
						AlertService.showConfirm(	'AWACP :: Alert!', "Unable to save Login logo image")
						.then(function (){return false;},function (){return false;});
					}
				});
			}else{
				asVm.saveHeaderLogo();
			}
		}		
		asVm.saveHeaderLogo = function(){
			var headerLogoFile = null;
			if(jQuery('input[name="headerLogoFile"]') && jQuery('input[name="headerLogoFile"]').get(0)){
				headerLogoFile = jQuery('input[name="headerLogoFile"]').get(0).files[0];
			}
			if(headerLogoFile){
				asVm.saveFile(headerLogoFile, function(data, sts){
					if("success" === sts){
						var headerLogo = {};
						headerLogo.id = data.file.id;
						asVm.siteInfo.headerLogo = headerLogo;
						asVm.saveEmailLogo();
					}else{
						AlertService.showConfirm(	'AWACP :: Alert!', "Unable to save header logo image")
						.then(function (){return false;},function (){return false;});
					}
				});
			}else{
				asVm.saveEmailLogo();
			}
		}
		asVm.saveEmailLogo = function(){
			var emailLogoFile = null;
			if(jQuery('input[name="emailLogoFile"]') && jQuery('input[name="emailLogoFile"]').get(0)){
				emailLogoFile = jQuery('input[name="emailLogoFile"]').get(0).files[0];
			}
			if(emailLogoFile){
				asVm.saveFile(emailLogoFile, function(data, sts){
					if("success" === sts){
						var emailLogo = {};
						emailLogo.id = data.file.id;
						asVm.siteInfo.emailLogo = emailLogo;
						asVm.saveSiteInfo();
					}else{
						AlertService.showConfirm(	'AWACP :: Alert!', "Unable to save header logo image")
						.then(function (){return false;},function (){return false;});
					}
				});
			}else{
				asVm.saveSiteInfo();
			}
		}
		asVm.saveSiteInfo = function(){			
			var message = "Site info saved successfully.";
			var url = "/awacp/saveSiteInfo";
			var update = false;
			if(asVm.siteInfo && asVm.siteInfo.id){
				asVm.siteInfo.updatedById = StoreService.getUser().userId;
				asVm.siteInfo.updatedByUserCode = StoreService.getUser().userCode;
				asVm.siteInfo.auditMessage = "Updated Site info ";
			}else{
				asVm.siteInfo.createdById = StoreService.getUser().userId;
				asVm.siteInfo.createdByUserCode = StoreService.getUser().userCode;
				asVm.siteInfo.auditMessage = "Created Site info ";
			}
			var formData = {};			
			formData["siteInfo"] = asVm.siteInfo;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){return false;},function (){return false;});				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.saveSiteInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.editSiteInfo = function(siteInfoId){
			asVm.siteInfo = {};
			var url = "/awacp/getSiteInfo";
			if(siteInfoId){
				url = "/awacp/getSiteInfo/"+siteInfoId;
			}
			AjaxUtil.getData(url, Math.random())
			.success(function(data, status, headers){
				if(data && data.siteInfo){
					asVm.siteInfo = data.siteInfo;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.editSiteInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.saveSiteColor = function(){
			var message = "Site color saved successfully.";
			var url = "/awacp/saveSiteColor";
			var update = false;
			if(asVm.siteColor && asVm.siteColor.id){
				asVm.siteColor.updatedById = StoreService.getUser().userId;
			}else{
				asVm.siteColor.createdById = StoreService.getUser().userId;
			}
			var formData = {};			
			formData["siteColor"] = asVm.siteColor;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){return false;},function (){return false;});				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.saveSiteColor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.editSiteColor = function(siteColorId){
			asVm.siteColor = {};
			var url = "/awacp/getSiteColor";
			if(siteColorId){
				url = "/awacp/getSiteColor/"+siteColorId;
			}
			AjaxUtil.getData(url, Math.random())
			.success(function(data, status, headers){
				if(data && data.siteColor){
					asVm.siteColor = data.siteColor;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.editSiteColor::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.saveSiteEmailAccount = function(){
			var message = "Site email account saved successfully.";
			var url = "/awacp/saveSiteEmailAccount";
			var update = false;
			if(asVm.siteEmailAccount && asVm.siteEmailAccount.id){
				asVm.siteEmailAccount.updatedById = StoreService.getUser().userId;
			}else{
				asVm.siteEmailAccount.createdById = StoreService.getUser().userId;
			}
			var formData = {};			
			formData["siteEmailAccount"] = asVm.siteEmailAccount;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){return false;},function (){return false;});				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.saveSiteEmailAccount::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.editSiteEmailAccount = function(siteEmailAccountId){
			asVm.siteColor = {};
			var url = "/awacp/getSiteEmailAccount";
			if(siteEmailAccountId){
				url = "/awacp/getSiteEmailAccount/"+siteEmailAccountId;
			}
			AjaxUtil.getData(url, Math.random())
			.success(function(data, status, headers){
				if(data && data.siteEmailAccount){
					asVm.siteEmailAccount = data.siteEmailAccount;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.editSiteEmailAccount::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.saveSiteMenuItem = function(){
			var message = "Site menu item saved successfully.";
			var url = "/awacp/saveSiteMenuItem";
			var update = false;
			if(asVm.menuItem && asVm.menuItem.id){
				asVm.menuItem.updatedById = StoreService.getUser().userId;
			}else{
				asVm.menuItem.createdById = StoreService.getUser().userId;
			}
			var formData = {};			
			formData["menuItem"] = asVm.menuItem;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){return false;},function (){return false;});				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.saveSiteMenuItem::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.editSiteMenuItem = function(menuItemId){
			asVm.menuItem = {};
			var url = "/awacp/getSiteMenuItem";
			if(menuItemId){
				url = "/awacp/getSiteMenuItem/"+menuItemId;
			}
			AjaxUtil.getData(url, Math.random())
			.success(function(data, status, headers){
				if(data && data.menuItem){
					asVm.menuItem = data.menuItem;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "AppSettingCtrl::asVm.editSiteMenuItem::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		asVm.initAppSetting = function(){
			if(asVm.target === 'siteinfo'){
				asVm.editSiteInfo();
			}else if(asVm.target === 'sitecolor'){
				asVm.editSiteColor();
			}else if(asVm.target === 'siteemailaccount'){
				asVm.editSiteEmailAccount();
			}else if(asVm.target === 'sitemenuitem'){
				asVm.editMenuItem();
			}
		}
	}		
})();



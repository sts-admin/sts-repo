(function() {
	'use strict';
	angular.module('awacpApp.services')
	.factory('AjaxUtil', function ($rootScope, $state, base, $timeout, StoreService, $uibModal, AlertService, basePath) {
		return {
			initHeaderInfo:function(){
				var accessToken = StoreService.getAccessToken();
				var headers = {
					'Authorization' : 'Bearer ' + accessToken,
					'Accept' : 'application/json'
				};
				jQuery.ajaxSetup({
					'headers' : headers,
					 dataType : 'json'
				});
			},
			isAuthorized: function(showAlert){
				var accessToken = StoreService.getAccessToken();
				if(accessToken != null && accessToken.length > 0){
					this.initHeaderInfo();
					return true;
				}
				if(!showAlert){
					StoreService.removeAll();
					$state.go("/");
					return false;
				}
				AlertService.showAlert(	'AWACP :: Alert!','You are not authorized to perform this operation tt.')
				.then(function (){StoreService.removeAll();alert("go to login");$state.go("/")},function (){return false;});
				return false;
			},	
			saveErrorLog:function(jqXHR, customMsg, showMsgDialog){
				var me = this;
				if (jqXHR.readyState == 0) {
					$rootScope.$apply(function(){ $rootScope.alert.noService = true;});
					StoreService.removeAll(); 
					AlertService.showAlert(	"AWACP :: Alert", "Unable to connect AWACP Services.")
					.then(function (){$state.go("/");},function (){return});
					return;
				}else{
					var msg = "Unable to complete request due to communication error.";
					var rs = "", unknownPassword = false;
					if(jqXHR.responseText && jqXHR.responseText !=null){	
						console.log(jqXHR.responseText);
						rs = JSON.parse(jqXHR.responseText);
						if(rs !=null && rs.error !=null){
							if(rs.error == 'invalid_token'){
								//msg = "Your user session expired, need to re-login.";	
								StoreService.removeAll();$state.go("/");				
								return;
							}else if("invalid_grant" === rs.error || "unknown_password" === rs.error_description){
								msg = "Invalid user credentials";
							}else if(rs.error === "unauthorized"){
								msg = "Unknown User";
							}
							AlertService.showAlert(	"AWACP :: Alert", msg)
							.then(function (){StoreService.removeAll();$state.go("/")},function (){});
						}						
					}else{
						if(showMsgDialog){
							var msg = "Unable to complete request due to communication error.";
							if(customMsg && customMsg.length > 0){ msg = customMsg;	}
							AlertService.showAlert('AWACP :: Alert!', msg)
							.then(function (){ StoreService.removeAll();$state.go("/")}, function (){});
						}
					}
				}				
				var errorText = jqXHR.status;
				if(jqXHR.errorSource && jqXHR.errorSource.length > 0){
					errorText = jqXHR.errorSource + ", CAUSE:"+jqXHR.statusText;
				}
				var formData = {}, errorLog = {};
				errorLog["errorCode"] = jqXHR.status;
				errorLog["errorText"] = errorText;
				errorLog["errorData"] = jqXHR.responseText;
				errorLog["serviceUrl"] = jqXHR.url;
				formData["errorLog"] = errorLog;
				this.submitDataNoSecure("/js/saveErrorLog",formData)
				.success(function (data, textStatus, jqXHR) {
				}).error(function (jqXHR, textStatus, errorThrown) {
				});	
			},
			toggleSpinner:function(buttonId, spinnerId, spinnerUrl, action){
				jQuery.noConflict();				
				if("disable" === action){
					angular.element("#"+buttonId).attr('disabled','disabled');
					angular.element("#"+spinnerId).html(""+spinnerUrl);
				}else{
					angular.element("#"+buttonId).removeAttr('disabled');
					angular.element("#"+spinnerId).html("");
				}
			},
			setPageSize: function(viewName, size, callback){
				var defaultSize = 20;
				this.getData("/awacp/setPageSizeByView?viewName="+viewName+"&size="+size, Math.random())
				.success(function(data, status, headers){
					if(data){
						if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
							callback("success", size);
						}
					}					
				})
				.error(function(jqXHR, textStatus, errorThrown){	
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback("fail", defaultSize);
					}
				});
			},
			getPageSize: function(viewName, callback){
				var size = 20;
				this.getData("/awacp/getSiteSettingByView?viewName="+viewName, Math.random())
				.success(function(data, status, headers){
					if(data && data.pageSetting){
						size = data.pageSetting.pageSize;
					}
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback("success", size);
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){	
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback("fail", size);
					}
				});
			},
			listCountries:function(callback){
				var countries = [];
				var url = "/awacp/listCountries";
				this.getData(url, Math.random())
				.success(function (data, status, headers) {					
					if(data && data.country && data.country.length > 0){
						jQuery.each(data.country, function(k, v){
							countries.push(v);
						});
						if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
							callback(countries, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},	
			listStates:function(countryId, callback){
				var states = [];
				var url = countryId == null || countryId.length <= 0?"/awacp/listStates":"/awacp/listStatesByCountry/"+countryId;
				this.getData(url, Math.random())
				.success(function (data, status, headers) {					
					if(data && data.state && data.state.length > 0){
						jQuery.each(data.state, function(k, v){
							states.push(v);
						});
					}
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(states, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},	
			
			getData: function (serviceUrl, data) {
				if(this.isAuthorized(true)){
					return this.getDataNoSecure(serviceUrl, data);
				}				
			},			
			getDataNoSecure: function (serviceUrl, data) {
				return this.submitAJAXRequest(serviceUrl, data, "GET")
			},			
			submitData: function (serviceUrl, data) {
				if(this.isAuthorized(true)){
					return this.submitDataNoSecure(serviceUrl, data);
				}
			},			
			submitDataNoSecure: function (serviceUrl, data) {
				return this.submitAJAXRequest(serviceUrl, data, "POST")
			},
			deleteData: function (serviceUrl, data) {
				if(this.isAuthorized(true)){
					return this.deleteDataNoSecure(serviceUrl, data);
				}
			},			
			deleteDataNoSecure: function (serviceUrl, data) {
				return this.submitAJAXRequest(serviceUrl, data, "DELETE")
			},
			submitAJAXRequest: function (serviceUrl, data, methodType) {
				var methodContentType = "application/json; charset=utf-8"; 
				var methodDataType = "json"; 
				return this.postAJAXRequest(serviceUrl, data, methodType, methodContentType, methodDataType)
			},			
			postAJAXRequest: function (serviceUrl, data, methodType, methodContentType, methodDataType) {
				var finalServiceUrl = base+serviceUrl;
				jQuery.support.cors = true;
				return jQuery.ajax(
					{
						type: methodType, 
						url: finalServiceUrl, 
						data: JSON.stringify(data),
						contentType:methodContentType,
						crossDomain: true,
						dataType: methodDataType,
						beforeSend: function(jqXHR, settings) {
							jqXHR.url = settings.url;
						}
					}
				);
			},			
			uploadData: function(serviceUrl, data) {
				var serverUrl = base+serviceUrl;
				if(this.isAuthorized(true)){
					jQuery.support.cors = true;
					return jQuery.ajax({type: "POST",url: serverUrl,data: data,contentType: false,crossDomain: true,	  processData: false,cache : false});
				}
			},			
			downloadData: function(serviceUrl, data) {
				if(this.isAuthorized(true)){
					jQuery.support.cors = true;
					return jQuery.ajax({type: "GET",url: base+serviceUrl,crossDomain: true,dataType: "binary",headers:{'Content-Type':'image/png','X-Requested-With':'XMLHttpRequest'},processData: false,});
				};
			}
		};
	});
})();







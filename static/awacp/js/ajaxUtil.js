(function() {
	'use strict';
	angular.module('awacpApp.services', [])
	.factory('AjaxUtil', function ($rootScope, $http, $state, $window, store, base, resourceReadPath, $timeout) {
		return {
			saveErrorLog:function(jqXHR, customMsg, showMsgDialog){
				var me = this;
				if (jqXHR.readyState == 0) {
					$rootScope.$apply(function(){
						$rootScope.alert.noService = true;
					})
					me.logout();
				}else{
					var msg = "";
					var rs = "";
					var unknownPassword = false;
					if(jqXHR.statusText == "unauthorized" || jqXHR.statusText == "Unauthorized"){
						this.showMessage("Unknown User");
						$timeout(function(){
							jQuery.noConflict();
							$("#message-dialog").modal("hide");
							me.logout();
							return;
						},2000);
					}else if(jqXHR.responseText && jqXHR.responseText!=null){
						rs = JSON.parse(jqXHR.responseText);
						if(rs!=null && rs.error!=null && rs.error == 'invalid_token'){
							this.showMessage("Your user session expired, need to re-login");
							$timeout(function(){
								jQuery.noConflict();
								$("#message-dialog").modal("hide");
								me.logout();
								return;
							},2000);							
						}else if(rs!=null && rs.error_description!=null && rs.error_description == 'unknown_password'){
							this.showMessage("Invalid user credentials");	
							$timeout(function(){
								jQuery.noConflict();
								$("#message-dialog").modal("hide");
								me.logout();
								return;
							},2000);
						}
					}else{
						if(showMsgDialog){
							if(customMsg && customMsg.length > 0){
								this.showMessage(customMsg);
							}else{
								this.showMessage("Unable to complete request due to communication error.");
							}
							$timeout(function(){
								jQuery.noConflict();
								$("#message-dialog").modal("hide");
								me.logout();
								return;
							},1000);
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
					/*alert("Log save success");*/
				}).error(function (jqXHR, textStatus, errorThrown) {
					/*alert("Log save failed");*/
				});	
			},
			toggleSpinner:function(buttonId, spinnerId, spinnerUrl, action){
				jQuery.noConflict();				
				if("disable" === action){
					$("#"+buttonId).attr('disabled','disabled');
					$("#"+spinnerId).html(""+spinnerUrl);
				}else{
					$("#"+buttonId).removeAttr('disabled');
					$("#"+spinnerId).html("");
				}
			},
			listCountries:function(callback){
				var countries = [];
				this.getData("/js/listCountries", Math.random())
				.success(function (data, status, headers) {					
					if(data && data.country && data.country.length > 0){
						$.each(data.country, function(k, v){
							countries.push(v);
						});
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(countries, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			listFeaturesByBoatId:function(boatId, callback){
				var features = [];
				this.getData("/ob/listFeaturesByBoatId?boatId"+boatId, Math.random())
				.success(function (data, status, headers) {					
					if(data && data.boatFeature && data.boatFeature.length > 0){
						$.each(data.boatFeature, function(k, v){
							features.push(v);
						});
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(features, "success");
						}
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			listBoats:function(withDetail, callback){
				var boats = [];
				var url = withDetail != null && withDetail.length > 0? "/ob/listBoatsWithDetail":"/ob/listBoats"
				this.getData(url, Math.random())
				.success(function (data, status, headers) {					
					if(data && data.boat && data.boat.length > 0){						
						$.each(data.boat, function(k, v){
							boats.push(v);
						});						
					}
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(boats, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			listFeatures:function(callback){
				var features = [];
				this.getData("/ob/listFeatures", Math.random())
				.success(function (data, status, headers) {					
					if(data && data.feature && data.feature.length > 0){
						$.each(data.feature, function(k, v){
							features.push(v);
						});
					}
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(features, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			listStates:function(countryId, callback){
				var states = [];
				var url = countryId == null || countryId.length <= 0?"/js/listStates":"/js/listStatesByCountry/"+countryId;
				this.getData(url, Math.random())
				.success(function (data, status, headers) {					
					if(data && data.state && data.state.length > 0){
						$.each(data.state, function(k, v){
							states.push(v);
						});
					}
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(states, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			getUserAddress:function(userId, callback){
				this.getData("/js/getAddress?userId="+ userId, Math.random())
				.success(function (data, status, headers) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(data, "success");
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}
				});
			},
			getUserDetail: function(callback){
				this.getData("/ob/getUserDetailByUserEmail?userEmail="+ this.getUserName(), Math.random())
				.success(function (data, status, headers) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(data, "success");
					}					
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(jqXHR, "error");
					}					
				});
			},
			setUser: function(user, callback){
				var me = this;
				if(user == null){ //get user and then set in store for further reference
					this.getData( "/js/getUserDetails?userNameOrEmail="+ this.getUserName(), Math.random())
					.success(function (data, status, headers) {
						store.set('user', data.user);
						$rootScope.$apply(function(){
							$rootScope.user.isLoggedIn = me.isLoggedIn();
							$rootScope.user.profileImageUrl = data.user.photoUrl;
						});						
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(data.user, "success");
						}
					})
					.error(function (jqXHR, textStatus, errorThrown) {
						if (typeof callback !== 'undefined' && $.isFunction(callback)) {
							callback(jqXHR, "error");
						}
					});
				}else{
					store.set('user', user);
					$rootScope.$apply(function(){
						$rootScope.user.isLoggedIn = me.isLoggedIn();
						$rootScope.user.profileImageUrl = user.photoUrl;
					});	
					if (typeof callback !== 'undefined' && $.isFunction(callback)) {
						callback(user, "success");
					}
				}				
			},
			getFileAccessPath:	function(){
				return fileAccessPath;
			},
			showMessage: function(msg){
				$("div#message-dialog div#message-dialog-content").html(msg);			 
				jQuery.noConflict();
				return $("#message-dialog").modal("show");
			},
			showConfirm: function(msg){
				$("div#confirm-dialog div#confirm-dialog-content").html(msg);			 
				jQuery.noConflict();
				return $("#confirm-dialog").modal("show");
			},
			fetchSearchCriteria:function(criteria){
				if(criteria){
					store.set("boatSearchCriteria", criteria);
				}
				return store.get("boatSearchCriteria");
			},
			
			fetchBoatCategories:function(){
				var me = this;
				this.getDataNoSecure("/ob/listBoatCategories", Math.random())
				.success(function (data, status, headers, config, profile) {
					if(data && data.boatCategory && data.boatCategory.length > 0){
						$.each(data.boatCategory, function(k, v){
							$rootScope.$apply(function(){
								$rootScope.boatCategories.push(v);
							});						
						});
					}
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					var msg = "Error listing categories";
					me.saveErrorLog(jqXHR, msg, true);
				});	
			},
			logout: function(){
				store.remove('token'); 
				store.remove('role');
				store.remove('facebook');
				store.remove('user');
				$timeout(function(){
					$rootScope.$apply(function(){
						$rootScope.user.isLoggedIn = me.isLoggedIn();
						$rootScope.user.profileImageUrl = me.profileImageUrl()
					});					
				}, 500);
				var me = this;
				var accessToken = store.get('token');
				var headers = {
					'Authorization' : 'Bearer ' + accessToken,
					'Accept' : 'application/json'
				};
				$.ajaxSetup({
					'headers' : headers,
					 dataType : 'json'
				});
				$.ajax({url: base+'/logout',type: "POST",data: Math.random(),contentType: "text/plain",crossDomain: true, dataType:"text"})
				.success(function (data, status, headers, config) {											
					$state.go("/");
				}).error(function (jqXHR, textStatus, errorThrown) {
					if (jqXHR.readyState == 0) {
						$rootScope.$apply(function(){
							$rootScope.alert.noService = true;
						});
					}								
				});
			},			
			login: function (username, password, loginType) {
				$.support.cors = true;
				var data ={
				  'username' 	  : username+'`'+loginType,
				  'password' 	  : password,
				  'client_id' 	  : 'awacpservices',
				  'client_secret' : 'awacpservices',
				  'grant_type'    : 'password'
				};
				return $.ajax(
					{
						url: base+'/oauth/token',
						type: "POST",
						data: data,
						contentType: "application/x-www-form-urlencoded",
						crossDomain: true,
						beforeSend: function(jqXHR, settings) {
							jqXHR.url = settings.url;
						}
					}
				);
			},			
			authorized: function(){
				var accessToken = store.get('token');
				var headers = {
					'Authorization' : 'Bearer ' + accessToken,
					'Accept' : 'application/json'
				};
				$.ajaxSetup({
					'headers' : headers,
					 dataType : 'json'
				});
				return true;
			},
			getUser:function(){
				return store.get('user');
			},
			profileImageUrl:function(){	
				var user = store.get('user');
				if(user!=null){
					return user.photoUrl;
				}
				return "";
			},
			getUserName: function() {
				return store.get("userName");
			},
			getToken: function() {
				return store.get('token');
			},
			isLoggedIn: function() {
				return !!store.get('token');
			},		
			getData: function (serviceUrl, data) {
				if(!this.authorized()){return false;};
				return this.getDataNoSecure(serviceUrl, data)
			},			
			getDataNoSecure: function (serviceUrl, data) {
				return this.submitAJAXRequest(serviceUrl, data, "GET")
			},			
			submitData: function (serviceUrl, data) {
				if(!this.authorized()){return false;};
				return this.submitDataNoSecure(serviceUrl, data)
			},			
			submitDataNoSecure: function (serviceUrl, data) {
				return this.submitAJAXRequest(serviceUrl, data, "POST")
			},
			deleteData: function (serviceUrl, data) {
				if(!this.authorized()){return false;};
				return this.deleteDataNoSecure(serviceUrl, data)
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
				$.support.cors = true;
				return $.ajax(
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
			uploadData: function uploadData(serviceUrl, data) {
				var serverUrl = base+serviceUrl;			
				if(!this.authorized()){return false;};
				$.support.cors = true;
				return $.ajax({type: "POST",url: serverUrl,data: data,contentType: false,crossDomain: true,	  processData: false,cache : false});
			},			
			downloadData: function uploadData(serviceUrl, data) {
				if(!this.authorized()){return false;};
				$.support.cors = true;
				return $.ajax({type: "GET",url: base+serviceUrl,crossDomain: true,dataType: "binary",headers:{'Content-Type':'image/png','X-Requested-With':'XMLHttpRequest'},processData: false,});
			}
		};
	});
})();







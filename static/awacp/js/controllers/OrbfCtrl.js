  (function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('OrbfCtrl', OrbfCtrl);
	OrbfCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService'];
	function OrbfCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService){
		var orbfVm = this;
		StoreService.remove("rtpQueryOptions-takeoff");
		StoreService.remove("rtpQueryOptions-quote");
		StoreService.remove("rtpQueryOptions-j");
		StoreService.remove("rtpQueryOptions-ob");
		
		orbfVm.truckers= [];		
		orbfVm.orbf = {};
		orbfVm.jobOrder = {};		
		orbfVm.estDate = {opened:false};
		orbfVm.estDatePicker = function(){
			orbfVm.estDate.opened = true;
		}
		orbfVm.cancelOrbfAction = function(){
			$state.go("orderbook-view");
		}
		orbfVm.initOrbfInputs = function(){
			orbfVm.getTruckers();
		}
		orbfVm.getTruckers = function(){
			orbfVm.truckers = [];
			orbfVm.pageNumber = orbfVm.currentPage;
			AjaxUtil.getData("/awacp/listTruckers/-1/-1", Math.random())
			.success(function(data, status, headers){
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
						orbfVm.truckers = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrbfCtrl::orbfVm.getTruckers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		orbfVm.editOrbf = function(orderBookId, jobOrderId){
			orbfVm.orbf = {};
			var obId = orderBookId;
			
			orbfVm.getJobOrder(jobOrderId);
			AjaxUtil.getData("/awacp/getOrbfByOrderBook/"+orderBookId, Math.random())
			.success(function(data, status, headers){
				if(data && data.orbf){
					if(data.orbf.estDate){
						data.orbf.estDate = new Date(data.orbf.estDate);
					}
					orbfVm.orbf = data.orbf;
					orbfVm.orbf.orderBookId = obId;
					$scope.$digest();
					console.log(orbfVm.orbf);
				}
				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrbfCtrl::orbfVm.editOrbf::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		orbfVm.saveOrbf = function(){
			jQuery(".orbf-add-action").attr('disabled','disabled');
			jQuery("#orbf-add-spinner").css('display','block');	
			var update = false;
			var message = "Orbf Detail Created Successfully";
			if(orbfVm.orbf && orbfVm.orbf.id){
				message = "Orbf Detail Updated Successfully";
				orbfVm.orbf.updatedById = StoreService.getUser().userId;
				orbfVm.orbf.updatedByUserCode = StoreService.getUser().userCode;
				orbfVm.orbf.auditMessage = "Updated Orbf with  detail '"+orbfVm.orbf.orbf+"'";
				update = true;
			}else{
				orbfVm.orbf.createdByUserCode = StoreService.getUser().userCode;
				orbfVm.orbf.createdById = StoreService.getUser().userId;
				orbfVm.orbf.auditMessage = "Added Orbf with  detail '"+orbfVm.orbf.orbf+"'";
			}
			
			var formData = {};
			orbfVm.orbf.userNameOrEmail = StoreService.getUserName();
			formData["orbf"] = orbfVm.orbf;
			AjaxUtil.submitData("/awacp/saveOrbf", formData)
			.success(function(data, status, headers){
				jQuery(".orbf-add-action").removeAttr('disabled');
				jQuery("#orbf-add-spinner").css('display','none');
				orbfVm.orbf = {};
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){orbfVm.cancelOrbfAction();},function (){return false;});
				return;
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".orbf-add-action").removeAttr('disabled');
				jQuery("#orbf-add-spinner").css('display','none');
				jqXHR.errorSource = "OrbfCtrl::orbfVm.saveOrbf::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		orbfVm.getJobOrder = function(jobOrderId){			
			AjaxUtil.getData("/awacp/getJobOrder/"+jobOrderId, Math.random())
			.success(function(data, status, headers){
				if(data && data.jobOrder){
					orbfVm.jobOrder = data.jobOrder;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrbfCtrl::obVm.getJobOrder::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		if($state.params.orbfOrderBookId != undefined && $state.params.orbfJobOrderId != undefined){
			orbfVm.editOrbf($state.params.orbfOrderBookId, $state.params.orbfJobOrderId);
		}
	}		
})();



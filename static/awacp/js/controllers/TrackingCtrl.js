(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TrackingCtrl', TrackingCtrl);
	TrackingCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function TrackingCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var trackingVm = this;
		trackingVm.action = "Add";
	    
		$scope.timers = [];
		trackingVm.tracking= {};
		trackingVm.truckers = [];
		trackingVm.getTrucker = function(id){
			for(var i = 0; i < trackingVm.truckers.length; i++){
				if(trackingVm.truckers[i].id == id){
					return trackingVm.truckers[i];
				}
			}
			return null;
		}
		
		trackingVm.getTruckers = function(callback){
			trackingVm.truckers = [];
			trackingVm.pageNumber = trackingVm.currentPage;
			AjaxUtil.getData("/awacp/listTruckers/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					trackingVm.totalItems = data.stsResponse.totalCount;
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
					trackingVm.truckers = tmp;
					$scope.$digest();
					if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
						callback(data, "success");
					}
				}	
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TrackingCtrl::trackingVm.getTruckers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		trackingVm.saveTracking = function(){
			
			jQuery(".actions").attr('disabled','disabled');
			jQuery(".spinner").css('display','block');	
			var update = false;
			var message = "Trucker Tracking Detail Saved Successfully";
			if(trackingVm.tracking && trackingVm.tracking.id){
				message = "Trucker Tracking Detail Updated Successfully";
				trackingVm.tracking.updatedByUserCode = StoreService.getUser().userCode;
				update = true;
			}else{
				trackingVm.tracking.createdByUserCode = StoreService.getUser().userCode;
				trackingVm.tracking.createdById = StoreService.getUserId();
			}
			if(trackingVm.tracking.truckerOneId){
				trackingVm.tracking.truckerOne = trackingVm.getTrucker(trackingVm.tracking.truckerOneId);
			}
			if(trackingVm.tracking.truckerTwoId){
				trackingVm.tracking.truckerTwo = trackingVm.getTrucker(trackingVm.tracking.truckerTwoId);
			}
			if(trackingVm.tracking.truckerThreeId){
				trackingVm.tracking.truckerThree = trackingVm.getTrucker(trackingVm.tracking.truckerThreeId);
			}
			var formData = {};
			formData["tracking"] = trackingVm.tracking;
			AjaxUtil.submitData("/awacp/saveTracking", formData)
			.success(function(data, status, headers){
				if(data.tracking.truckerOne){
					data.tracking.truckerOneId = data.tracking.truckerOne.id;
				}
				if(data.tracking.truckerTwo){
					data.tracking.truckerTwoId = data.tracking.truckerTwo.id;
				}
				if(data.tracking.truckerThree){
					data.tracking.truckerThreeId = data.tracking.truckerThree.id;
				}
				trackingVm.tracking = data.tracking;
				$scope.$digest();
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){return false;},function (){return false;});
					return;
				}else{
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){return false;},function (){return false;});
				}	
				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".actions").removeAttr('disabled');
				jQuery(".spinner").css('display','none');
				
				
				if(1005 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "Unknow service provider, unable to track shipment status.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "TrackingCtrl::trackingVm.saveTracking::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
				
			});
		}
		trackingVm.editTracking = function(orderNumber){
			AjaxUtil.getData("/awacp/getTrackingByOrderNumber/"+orderNumber, Math.random())
			.success(function(data, status, headers){
				if(data && data.tracking){	
					if(data.tracking.truckerOne){
						data.tracking.truckerOneId = data.tracking.truckerOne.id;
					}
					if(data.tracking.truckerTwo){
						data.tracking.truckerTwoId = data.tracking.truckerTwo.id;
					}
					if(data.tracking.truckerThree){
						data.tracking.truckerThreeId = data.tracking.truckerThree.id;
					}				
					trackingVm.tracking = data.tracking;
					$scope.$digest();
				}				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TrackingCtrl::trackingVm.editTracking::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		var state = $state.current.name;
		if(state === 'tracking'){
			if($state.params.invOrderBookNumber != null && $state.params.invOrderBookId != null && $state.params.invOrderItemId != null && $state.params.invType != null){
				trackingVm.getTruckers(function(data, sts){
					trackingVm.tracking.invOrderBookId = $state.params.invOrderBookId;
					trackingVm.tracking.orderInvType = $state.params.invType;
					trackingVm.tracking.orderBookInvItemId = $state.params.invOrderItemId;
					trackingVm.tracking.orderBookNumber = $state.params.invOrderBookNumber;
					trackingVm.editTracking(trackingVm.tracking.invOrderBookId);
				});	
			}			
		}
	}		
})();



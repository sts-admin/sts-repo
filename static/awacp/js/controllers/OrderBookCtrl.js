  (function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('OrderBookCtrl', OrderBookCtrl);
	OrderBookCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService'];
	function OrderBookCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService){
		var obVm = this;
		obVm.orderCategories = [{id:'REGULAR', title:'Regular Order'}, {id:'AW', title:'AW Order'}, {id:'AWF', title:'AWF Order'}, {id:'SBC', title:'SBC Order'}, {id:'SPL', title:'SPL Order'}, {id:'J', title:'J Order'}, {id:'Q', title:'Q Order'}];
		obVm.spInstructions = [{id:'A', title:'Revision A'}, {id:'B', title:'Revision B'}, {id:'C', title:'Revision C'}, {id:'RPL', title:'(RPL) Replacement Order'}, {id:'PDQ', title:'(PDQ) Premium Quick Ship'}, {id:'DDC', title:'Direct Digital Control'}];
		obVm.estDate = {opened:false};
		obVm.factories = [];
		obVm.shiptos = [];
		obVm.invItems = [];
		obVm.bookingItems = [];
		obVm.jobOrder = {};
		obVm.tmpFactories = [];
		
		
		
		obVm.openAnother = true;		
		obVm.selectedJobOrder = {};
		$scope.timers = [];
		
		obVm.totalItems = -1;
		obVm.currentPage = 1;
		obVm.pageNumber = 1;
		obVm.pageSize = 20;
		obVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		obVm.users = [];
		obVm.contractors = [];
		obVm.orderBooks = [];
		obVm.orderBook = {invItems:[]};
		
		obVm.setFactory = function(obInvType){
			if(obInvType === 'REGULAR'){
				return;
			}			
			obVm.listInvItems(obInvType);
		}
		
		obVm.getJobByOrderId = function(orderId){
			obVm.jobOrder = {};
			AjaxUtil.getData("/awacp/getJobOrderByOrderId/"+orderId, Math.random())
			.success(function(data, status, headers){
				if(data && data.jobOrder){
					obVm.jobOrder = data.jobOrder;
					obVm.orderBook.jobId = obVm.jobOrder.id;
					obVm.orderBook.jobName = obVm.jobOrder.jobName;
					obVm.orderBook.jobAddress = obVm.jobOrder.jobAddress;
					obVm.orderBook.contractorId = obVm.jobOrder.contractorId;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.getJobByOrderId::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		obVm.listInvItems = function(source){
			obVm.invItems = [];
			AjaxUtil.getData("/awacp/listInventoryItems/"+source, Math.random())
			.success(function(data, status, headers){
				if(data && data.inventoryDTO){
					var tmp = [];
					if(jQuery.isArray(data.inventoryDTO)) {
						jQuery.each(data.inventoryDTO, function(k, v){
							tmp.push(v);
						});
					}else {
						tmp.push(data.inventoryDTO);
					}
					$scope.$apply(function(){
						obVm.invItems = tmp;
					});					
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.listInvItems::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		obVm.estDatePicker = function(){
			obVm.estDate.opened = true;
		}
		obVm.action = "Add New";	
		obVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("ORDERBOOK", size, function(status, size){
				if("success" === status){
					obVm.pageSize = size;
					obVm.pageChanged();
				}
			});
		}
		
		obVm.getPageSize = function(){
			AjaxUtil.getPageSize("ORDERBOOK", function(status, size){
				if("success" === status){
					obVm.pageSize = size;
				}
			});
		}
		obVm.pageChanged = function() {
			obVm.listOrderBooks();
		};	
		obVm.setPage = function (pageNo) {
			obVm.currentPage = pageNo;
		};
		obVm.pageChanged = function() {
			console.log('Page changed to: ' + obVm.currentPage);
		};	
		obVm.cancelOrderBookAction = function(){
			$state.go("orderbook-view");
		}
		obVm.getContractors = function(){
			obVm.contractors = [];
			AjaxUtil.getData("/awacp/listContractors/1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						tmp.push(data.stsResponse.results);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
						});
					}
					obVm.contractors = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.getContractors::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.getUsers = function(){
			obVm.users = [];
			AjaxUtil.getData("/awacp/listUser/1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						var t = data.stsResponse.results;
						t.customName = t.userCode + " - "+ t.firstName;
						tmp.push(t);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
							v.customName = v.userCode + " - "+ v.firstName;
							tmp.push(v);
						});
					}	
					$scope.$apply(function(){
						obVm.users = tmp;
					});						
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		obVm.getShipTos = function(){
			obVm.shiptos = [];
			AjaxUtil.getData("/awacp/listShipTos/1/-1", Math.random())
			.success(function(data, status, headers){				
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						tmp.push(data.stsResponse.results);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
						});
					}	
						obVm.shiptos= tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.getShipTos::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.initOrderBookMasterInputs = function(){
			obVm.getUsers();
			obVm.getShipTos();
			obVm.getContractors();
			obVm.listFactories();
		}
		obVm.isValidDateRange =function(fDate, lDate){
			var sDate = new Date(fDate);
			var eDate = new Date(lDate);
			return (eDate >= sDate);
		}
		obVm.editOrderBook = function(){
			if($state.params.id != undefined){
				var formData = {};
				AjaxUtil.getData("/awacp/getJobOrder/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.orderBook){
						$scope.$apply(function(){
							obVm.orderBook = data.orderBook;	
							if(obVm.orderBook.bidders){
								if(jQuery.isArray(obVm.orderBook.bidders)) {
									obVm.selectedBidders = obVm.orderBook.bidders;
								}else{
									obVm.selectedBidders = [];
									obVm.selectedBidders.push(obVm.orderBook.bidders);
								}								
							}
							if(obVm.orderBook.generalContractors){
								if(jQuery.isArray(obVm.orderBook.generalContractors)) {
									obVm.selectedContractors = obVm.orderBook.generalContractors;
								}else{
									obVm.selectedContractors = [];
									obVm.selectedContractors.push(obVm.orderBook.generalContractors);
								}								
							}
							obVm.action = obVm.orderBook && obVm.orderBook.id?"Update":"Add New";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "OrderBookCtrl::obVm.editOrderBook::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		obVm.checkOrderLimit = function(invType, lineItemId, itemAtIndex){
			AjaxUtil.getData("/awacp/getCurrentAvailableQty/"+invType+"/"+lineItemId, Math.random())
			.success(function(data, status, headers){				
				if(data && data.quantity){
					if(parseInt(data.quantity) < obVm.orderBook.invItems[itemAtIndex].orderQty){
						AlertService.showAlert(	'AWACP :: Alert!', "Order quantity must not be exceeding available quantity")
						.then(function (){obVm.orderBook.invItems[itemAtIndex].orderQty = 0; return false;},function (){return false;});
						return;
					}
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.checkOrderLimit::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.saveOrderBook = function(){
			jQuery(".orderBook-add-action").attr('disabled','disabled');
			jQuery("#orderBook-add-spinner").css('display','block');	
			var update = false;
			var message = "New Order Book Detail Created Successfully";
			if(obVm.orderBook && obVm.orderBook.id){
				message = "Order Book Detail Updated Successfully";
				obVm.orderBook.updatedByUserCode = StoreService.getUser().userCode;
				update = true;
			}else{
				obVm.orderBook.createdByUserCode = StoreService.getUser().userCode;
			}
			//filter inventory items for which order quantity input provided.
			
			var formData = {};
			obVm.orderBook.userNameOrEmail = StoreService.getUserName();
			formData["orderBook"] = obVm.orderBook;
			AjaxUtil.submitData("/awacp/saveOrderBook", formData)
			.success(function(data, status, headers){
				jQuery(".orderBook-add-action").removeAttr('disabled');
				jQuery("#orderBook-add-spinner").css('display','none');
				obVm.orderBook = {};
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){obVm.cancelOrderBookAction();},function (){return false;});
				return;
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".orderBook-add-action").removeAttr('disabled');
				jQuery("#orderBook-add-spinner").css('display','none');
				jqXHR.errorSource = "OrderBookCtrl::obVm.saveOrderBook::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.listOrderBooks = function(){
			obVm.orderBooks = [];
			AjaxUtil.getData("/awacp/listOrderBooks/"+obVm.currentPage+"/"+obVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					obVm.totalItems = 	data.stsResponse.totalCount;
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
						obVm.orderBooks = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.listOrderBooks::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.listFactories = function(){
			obVm.factories = [];
			AjaxUtil.getData("/awacp/listFactories/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					obVm.totalItems = 	data.stsResponse.totalCount;
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
						obVm.tmpFactories = obVm.factories = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.listOrderBooks::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});			
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		obVm.editOrderBook();
		obVm.getPageSize();
	}		
})();



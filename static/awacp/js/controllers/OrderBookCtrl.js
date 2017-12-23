  (function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('OrderBookCtrl', OrderBookCtrl);
	OrderBookCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService'];
	function OrderBookCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService){
		var obVm = this;
		obVm.showReportForm = true;
		obVm.report = {mode:'input'};
		obVm.orderCategories = [{id:'REGULAR', title:'Regular Order'}, {id:'AW', title:'AW Order'}, {id:'AWF', title:'AWF Order'}, {id:'SBC', title:'SBC Order'}, {id:'SPL', title:'SPL Order'}, {id:'J', title:'J Order'}, {id:'Q', title:'Q Order'}];
		obVm.spInstructions = [{id:'A', title:'Revision A'}, {id:'B', title:'Revision B'}, {id:'C', title:'Revision C'}, {id:'RPL', title:'(RPL) Replacement Order'}, {id:'PDQ', title:'(PDQ) Premium Quick Ship'}, {id:'DDC', title:'Direct Digital Control'}];
		
		obVm.factories = [];
		obVm.shiptos = [];
		obVm.invItems = [];
		obVm.bookingItems = [];
		obVm.jobOrder = {};
		obVm.tmpFactories = [];
		
		obVm.openAnother = true;		
		obVm.selectedJobOrder = {};
		obVm.selectedOrderBook = {};
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
		obVm.selectedTakeoff = {};
		obVm.selectedQuote = {};

		obVm.rptYearRange = [{id:2015, val:"2015"}, {id:2016, val:"2016"}, {id:2017, val:"2017"}];
		obVm.rptFromDate = {opened:false};
		obVm.estDate = {opened:false};
		obVm.rptToDate = {opened:false};
		obVm.rptEstDate = {opened:false};
		obVm.estDatePicker = function(){
			obVm.estDate.opened = true;
		}
		obVm.rptEstDatePicker = function(){
			obVm.rptEstDate.opened = true;
		}
		obVm.rptFromDatePicker = function(){
			obVm.rptFromDate.opened = true;
		}
		obVm.rptToDatePicker = function(){
			obVm.rptToDate.opened = true;
		}
		
		obVm.takeoffInfoPopover = {
			templateUrl: 'templates/takeoff-info-ob.html',
			title: 'Takeoff Detail'
		};
		obVm.quotePopover = {
			templateUrl: 'templates/quote-info-ob.html',
			title: 'Quote Detail'
		};
		obVm.jobOrderPopover = {
			templateUrl: 'templates/joborder-info-ob.html',
			title: 'Job Order Detail'
		};
		obVm.orderBookPopover = {
			templateUrl: 'templates/orderbook-info-ob.html',
			title: 'Order Book Detail'
		};
		obVm.showPodFile = function(obId){
			AjaxUtil.getData("/awacp/getShipingStatus/"+obId, Math.random())
			.success(function(data, status, headers){
				if(data && data.shipmentStatus){
					/*alert(data.shipmentStatus.podUrl);*/
					$window.open(data.shipmentStatus.podUrl);
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.showPodFile::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
			
		}
		obVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					obVm.updateFileUploadCount(source, sourceId, filePattern);
				}
			});
		}
		
		obVm.updateFileUploadCount = function(source, sourceId, docType){
			if(obVm.orderBooks && obVm.orderBooks.length > 0){
				for(var i = 0; i < obVm.orderBooks.length; i++){
					if(obVm.orderBooks[i].id === sourceId){
						if(source.includes("OB_A_DOC")){
							obVm.orderBooks[i].obADocCount = (parseInt(obVm.orderBooks[i].obADocCount) + 1);
						}else if(source.includes("OB_Y_XLS")){
							obVm.orderBooks[i].obYXlsDocCount = (parseInt(obVm.orderBooks[i].obYXlsDocCount) + 1);
						}else if(source.includes("OB_ACK_PDF")){
							obVm.orderBooks[i].obAckPdfDocCount = (parseInt(obVm.orderBooks[i].obAckPdfDocCount) + 1);
						}else if(source.includes("OB_FRT_PDF")){
							obVm.orderBooks[i].obFrtPdfDocCount = (parseInt(obVm.orderBooks[i].obFrtPdfDocCount) + 1);
						}	
						break;
					}
				}
			}
		}
		
		obVm.showQuoteInfo = function(takeoffId){
			AjaxUtil.getData("/awacp/getTakeoff/"+takeoffId, Math.random())
			.success(function(data, status, headers){
				if(data && data.takeoff){
					$scope.$apply(function(){
						if(data.takeoff.hasOwnProperty('bidders') && !jQuery.isArray(data.takeoff.bidders)){
							var b = [];
							b.push(data.takeoff.bidders);
							data.takeoff["bidders"] = b;
						}
						if(data.takeoff.hasOwnProperty('generalContractors') && !jQuery.isArray(data.takeoff.generalContractors)){
							var gc = [];
							gc.push(data.takeoff.generalContractors);
							data.takeoff["generalContractors"] = gc;
						}
						obVm.selectedQuote = data.takeoff;				
					});
					
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.showQuoteInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.showTakeoffInfo = function(takeoffId){
			AjaxUtil.getData("/awacp/getTakeoff/"+takeoffId, Math.random())
			.success(function(data, status, headers){
				if(data && data.takeoff){
					$scope.$apply(function(){
						if(data.takeoff.hasOwnProperty('bidders') && !jQuery.isArray(data.takeoff.bidders)){
							var b = [];
							b.push(data.takeoff.bidders);
							data.takeoff["bidders"] = b;
						}
						if(data.takeoff.hasOwnProperty('generalContractors') && !jQuery.isArray(data.takeoff.generalContractors)){
							var gc = [];
							gc.push(data.takeoff.generalContractors);
							data.takeoff["generalContractors"] = gc;
						}
						obVm.selectedTakeoff = data.takeoff;				
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.showTakeoffInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.showOrderBookInfo = function(orderBook){
			obVm.selectedOrderBook = orderBook;		
		}
		obVm.showJobOrderInfo = function(jobId){
			AjaxUtil.getData("/awacp/getJobOrder/"+jobId, Math.random())
			.success(function(data, status, headers){
				if(data && data.jobOrder){
					$scope.$apply(function(){
						obVm.selectedJobOrder = data.jobOrder;				
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.showJobOrderInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.cancelOrderBook = function(id){
			AlertService.showConfirm(	'AWACP :: Confirmation!', "Are you sure cancel this order book?")
			.then(function (){
				AjaxUtil.getData("/awacp/cancelOrderBook/"+id, Math.random())
				.success(function(data, status, headers){
					if(data && data.result){
						AlertService.showAlert(	'AWACP :: Message!', "Order Book Cancelled Successfully")
						.then(function (){obVm.cancelOrderBookAction();},function (){return false;});
						return;
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "OrderBookCtrl::obVm.cancelOrderBook::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			},
			function (){
				return false;
			});
		}
		obVm.uncancellOrderBook = function(id){
			AlertService.showConfirm(	'AWACP :: Confirmation!', "Are you sure to make this order book active?")
			.then(function (){
				AjaxUtil.getData("/awacp/uncancellOrderBook/"+id, Math.random())
				.success(function(data, status, headers){
					if(data && data.result){
						AlertService.showAlert(	'AWACP :: Message!', "Order Book Activated Successfully")
						.then(function (){obVm.cancelOrderBookAction();},function (){return false;});
						return;
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "OrderBookCtrl::obVm.uncancellOrderBook::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			},
			function (){
				return false;
			});
		}
		obVm.setFactory = function(obInvType){
			if(obInvType === 'REGULAR'){
				return;
			}			
			obVm.listInvItems(obInvType, function(items){				
				$scope.$apply(function(){
					obVm.invItems = items;
				});
			});
		}
		
		obVm.getJobByOrderId = function(orderNumber){
			obVm.jobOrder = {};
			AjaxUtil.getData("/awacp/getJobOrderByOrderNumber/"+orderNumber, Math.random())
			.success(function(data, status, headers){
				if(data && data.jobOrder){
					obVm.jobOrder = data.jobOrder;
					obVm.orderBook.jobId = obVm.jobOrder.id;
					obVm.orderBook.jobOrderNumber = obVm.jobOrder.orderNumber;
					obVm.orderBook.jobName = obVm.jobOrder.jobName;
					obVm.orderBook.jobAddress = obVm.jobOrder.jobAddress;
					obVm.orderBook.contractorId = obVm.jobOrder.contractorId;
					obVm.orderBook.quoteId = obVm.jobOrder.quoteId;
					obVm.orderBook.takeoffId = obVm.jobOrder.takeoffId;
					$scope.$digest();
				}else{
					AlertService.showAlert(	'AWACP :: Alert!', "No job order detail found.")
					.then(function (){return false;},function (){return false;});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.getJobByOrderId::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		obVm.listInvItems = function(source, callback){
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
					callback(tmp);
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.listInvItems::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
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
		obVm.validateReportInputs = function(callback){
			if(obVm.jobOrder.toDate && !obVm.isValidDateRange(obVm.jobOrder.fromDate, obVm.jobOrder.toDate)){
				callback(false, "From date should be greater than or equal to To date.");
				return;
			}else{
				callback(true, "");
				return;
			}
		}
		obVm.toggleReportFormVisibility = function(){
			obVm.showReportForm = !obVm.showReportForm;
		}
		obVm.rememberReportQueryParams = function(){
			jQuery("#orderBook-rpt-btn").attr('disabled','disabled');
			jQuery("#orderBook-rpt-spinner").css('display','block');	
			obVm.validateReportInputs(
				function(isValid, msg){
					if(isValid == false){
						AlertService.showAlert(
						'AWACP :: Message!',
						msg
						).then(function (){	orderBook
								jQuery("#-rpt-btn").removeAttr('disabled');
								jQuery("#orderBook-rpt-spinner").css('display','none');
								return;
							},function (){	return; } );							
					}else{
						obVm.generateReport();
					}
				}
			);	
		}
		obVm.generateReport = function(){
			obVm.report.mode = 'input';
			obVm.orderBooks = [];
			if(obVm.orderBook.fromDate && !obVm.orderBook.toDate){
				obVm.orderBook.toDate = obVm.orderBook.fromDate;
			}
			obVm.orderBook.pageNumber = obVm.currentPage;
			obVm.orderBook.pageSize = obVm.pageSize;
			var formData = {};
			formData["orderBook"] = obVm.orderBook;
			AjaxUtil.submitData("/awacp/generateOrderBookReport", formData)
			.success(function(data, status, headers){
				obVm.report.mode = 'output';
				jQuery("#orderBook-rpt-btn").removeAttr('disabled');
				jQuery("#orderBook-rpt-spinner").css('display','none');
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
				}else{
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.generateReport::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		obVm.getItemIndex = function(invItemId){
			var index = -1;
			for(var i = 0; i < obVm.invItems.length; i++){
				if(obVm.invItems[i].id == invItemId){
					index = i;
					break;
				}
			}
			return index;
		}
		obVm.editOrderBook = function(){			
			AjaxUtil.getData("/awacp/getOrderBook/"+$state.params.id, Math.random())
			.success(function(data, status, headers){
				if(data && data.orderBook){
					var tmpItems = [];
					
					if(data.orderBook.invItems){
						if(jQuery.isArray(data.orderBook.invItems)){
							tmpItems = data.orderBook.invItems;
						}else{
							tmpItems.push(data.orderBook.invItems);
						}
					}
					
					data.orderBook.invItems = [];
					obVm.listInvItems(data.orderBook.obCategory, function(items){
						$scope.$apply(function(){
							obVm.invItems = items;								
						});
						if(tmpItems && tmpItems.length > 0){
							for(var i = 0; i < tmpItems.length; i++){
								data.orderBook.invItems[obVm.getItemIndex(tmpItems[i].invItemId)] = tmpItems[i];
							}
						}
						$scope.$apply(function(){
							obVm.orderBook = data.orderBook;
							if(obVm.orderBook.estDate){
								obVm.orderBook.estDate = new Date(obVm.orderBook.estDate);
							}
							obVm.action = obVm.orderBook && obVm.orderBook.id?"Update":"Add New";	
						});
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "OrderBookCtrl::obVm.editOrderBook::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
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
				obVm.orderBook.updatedById = StoreService.getUser().userId;
				obVm.orderBook.auditMessage = "Updated Order with ID: '"+obVm.orderBook.orderBookNumber+"'";
				update = true;
			}else{
				obVm.orderBook.createdByUserCode = StoreService.getUser().userCode;
				obVm.orderBook.createdById = StoreService.getUser().userId;
				obVm.orderBook.auditMessage = "Added Order with ID: '"+obVm.orderBook.orderBookNumber+"'";
			}
			var noInvItems = true;
			if(obVm.orderBook.invItems && obVm.orderBook.invItems.length > 0){
				jQuery.each(obVm.orderBook.invItems, function(k, d){
					d.createdById = StoreService.getUserId();
					d.createdByUserCode = StoreService.getUser().userCode;
					if(d.orderQty && (parseInt(d.orderQty) > 0 || d.orderQty.length > 0)){
						noInvItems = false;
						return false;
					}
				});
			}
			if(noInvItems && obVm.orderBook.obCategory != 'REGULAR' ){
				jQuery(".orderBook-add-action").removeAttr('disabled');
				jQuery("#orderBook-add-spinner").css('display','none');
				AlertService.showAlert(	'AWACP :: Alert!', "Please select inventory item detail.")
				.then(function (){return false;},function (){return false;});
				return;
			}
			
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
			obVm.pageNumber = obVm.currentPage;
			if($state.params.qSource != undefined && $state.params.qSource.length > 0){
				AjaxUtil.getData("/awacp/getOrderBook/"+$state.params.qSource, Math.random())
				.success(function(data, status, headers){
					if(data && data.orderBook){
						$scope.$apply(function(){
							obVm.orderBooks.push(data.orderBook);
						});
					}					
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "QuoteCtrl::obVm.listOrderBooks::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}else{
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
		if($state.params.id != undefined){	
			obVm.editOrderBook();
		}
		
		obVm.getPageSize();
	}		
})();



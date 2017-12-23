  (function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('JobOrderCtrl', JobOrderCtrl);
	JobOrderCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService'];
	function JobOrderCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService){
		var jobVm = this;
		jobVm.showReportForm = true;
		jobVm.report = {mode:'input'};
		StoreService.remove("rtpQueryOptions-takeoff");
		StoreService.remove("rtpQueryOptions-quote");
		StoreService.remove("rtpQueryOptions-ob");
		jobVm.selectedJobId = "";
		jobVm.selectedOrderNumber = "";
		jobVm.showAddJobOrderLink = true;
		jobVm.jobOrderViewHeading = "View Job Order";
		jobVm.billTypes = ['INV', 'BILL', 'PTL'];
		jobVm.rptYearRange = [{id:2015, val:"2015"}, {id:2016, val:"2016"}, {id:2017, val:"2017"}];
		jobVm.jobStatuses = [{id:'false', title:"Live Job"}, {id:'true', title:"Cancelled Job"}];
		jobVm.jobStatus = [{id:'released', title:"Released Job"}, {id:'unreleased', title:"Un-released Job"}];
		jobVm.finalStatus = [{id:'false', title:"In-complete Job"}, {id:'true', title:"Completed Job"}];
		jobVm.invoiceModes = [{id:'bill', title:"BILL"}, {id:'ptl', title:"PARTIAL"}, {id:'inv', title:"INVOICE"}];
		jobVm.billType = {};
		if($state.params.jobId != undefined){
			jobVm.selectedJobId = $state.params.jobId;
		}
		if($state.params.orderNumber != undefined){
			jobVm.selectedOrderNumber = $state.params.orderNumber;
		}
		jobVm.openAnother = true;		
		jobVm.selectedjobOrder = {};
		jobVm.drawingDate = {opened:false};
		jobVm.revisedDate = {opened:false};
		jobVm.dueDate = {opened:false};		
		jobVm.arc_text = "NEW";
		jobVm.cont_text = "NEW";
		jobVm.eng_text = "NEW";
		$scope.timers = [];
		jobVm.specSettings = {displayProp: 'detail', idProp: 'id'};
		jobVm.totalItems = -1;
		jobVm.currentPage = 1;
		jobVm.pageNumber = 1;
		jobVm.pageSize = 20;
		jobVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		jobVm.users = [];
		jobVm.contractors = [];
		jobVm.engineers = [];
		jobVm.architects = [];	
		jobVm.contractors = [];
		jobVm.bidders = [];
		jobVm.jobOrders = [];
		jobVm.jobOrder = {};
		jobVm.selectedBidders = [];
		jobVm.selectedContractors = [];
		jobVm.jobOrderGcs = [];
		jobVm.jobOrderBidders = [];	
		
		jobVm.selectedTakeoff = {};
		jobVm.selectedQuote = {};
		jobVm.selectedOrderBook = {};

		jobVm.rptFromDate = {opened:false};
		jobVm.rptToDate = {opened:false};
		jobVm.rptFromDatePicker = function(){
			jobVm.rptFromDate.opened = true;
		}
		jobVm.rptToDatePicker = function(){
			jobVm.rptToDate.opened = true;
		}
		
		jobVm.takeoffInfoPopover = {
			templateUrl: 'templates/takeoff-info-jo.html',
			title: 'Takeoff Detail'
		};
		jobVm.quotePopover = {
			templateUrl: 'templates/quote-info-jo.html',
			title: 'Quote Detail'
		};
		jobVm.jobOrderPopover = {
			templateUrl: 'templates/joborder-info-jo.html',
			title: 'Job Order Detail'
		};
		jobVm.orderBookPopover = {
			templateUrl: 'templates/orderbook-info-jo.html',
			title: 'Order Book Detail'
		};
		jobVm.cancelJobOrder = function(id){
			AlertService.showConfirm(	'AWACP :: Confirmation!', "Are you sure to cancel this Job?")
			.then(function (){
				AjaxUtil.getData("/awacp/cancelJobOrder/"+id, Math.random())
				.success(function(data, status, headers){
					if(data && data.result){
						AlertService.showAlert(	'AWACP :: Message!', "Job order Cancelled Successfully")
						.then(function (){jobVm.cancelJobOrderAction();},function (){return false;});
						return;
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "JobOrderCtrl::jobVm.cancelJobOrder::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			},
			function (){
				return false;
			});
		}
		jobVm.uncancellJobOrder = function(id){
			AlertService.showConfirm(	'AWACP :: Confirmation!', "Are you sure to make this job active?")
			.then(function (){
				AjaxUtil.getData("/awacp/uncancellJobOrder/"+id, Math.random())
				.success(function(data, status, headers){
					if(data && data.result){
						AlertService.showAlert(	'AWACP :: Message!', "JobOrder activated Successfully")
						.then(function (){jobVm.cancelJobOrderAction();},function (){return false;});
						return;
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "JobOrderCtrl::jobVm.uncancellJobOrder::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			},
			function (){
				return false;
			});
		}
		jobVm.showQuoteInfo = function(takeoffId){
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
						jobVm.selectedQuote = data.takeoff;				
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "JobOrderCtrl::jobVm.showQuoteInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		jobVm.showTakeoffInfo = function(takeoffId){
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
						jobVm.selectedTakeoff = data.takeoff;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "JobOrderCtrl::jobVm.showTakeoffInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		jobVm.showOrderBookInfo = function(obId){
			AjaxUtil.getData("/awacp/getOrderBook/"+obId, Math.random())
			.success(function(data, status, headers){
				if(data && data.jobOrder){
					$scope.$apply(function(){						
						jobVm.selectedOrderBook = data.jobOrder;				
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "JobOrderCtrl::jobVm.showOrderBookInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
					
		}
		jobVm.showJobOrderInfo = function(jobOrder){
			jobVm.selectedJobOrder = jobOrder;
		}
		
		jobVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					jobVm.updateFileUploadCount(source, sourceId, filePattern);
				}
			});
		}
		jobVm.updateFileUploadCount = function(source, sourceId, docType){
			if(jobVm.jobOrders && jobVm.jobOrders.length > 0){
				for(var i = 0; i < jobVm.jobOrders.length; i++){
					if(jobVm.jobOrders[i].id === sourceId){
						if(source.includes("JO_ONE")){
							jobVm.jobOrders[i].joOneDocCount = (parseInt(jobVm.jobOrders[i].joOneDocCount) + 1);
						}else if(source.includes("JO_TWO")){
							jobVm.jobOrders[i].joTwoDocCount = (parseInt(jobVm.jobOrders[i].joTwoDocCount) + 1);
						}else if(source.includes("JO_THREE")){
							jobVm.jobOrders[i].joThreeDocCount = (parseInt(jobVm.jobOrders[i].joThreeDocCount) + 1);
						}else if(source.includes("JO_FOUR")){
							jobVm.jobOrders[i].joFourDocCount = (parseInt(jobVm.jobOrders[i].joFourDocCount) + 1);
						}else if(source.includes("JO_FIVE")){
							jobVm.jobOrders[i].joFiveDocCount = (parseInt(jobVm.jobOrders[i].joFiveDocCount) + 1);
						}else if(source.includes("JO_SIX")){
							jobVm.jobOrders[i].joSixDocCount = (parseInt(jobVm.jobOrders[i].joSixDocCount) + 1);
						}else if(source.includes("JO_XLS")){
							jobVm.jobOrders[i].joXlsCount = (parseInt(jobVm.jobOrders[i].joXlsCount) + 1);
						}else if(source.includes("JO_DOC")){
							jobVm.jobOrders[i].joDocCount = (parseInt(jobVm.jobOrders[i].joDocCount) + 1);
						}else if(source.includes("JO_TAX")){
							jobVm.jobOrders[i].joTaxDocCount = (parseInt(jobVm.jobOrders[i].joTaxDocCount) + 1);
						}else if(source.includes("JO_PO")){
							jobVm.jobOrders[i].joPoDocCount = (parseInt(jobVm.jobOrders[i].joPoDocCount) + 1);
						}else if(source.includes("JO_IU")){
							jobVm.jobOrders[i].joUiDocCount = (parseInt(jobVm.jobOrders[i].joUiDocCount) + 1);
						}			
						break;
					}
				}
			}
		}
		jobVm.filterJobOrders = function(billType){
			jobVm.listJobOrders(billType);
		}		
		jobVm.action = "Add New";	
		jobVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("JOBORDER", size, function(status, size){
				if("success" === status){
					jobVm.pageSize = size;
					jobVm.pageChanged();
				}
			});
		}
		
		jobVm.getPageSize = function(){
			AjaxUtil.getPageSize("JOBORDER", function(status, size){
				if("success" === status){
					jobVm.pageSize = size;
				}
			});
		}
		jobVm.pageChanged = function() {
			jobVm.listJobOrders('all');
		};		
		
		jobVm.searchQuoteForJobOrder = function(){
			if(!jobVm.jobOrder.quoteId){
				AlertService.showAlert(	'AWACP :: Information!', "Please enter quote ID")
					.then(function (){return},function (){return});
					return;
			}
			AjaxUtil.getData("/awacp/searchQuoteForJobOrder/"+jobVm.jobOrder.quoteId, Math.random())
			.success(function(data, status, headers){
				jobVm.jobOrder = {};
				if(data && data.jobOrder){
					if(data.jobOrder.id){
						AlertService.showAlert(	'AWACP :: Information!', "A Job with #Job Number: "+ data.jobOrder.orderNumber + " already exits for this quote.")
						.then(function (){return false},function (){return false});
						return;
					}else{
						jobVm.jobOrder = data.jobOrder;
						$scope.$digest();
					}
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(777777 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Information!', "Quote detail not found")
					.then(function (){return},function (){return});
					return;
				}
				
				jqXHR.errorSource = "JobOrderCtrl::jobVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		jobVm.searchJobOrderIds = function(){
			return jobVm.jobOrderIds;
		}
		
		jobVm.GcsPopover = {
			templateUrl: 'templates/jobOrder-gc-list.html',
			title: 'General Contractor(s)'
		};
		jobVm.bidderPopover = {
			templateUrl: 'templates/jobOrder-bidder-list.html',
			title: 'Bidder(s)'
		};
		jobVm.listGcsByjobOrder = function(jobOrder){
			if(jobOrder.generalContractors){
				jobVm.jobOrderGcs = jobOrder.generalContractors;
			}else{
				jobVm.jobOrderGcs  = null;
			}
		}
		jobVm.listBiddersByjobOrder = function(jobOrder){
			jobVm.jobOrderBidders = [];
			if(jobOrder.bidders){
				jobVm.jobOrderBidders = jobOrder.bidders;
			}else{
				jobVm.jobOrderBidders = null;
			}
		}
		jobVm.drawingDatePicker = function(){
			jobVm.drawingDate.opened = true;
		}
		jobVm.reversedDatePicker = function(){
			jobVm.revisedDate.opened = true;
		}
		jobVm.dueDatePicker = function(){
			jobVm.dueDate.opened = true;
		}
		jobVm.setNewArc = function(){
			jobVm.jobOrder.architectureName = "";
			jobVm.arc_text = jobVm.arc_text === 'NEW'?'REVERT':'NEW';
		}
		jobVm.setNewCont = function(){
			jobVm.jobOrder.contractorName = "";
			jobVm.cont_text = jobVm.cont_text === 'NEW'?'REVERT':'NEW';			
		}
		jobVm.setNewEng = function(){
			jobVm.jobOrder.engineerName = "";
			jobVm.eng_text = jobVm.eng_text === 'NEW'?'REVERT':'NEW';
		}
		jobVm.setPage = function (pageNo) {
			jobVm.currentPage = pageNo;
		};
		jobVm.pageChanged = function() {
			console.log('Page changed to: ' + jobVm.currentPage);
		};	
		jobVm.cancelJobOrderAction = function(){
			$state.go("joborder-view");
		}
		jobVm.getContractors = function(){
			jobVm.contractors = [];
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
					jobVm.contractors = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "JobOrderCtrl::jobVm.getContractors::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		jobVm.getUsers = function(){
			jobVm.users = [];
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
						jobVm.users = tmp;
					});						
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "JobOrderCtrl::jobVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		jobVm.getEngineers = function(){
			jobVm.engineers = [];
			AjaxUtil.getData("/awacp/listEngineers/1/-1", Math.random())
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
						jobVm.engineers= tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "JobOrderCtrl::jobVm.getEngineers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		jobVm.getArchitects = function(){
			jobVm.architects = [];
			AjaxUtil.getData("/awacp/listArchitects/1/-1", Math.random())
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
						jobVm.architects = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "JobOrderCtrl::jobVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		jobVm.initJobOrderMasterInputs = function(){
			jobVm.getUsers();
			jobVm.getArchitects();
			jobVm.getEngineers();
			jobVm.getContractors();
		}
		jobVm.isValidDateRange =function(fDate, lDate){
			var sDate = new Date(fDate);
			var eDate = new Date(lDate);
			return (eDate >= sDate);
		}
		jobVm.validateReportInputs = function(callback){
			if(jobVm.jobOrder.toDate && !jobVm.isValidDateRange(jobVm.jobOrder.fromDate, jobVm.jobOrder.toDate)){
				callback(false, "From date should be greater than or equal to To date.");
				return;
			}else{
				callback(true, "");
				return;
			}
		}
		jobVm.toggleReportFormVisibility = function(){
			jobVm.showReportForm = !jobVm.showReportForm;
		}
		jobVm.rememberReportQueryParams = function(){
			jQuery("#jobOrder-rpt-btn").attr('disabled','disabled');
			jQuery("#jobOrder-rpt-spinner").css('display','block');
			jobVm.validateReportInputs(
				function(isValid, msg){
					if(isValid == false){
						AlertService.showAlert(
						'AWACP :: Message!',
						msg
						).then(function (){	
								jQuery("#jobOrder-rpt-btn").removeAttr('disabled');
								jQuery("#jobOrder-rpt-spinner").css('display','none');
								return;
							},function (){	return; } );					
					}else{
						jobVm.generateReport();
					}
				}
			);	
		}
		jobVm.totalBillingAmount = 0;
		jobVm.totalCost = 0;
		jobVm.totalProfit = 0;
		jobVm.profitPercent = 0;
		jobVm.generateReport = function(){
			jobVm.jobOrders = [];
			jobVm.report.mode = 'input';
			if(jobVm.jobOrder.fromDate && !jobVm.jobOrder.toDate){
				jobVm.jobOrder.toDate = jobVm.jobOrder.fromDate;
			}
			jobVm.jobOrder.pageNumber = jobVm.currentPage;
			jobVm.jobOrder.pageSize = jobVm.pageSize;
			var formData = {};

			formData["jobOrder"] = jobVm.jobOrder;
			AjaxUtil.submitData("/awacp/generateJobOrderReport", formData)
			.success(function(data, status, headers){
				jobVm.report.mode = 'output';
				jQuery("#jobOrder-rpt-btn").removeAttr('disabled');
				jQuery("#jobOrder-rpt-spinner").css('display','none');
				if(data && data.stsResponse && data.stsResponse.totalCount){
					jobVm.totalItems = 	data.stsResponse.totalCount;
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
							if(v.billingAmount){
								jobVm.totalBillingAmount = jobVm.totalBillingAmount + parseFloat(v.billingAmount);
							}
							if(v.totalCost){
								jobVm.totalCost = jobVm.totalCost + parseFloat(v.totalCost);
							}
						});					
					} else {
						data.stsResponse.results.openInfoBox = false;						
					    tmp.push(data.stsResponse.results);
						if(data.stsResponse.results.billingAmount){
							jobVm.totalBillingAmount = jobVm.totalBillingAmount + parseFloat(data.stsResponse.results.billingAmount);
						}
						if(data.stsResponse.results.totalCost){
							jobVm.totalCost = jobVm.totalCost + parseFloat(data.stsResponse.results.totalCost);
						}
					}
					$scope.$apply(function(){
						jobVm.totalProfit = (parseFloat(jobVm.totalBillingAmount) - parseFloat(jobVm.totalCost));
						if(jobVm.totalCost > 0 && jobVm.totalBillingAmount > 0){
							jobVm.profitPercent = (((parseFloat(jobVm.totalBillingAmount) - parseFloat(jobVm.totalCost)) / parseFloat(jobVm.totalCost)) * 100);
						}else{
							jobVm.profitPercent = jobVm.totalBillingAmount;
						}
						jobVm.jobOrders = tmp;
					});
				}else{
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "JobOrderCtrl::jobVm.generateReport::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		jobVm.editJobOrder = function(){
			if($state.params.id != undefined){
				var formData = {};
				AjaxUtil.getData("/awacp/getJobOrder/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.jobOrder){
						$scope.$apply(function(){
							jobVm.jobOrder = data.jobOrder;	
							if(jobVm.jobOrder.dateEntered){
								jobVm.jobOrder.dateEntered = new Date(jobVm.jobOrder.dateEntered);
							}
							if(jobVm.jobOrder.bidders){
								if(jQuery.isArray(jobVm.jobOrder.bidders)) {
									jobVm.selectedBidders = jobVm.jobOrder.bidders;
								}else{
									jobVm.selectedBidders = [];
									jobVm.selectedBidders.push(jobVm.jobOrder.bidders);
								}								
							}
							if(jobVm.jobOrder.generalContractors){
								if(jQuery.isArray(jobVm.jobOrder.generalContractors)) {
									jobVm.selectedContractors = jobVm.jobOrder.generalContractors;
								}else{
									jobVm.selectedContractors = [];
									jobVm.selectedContractors.push(jobVm.jobOrder.generalContractors);
								}								
							}
							jobVm.action = jobVm.jobOrder && jobVm.jobOrder.id?"Update":"Add New";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "JobOrderCtrl::jobVm.editjobOrder::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		jobVm.saveJobOrder = function(){
			jQuery(".jobOrder-add-action").attr('disabled','disabled');
			jQuery("#jobOrder-add-spinner").css('display','block');	
			var update = false;
			var message = "New Job Order Detail Created Successfully, add more?";
			if(jobVm.jobOrder && jobVm.jobOrder.id){
				message = "Job Order Detail Updated Successfully";
				jobVm.jobOrder.updatedByUserCode = StoreService.getUser().userCode;
				jobVm.jobOrder.createdById = StoreService.getUser().userId;
				jobVm.jobOrder.auditMessage = "Updated Job Order with ID: '"+ jobVm.jobOrder.orderNumber + "'";
				update = true;
			}else{
				jobVm.jobOrder.createdByUserCode = StoreService.getUser().userCode;
				jobVm.jobOrder.createdById = StoreService.getUser().userId;
				jobVm.jobOrder.auditMessage = "Created Job Order with ID '"+ jobVm.jobOrder.orderNumber + "'";
			}
			var formData = {};
			jobVm.jobOrder.userNameOrEmail = StoreService.getUserName();
			formData["jobOrder"] = jobVm.jobOrder;
			AjaxUtil.submitData("/awacp/saveJobOrder", formData)
			.success(function(data, status, headers){
				jQuery(".jobOrder-add-action").removeAttr('disabled');
				jQuery("#jobOrder-add-spinner").css('display','none');
				jobVm.jobOrder = {};
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){jobVm.cancelJobOrderAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){jobVm.cancelJobOrderAction();});
					return;
				}				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".jobOrder-add-action").removeAttr('disabled');
				jQuery("#jobOrder-add-spinner").css('display','none');
				jqXHR.errorSource = "JobOrderCtrl::jobVm.savejobOrder::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		jobVm.listJobOrders = function(invoiceMode){
			jobVm.jobOrders = [];
			if($state.params.oSource != undefined && $state.params.oSource.length > 0){
				AjaxUtil.getData("/awacp/getJobOrder/"+$state.params.oSource, Math.random())
				.success(function(data, status, headers){
					if(data && data.jobOrder){
						$scope.$apply(function(){
							jobVm.jobOrders.push(data.jobOrder);
						});
					}					
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "JobOrderCtrl::jobVm.listJobOrders::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}else{
				AjaxUtil.getData("/awacp/listJobOrders/"+invoiceMode+"/"+jobVm.currentPage+"/"+jobVm.pageSize, Math.random())
				.success(function(data, status, headers){
					if(data && data.stsResponse && data.stsResponse.totalCount){
						jobVm.totalItems = 	data.stsResponse.totalCount;
					}
					if(data && data.stsResponse && data.stsResponse.results){
						var tmp = [];
						if(jQuery.isArray(data.stsResponse.results)) {
							jQuery.each(data.stsResponse.results, function(k, v){
								v.openInfoBox = false;
								tmp.push(v);
							});					
						} else {
							data.stsResponse.results.openInfoBox = false;						
							tmp.push(data.stsResponse.results);
						}
						$scope.$apply(function(){
							jobVm.jobOrders = tmp;
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "JobOrderCtrl::jobVm.listJobOrders::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		jobVm.editJobOrder();
		jobVm.getPageSize();
	}		
})();



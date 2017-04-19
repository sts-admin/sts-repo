  (function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('InvoiceCtrl', InvoiceCtrl);
	InvoiceCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService'];
	function InvoiceCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService){
		var invoiceVm = this;
		invoiceVm.activeIndex = 0;
		invoiceVm.selectedJobId = "";
		invoiceVm.selectedOrderNumber = [];
		invoiceVm.action = "Add";
		
		invoiceVm.openAnother = true;		
		invoiceVm.selectedinvoice = {};
		$scope.timers = [];
		invoiceVm.invoice = {profitSheetItems:[], salesPersonCode:StoreService.getUser().userCode, createdByUserCode:StoreService.getUser().userCode};
		invoiceVm.jobOrder = {};
		invoiceVm.taxes = [];
		invoiceVm.shippedVias = [];
		invoiceVm.shippedItems = [];
		invoiceVm.shipDate = {opened:false};
		invoiceVm.shipmentOptions = [{id:'FULL', title:'FULL'}, {id:'PARTIAL', title:'PARTIAL'}];
		invoiceVm.calculatePayable = function(){
			var partAmt = invoiceVm.invoice.partAmount?parseFloat(invoiceVm.invoice.partAmount):0.0;
			invoiceVm.invoice.balancePayable = parseFloat(invoiceVm.invoice.balancePayable) - parseFloat(partAmt);
		}
		invoiceVm.addCostSheetLineItem = function(){
			invoiceVm.invoice.profitSheetItems.push({awacpPoNumber:"", orbf:"", facInv:"", invAmount:"", freight:"", manual:true});
		}
		invoiceVm.removeCostSheetLineItem = function(index){
			invoiceVm.invoice.profitSheetItems.splice(index, 1);
		}
		invoiceVm.initProfitsheetLineItems = function(){
			if(invoiceVm.invoice.profitSheetItems.length == 0){
				invoiceVm.invoice.profitSheetItems.push({awacpPoNumber:"", orbf:"", facInv:"", invAmount:"", freight:""});
			}
		}
		invoiceVm.shipDatePicker = function(){
			invoiceVm.shipDate.opened = true;
		}
		invoiceVm.setActiveTab = function(index){
			invoiceVm.activeIndex = index;
		}
		invoiceVm.listShippedItems = function(){
			invoiceVm.shippedItems = [];
			AjaxUtil.getData("/awacp/listItemShippeds/-1/-1", Math.random())
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
					invoiceVm.shippedItems = tmp;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "InvoiceCtrl::invoiceVm.listShippedVias::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		invoiceVm.listShippedVias = function(){
			invoiceVm.shippedVias = [];
			AjaxUtil.getData("/awacp/listShippedVias/-1/-1", Math.random())
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
					invoiceVm.shippedVias = tmp;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "InvoiceCtrl::invoiceVm.listShippedVias::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		invoiceVm.listTaxEntries = function(){
			invoiceVm.taxes = [];
			AjaxUtil.getData("/awacp/listTaxEntries/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						var t = data.stsResponse.results;
						t.customName = t.city + " ("+ t.rate + ")";
						tmp.push(t);
					}else{
						jQuery.each(data.stsResponse.results, function(k, v){
							v.customName = v.city + " ("+ v.rate + ")";
							tmp.push(v);
						});
					}	
					invoiceVm.taxes = tmp;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "InvoiceCtrl::invoiceVm.listTaxEntries::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		invoiceVm.getJobOrderForThisInvoice = function(jobOrderId){
			invoiceVm.jobOrder = {};
			AjaxUtil.getData("/awacp/getJobOrder/"+jobOrderId, Math.random())
			.success(function(data, status, headers){
				if(data && data.jobOrder){
					invoiceVm.jobOrder = data.jobOrder;
					if(invoiceVm.jobOrder.jobOrderBookNumbers){
						if(!jQuery.isArray(invoiceVm.jobOrder.jobOrderBookNumbers)){
							var tmp = invoiceVm.jobOrder.jobOrderBookNumbers;
							invoiceVm.jobOrder.jobOrderBookNumbers = [];
							invoiceVm.jobOrder.jobOrderBookNumbers.push(tmp);
						}
					}
					if(!$state.params.invoiceId){ //new invoice
						invoiceVm.invoice.jobOrderId = invoiceVm.jobOrder.id;
						invoiceVm.invoice.salesPersonCode = invoiceVm.jobOrder.createdByUserCode;
					}
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "InvoiceCtrl::invoiceVm.getJobOrderForThisInvoice::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		invoiceVm.showFileListingView = function(source, sourceId, title, size){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size);
		}
		invoiceVm.showInvoiceInfo = function(invoice){
			invoice.openInfoBox = true;
			invoiceVm.selectedinvoice =  invoice;
		}
		invoiceVm.invoIceInfoPopover = {
			templateUrl: 'templates/invoice-info.html',
			title: 'Job Order Detail'
		};
	
		invoiceVm.cancelInvoiceAction = function(){
			$state.go("joborder-view");
		}
		invoiceVm.getUsers = function(){
			invoiceVm.users = [];
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
						invoiceVm.users = tmp;
					});						
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "InvoiceCtrl::invoiceVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		invoiceVm.editInvoice = function(id){
			AjaxUtil.getData("/awacp/getInvoice/"+id, Math.random())
			.success(function(data, status, headers){				
				if(data && data.invoice){
					console.log(JSON.stringify(data.invoice, null, 4));
					$scope.$apply(function(){
						invoiceVm.invoice = data.invoice;	
						invoiceVm.action = "Update";							
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "InvoiceCtrl::invoiceVm.editInvoice::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		invoiceVm.saveInvoice = function(){
			jQuery(".invoice-add-action").attr('disabled','disabled');
			jQuery("#invoice-add-spinner").css('display','block');	
			var update = false;
			var message = "Job Invoice Created Successfully, add more?";
			if(invoiceVm.invoice && invoiceVm.invoice.id){
				message = "Job Invoice Detail Updated Successfully";
				invoiceVm.invoice.updatedByUserCode = StoreService.getUser().userCode;
				invoiceVm.invoice.updatedById = StoreService.getUser().id;
				update = true;
			}else{
				invoiceVm.invoice.createdByUserCode = StoreService.getUser().userCode;
				invoiceVm.invoice.createdById = StoreService.getUser().id;
			}
			var formData = {};
			invoiceVm.invoice.userNameOrEmail = StoreService.getUserName();
			formData["invoice"] = invoiceVm.invoice;
			AjaxUtil.submitData("/awacp/saveInvoice", formData)
			.success(function(data, status, headers){
				jQuery(".invoice-add-action").removeAttr('disabled');
				jQuery("#invoice-add-spinner").css('display','none');
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){invoiceVm.cancelInvoiceAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){invoiceVm.cancelinvoiceAction();});
					return;
				}				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".invoice-add-action").removeAttr('disabled');
				jQuery("#invoice-add-spinner").css('display','none');
				jqXHR.errorSource = "InvoiceCtrl::invoiceVm.saveInvoice::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		if($state.params.jobId != undefined && $state.params.orderNumber != undefined){
			if($state.params.invoiceId){
				invoiceVm.editInvoice($state.params.invoiceId);
			}
			invoiceVm.selectedJobId = $state.params.jobId;
			invoiceVm.selectedOrderNumber = $state.params.orderNumber;
			invoiceVm.getJobOrderForThisInvoice(invoiceVm.selectedJobId);
			invoiceVm.listTaxEntries();
			invoiceVm.listShippedVias();
			invoiceVm.listShippedItems();
		}
		invoiceVm.initProfitsheetLineItems();
	}		
})();



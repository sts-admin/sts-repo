(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TakeoffCtrl', TakeoffCtrl);
	TakeoffCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService'];
	function TakeoffCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService){
		var takeVm = this;
		takeVm.takeoffViewHeading = "View Takeoff";
		takeVm.showAddTakeoffLink = true;
		takeVm.takeoffIds = [{id:"T16-1"}, {id:"T16-2"}, {id:"T16-3"}, {id:"T16-4"}, {id:"T16-5"}];
		takeVm.selectedTakeoffId;
		takeVm.openAnother = true;		
		takeVm.selectedTakeoff = {};
		takeVm.selectedQuote = {};
		takeVm.selectedJobOrder = {};
		takeVm.drawingDate = {opened:false};
		takeVm.revisedDate = {opened:false};
		takeVm.dueDate = {opened:false};		
		takeVm.arc_text = "NEW";
		takeVm.spec_text = "NEW";
		takeVm.eng_text = "NEW";
		$scope.timers = [];
		takeVm.specSettings = {displayProp: 'detail', idProp: 'id'};
		takeVm.totalItems = 0;
		takeVm.currentPage = 1;
		takeVm.pageSize = 5;
		takeVm.users = [];
		takeVm.specs = [];
		takeVm.engineers = [];
		takeVm.architects = [];	
		takeVm.contractors = [];
		takeVm.bidders = [];
		takeVm.takeoffs = [];
		takeVm.takeoff = {};
		takeVm.selectedBidders = [];
		takeVm.selectedContractors = [];
		takeVm.takeoffGcs = [];
		takeVm.takeoffBidders = [];		
		takeVm.action = "Add New";
		takeVm.searchTakeoffIds = function(){
			return takeVm.takeoffIds;
		}
		takeVm.showFileListingView = function(source, sourceId, title, size, filePattern){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern);
		}
		takeVm.showTakeoffInfo = function(takeoff){
			takeVm.selectedTakeoff =  takeoff;
		}
		takeVm.showQuoteInfo = function(takeoff){
			takeVm.selectedQuote = takeoff;
		}
		takeVm.takeoffInfoPopover = {
			templateUrl: 'templates/takeoff-info-to.html',
			title: 'Takeoff Detail'
		};
		takeVm.quoteInfoPopover = {
			templateUrl: 'templates/quote-info-to.html',
			title: 'Quote Detail'
		};
		takeVm.quoteInfoPopover = {
			templateUrl: 'templates/quote-info-to.html',
			title: 'Quote Detail'
		};
		takeVm.jobInfoPopover = {
			templateUrl: 'templates/joborder-info-to.html',
			title: 'Job Order Detail'
		};
		takeVm.GcsPopover = {
			templateUrl: 'templates/takeoff-gc-list.html',
			title: 'General Contractor(s)'
		};
		takeVm.bidderPopover = {
			templateUrl: 'templates/takeoff-bidder-list.html',
			title: 'Bidder(s)'
		};
		takeVm.listGcsByTakeoff = function(takeoff){
			if(takeoff.generalContractors){
				takeVm.takeoffGcs = takeoff.generalContractors;
			}else{
				takeVm.takeoffGcs  = null;
			}
		}
		takeVm.listBiddersByTakeoff = function(takeoff){
			takeVm.takeoffBidders = [];
			if(takeoff.bidders){
				takeVm.takeoffBidders = takeoff.bidders;
			}else{
				takeVm.takeoffBidders = null;
			}
		}
		takeVm.drawingDatePicker = function(){
			takeVm.drawingDate.opened = true;
		}
		takeVm.reversedDatePicker = function(){
			takeVm.revisedDate.opened = true;
		}
		takeVm.dueDatePicker = function(){
			takeVm.dueDate.opened = true;
		}
		takeVm.setNewArc = function(){
			takeVm.takeoff.architectureName = "";
			takeVm.arc_text = takeVm.arc_text === 'NEW'?'REVERT':'NEW';
		}
		takeVm.setNewSpec = function(){
			takeVm.takeoff.specName = "";
			takeVm.spec_text = takeVm.spec_text === 'NEW'?'REVERT':'NEW';			
		}
		takeVm.setNewEng = function(){
			takeVm.takeoff.engineerName = "";
			takeVm.eng_text = takeVm.eng_text === 'NEW'?'REVERT':'NEW';
		}
		takeVm.setPage = function (pageNo) {
			takeVm.currentPage = pageNo;
		};
		takeVm.pageChanged = function() {
			console.log('Page changed to: ' + takeVm.currentPage);
		};	
		takeVm.cancelTakeoffAction = function(){
			$state.go("takeoff-view");
		}
		takeVm.showJobInfo = function(jobId){
			takeVm.specs = [];
			AjaxUtil.getData("/awacp/getJobOrder/"+jobId, Math.random())
			.success(function(data, status, headers){
				if(data && data.jobOrder){
					takeVm.selectedJobOrder =  data.jobOrder;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.showJobInfo::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.getSpecs = function(){
			takeVm.specs = [];
			AjaxUtil.getData("/awacp/listSpecifications/1/-1", Math.random())
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
					takeVm.specs = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getSpecs::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.getUsers = function(){
			takeVm.users = [];
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
						takeVm.users = tmp;
					});						
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		takeVm.getEngineers = function(){
			takeVm.engineers = [];
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
					takeVm.engineers= tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getEngineers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		takeVm.getArchitects = function(){
			takeVm.architects = [];
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
						takeVm.architects = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}		
		takeVm.getContractors = function(){			
			takeVm.contractors = [];
			AjaxUtil.getData("/awacp/listGcs/1/-1", Math.random())
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
					takeVm.contractors = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getContractors::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.getBidders = function(){
			takeVm.bidders = [];
			AjaxUtil.getData("/awacp/listBidders/1/-1", Math.random())
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
						takeVm.bidders = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getBidders::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		takeVm.initTakeoffMasterInputs = function(){
			takeVm.getUsers();
			takeVm.getArchitects();
			takeVm.getEngineers();
			takeVm.getContractors();
			takeVm.getBidders();
			takeVm.getSpecs();
		}
		takeVm.isValidDateRange =function(fDate, lDate){
			var sDate = new Date(fDate);
			var eDate = new Date(lDate);
			return (eDate >= sDate);
		}
		takeVm.editTakeoff = function(){
			if($state.params.id != undefined){
				AjaxUtil.getData("/awacp/getTakeoff/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.takeoff){
						$scope.$apply(function(){
							takeVm.takeoff = data.takeoff;	
							if(takeVm.takeoff.drawingDate){
								takeVm.takeoff.drawingDate = new Date(takeVm.takeoff.drawingDate);
							}
							if(takeVm.takeoff.dueDate){
								takeVm.takeoff.dueDate = new Date(takeVm.takeoff.dueDate);
							}
							if(takeVm.takeoff.revisedDate){
								takeVm.takeoff.revisedDate = new Date(takeVm.takeoff.revisedDate);
							}
							if(takeVm.takeoff.bidders){
								if(jQuery.isArray(takeVm.takeoff.bidders)) {
									takeVm.selectedBidders = takeVm.takeoff.bidders;
								}else{
									takeVm.selectedBidders = [];
									takeVm.selectedBidders.push(takeVm.takeoff.bidders);
								}								
							}
							if(takeVm.takeoff.generalContractors){
								if(jQuery.isArray(takeVm.takeoff.generalContractors)) {
									takeVm.selectedContractors = takeVm.takeoff.generalContractors;
								}else{
									takeVm.selectedContractors = [];
									takeVm.selectedContractors.push(takeVm.takeoff.generalContractors);
								}								
							}
							takeVm.action = takeVm.takeoff && takeVm.takeoff.id?"Update":"Add New";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "TakeoffCtrl::takeVm.editTakeoff::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		takeVm.saveTakeoff = function(){
			jQuery(".takeoff-add-action").attr('disabled','disabled');
			jQuery("#takeoff-add-spinner").css('display','block');	
			var update = false;
			if(takeVm.takeoff.revisedDate){
				if(!takeVm.isValidDateRange(takeVm.takeoff.drawingDate, takeVm.takeoff.revisedDate)){
					jQuery(".takeoff-add-action").removeAttr('disabled');
					jQuery("#takeoff-add-spinner").css('display','none');
					 AlertService.showAlert(
					'AWACP :: Message!',
					"Drawing date should be greater than or equal to revised date."
					).then(function (){	
						return;
					},
					function (){		
						return;
					});
					return;
				}
				if(!takeVm.isValidDateRange(takeVm.takeoff.revisedDate, takeVm.takeoff.dueDate)){
					jQuery(".takeoff-add-action").removeAttr('disabled');
					jQuery("#takeoff-add-spinner").css('display','none');
					 AlertService.showAlert(
					'AWACP :: Message!',
					"Due date should be greater than or equal to revised date."
					).then(function (){	
						return;
					},
					function (){		
						return;
					});
					return;
				}
			}
			if(takeVm.takeoff.dueDate || takeVm.takeoff.drawingDate){
				if(!takeVm.isValidDateRange(takeVm.takeoff.drawingDate, takeVm.takeoff.dueDate)){
					jQuery(".takeoff-add-action").removeAttr('disabled');
					jQuery("#takeoff-add-spinner").css('display','none');
					 AlertService.showAlert(
					'AWACP :: Message!',
					"Due date should be greater than or equal to drawing date."
					).then(function (){	
						return;
					},
					function (){		
						return;
					});
					return;
				}
			}
			
			if(takeVm.selectedBidders.length > 0){
				var ids = [];
				jQuery.each(takeVm.selectedBidders, function(k, v){
					ids.push(v.id);
				});
				if(ids.length > 0){
					takeVm.takeoff.biddersIds = ids;
				}
			}
			if(takeVm.selectedContractors.length > 0){
				var ids = [];
				jQuery.each(takeVm.selectedContractors, function(k, v){
					ids.push(v.id);
				});
				if(ids.length > 0){
					takeVm.takeoff.contractorsIds = ids;
				}
			}
			var message = "New Takeoff Detail Created Successfully, add more?";
			if(takeVm.takeoff && takeVm.takeoff.id){
				message = "Takeoff Detail Updated Successfully";
				takeVm.takeoff.updatedByUserCode = StoreService.getUser().userCode;
				update = true;
			}else{
				takeVm.takeoff.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};
			takeVm.takeoff.userNameOrEmail = StoreService.getUserName();
			formData["takeoff"] = takeVm.takeoff;
			AjaxUtil.submitData("/awacp/saveTakeoff", formData)
			.success(function(data, status, headers){
				jQuery(".takeoff-add-action").removeAttr('disabled');
				jQuery("#takeoff-add-spinner").css('display','none');
				takeVm.takeoff = {};
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){takeVm.cancelTakeoffAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){takeVm.cancelTakeoffAction();});
					return;
				}				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".takeoff-add-action").removeAttr('disabled');
				jQuery("#takeoff-add-spinner").css('display','none');
				jqXHR.errorSource = "TakeoffCtrl::takeVm.saveTakeoff::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.listTakeoffs = function(){
			takeVm.takeoffs = [];
			if($state.current.name === 'takeoff-report'){
				var reportParams = StoreService.get("rtpQueryOptions-takeoff");
				if(reportParams != null && reportParams != undefined){
					takeVm.generateReport();
				}
			}else{
				AjaxUtil.getData("/awacp/listTakeoffs/"+takeVm.currentPage+"/"+takeVm.pageSize, Math.random())
				.success(function(data, status, headers){
					if(data && data.stsResponse && data.stsResponse.totalCount){
						takeVm.totalItems = data.stsResponse.totalCount;
					}
					if(data && data.stsResponse && data.stsResponse.results){
						var tmp = [];
						if(jQuery.isArray(data.stsResponse.results)) {
							jQuery.each(data.stsResponse.results, function(k, v){
								v.openInfoBox = false;
								if(v.hasOwnProperty('bidders') && !jQuery.isArray(v.bidders)){
									var b = [];
									b.push(v.bidders);
									v["bidders"] = b;
								}
								if(v.hasOwnProperty('generalContractors') && !jQuery.isArray(v.generalContractors)){
									var gc = [];
									gc.push(v.generalContractors);
									v["generalContractors"] = gc;
								}
								tmp.push(v);
							});					
						} else {
							data.stsResponse.results.openInfoBox = false;
							if(data.stsResponse.results.hasOwnProperty('bidders') && !jQuery.isArray(data.stsResponse.results.bidders)){
								var b = [];
								b.push(data.stsResponse.results.bidders);
								data.stsResponse.results["bidders"] = b;
							}
							if(data.stsResponse.results.hasOwnProperty('generalContractors') && !jQuery.isArray(data.stsResponse.results.generalContractors)){
								var gc = [];
								gc.push(data.stsResponse.results.generalContractors);
								data.stsResponse.results["generalContractors"] = gc;
							}
							tmp.push(data.stsResponse.results);
						}
						$scope.$apply(function(){
							takeVm.takeoffs = tmp;
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "TakeoffCtrl::takeVm.listTakeoffs::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}
		}
		takeVm.rptYearRange = [{id:2015, val:"2015"}, {id:2016, val:"2016"}, {id:2017, val:"2017"}];
		takeVm.rptFromDate = {opened:false};
		takeVm.rptToDate = {opened:false};
		takeVm.rptDueDateFrom ={opened:false};
		takeVm.rptDueDateTo ={opened:false};
		takeVm.rptFromDatePicker = function(){
			takeVm.rptFromDate.opened = true;
		}
		takeVm.rptToDatePicker = function(){
			takeVm.rptToDate.opened = true;
		}
		takeVm.rptDueDateFromPicker = function(){
			takeVm.rptDueDateFrom.opened = true;
		}
		takeVm.rptDueDateToPicker = function(){
			takeVm.rptDueDateTo.opened = true;
		}
		takeVm.initTakeoffReportInputs = function(){
			takeVm.takeoff = {};
			takeVm.getUsers();
			takeVm.getArchitects();
			takeVm.getEngineers();
			takeVm.getSpecs();
		}
		takeVm.rememberReportQueryParams = function(){
			takeVm.validateReportInputs(
				function(isValid, msg){
					if(isValid == false){
						AlertService.showAlert(
						'AWACP :: Message!',
						msg
						).then(function (){	return;},function (){	return; } );						
					}else{
						StoreService.remove("rtpQueryOptions-takeoff");
						StoreService.set("rtpQueryOptions-takeoff", takeVm.takeoff);
						$state.go("takeoff-report");
					}
				}
			);	
		}
		takeVm.generateReport = function(){
			takeVm.showAddTakeoffLink = false;
			takeVm.takeoffViewHeading = "Takeoff Report";
			takeVm.takeoff = StoreService.get("rtpQueryOptions-takeoff");
			if(takeVm.takeoff.fromDate && !takeVm.takeoff.toDate){
				takeVm.takeoff.toDate = takeVm.takeoff.fromDate;
			}
			if(takeVm.takeoff.fromDueDate && !takeVm.takeoff.toDueDate){
				takeVm.takeoff.toDueDate = takeVm.takeoff.fromDueDate;
			}
			takeVm.takeoff.pageNumber = takeVm.currentPage;
			takeVm.takeoff.pageSize = takeVm.pageSize;
			var formData = {};
			formData["takeoff"] = takeVm.takeoff;
			AjaxUtil.submitData("/awacp/generateTakeoffReport", formData)
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
						takeVm.totalItems = data.stsResponse.totalCount;
					}
					if(data && data.stsResponse && data.stsResponse.results){
						var tmp = [];
						if(jQuery.isArray(data.stsResponse.results)) {
							jQuery.each(data.stsResponse.results, function(k, v){
								v.openInfoBox = false;
								if(v.hasOwnProperty('bidders') && !jQuery.isArray(v.bidders)){
									var b = [];
									b.push(v.bidders);
									v["bidders"] = b;
								}
								if(v.hasOwnProperty('generalContractors') && !jQuery.isArray(v.generalContractors)){
									var gc = [];
									gc.push(v.generalContractors);
									v["generalContractors"] = gc;
								}
								tmp.push(v);
							});					
						} else {
							data.stsResponse.results.openInfoBox = false;
							if(data.stsResponse.results.hasOwnProperty('bidders') && !jQuery.isArray(data.stsResponse.results.bidders)){
								var b = [];
								b.push(data.stsResponse.results.bidders);
								data.stsResponse.results["bidders"] = b;
							}
							if(data.stsResponse.results.hasOwnProperty('generalContractors') && !jQuery.isArray(data.stsResponse.results.generalContractors)){
								var gc = [];
								gc.push(data.stsResponse.results.generalContractors);
								data.stsResponse.results["generalContractors"] = gc;
							}
							tmp.push(data.stsResponse.results);
						}
						$scope.$apply(function(){
							takeVm.takeoffs = tmp;
						});
					}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.generateReport::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.validateReportInputs = function(callback){
			if(takeVm.takeoff.toDate && !takeVm.isValidDateRange(takeVm.takeoff.fromDate, takeVm.takeoff.toDate)){
				callback(false, "From date should be greater than or equal to To date.");
				return;
			}else if(takeVm.takeoff.toDueDate && !takeVm.isValidDateRange(takeVm.takeoff.fromDueDate, takeVm.takeoff.toDueDate)){
				callback(false, "From due date should be greater than or equal to To due date.");
				return;
			}else{
				callback(true, "");
				return;
			}
		}
		
		if($state.params.id != undefined){
			StoreService.remove("rtpQueryOptions-takeoff");
			takeVm.editTakeoff();
		}
		
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



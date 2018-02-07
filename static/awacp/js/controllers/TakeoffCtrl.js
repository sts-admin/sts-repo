(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TakeoffCtrl', TakeoffCtrl);
	TakeoffCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal','StoreService', '$sce'];
	function TakeoffCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal, StoreService, $sce){
		var takeVm = this;		
		takeVm.recordType = "all";
		takeVm.totalCountOfTheYear = 0;
		takeVm.resetSearch = false;
		takeVm.currentDate = $rootScope.currentDate;
		takeVm.currentYear = $rootScope.currentDate.getFullYear();
		takeVm.report = {mode:'input'};
		takeVm.showReportForm = true;
		takeVm.editQuote = false;
		takeVm.takeoffViewHeading = "View Takeoff";
		takeVm.showAddTakeoffLink = true;
		takeVm.takeoffIds = [];
		takeVm.quoteRevisions = [{id:"A", name:"A"}, {id:"B", name:"B"}, {id:"C", name:"C"}, {id:"D", name:"D"}, {id:"E", name:"E"}, {id:"F", name:"F"}, {id:"G", name:"G"}, {id:"H", name:"H"}, {id:"I", name:"I"}, {id:"J", name:"J"}, {id:"K", name:"K"}, {id:"L", name:"L"}, {id:"M", name:"M"}, {id:"N", name:"N"}, {id:"O", name:"O"}, {id:"P", name:"P"}, {id:"Q", name:"Q"}, {id:"R", name:"R"}, {id:"S", name:"S"}, {id:"T", name:"T"}, {id:"U", name:"U"}, {id:"V", name:"V"}, {id:"W", name:"W"}, {id:"X", name:"X"}, {id:"Y", name:"Y"}, {id:"Z", name:"Z"}];
		
		takeVm.openAnother = true;		
		takeVm.selectedTakeoff = {};
		takeVm.selectedQuote = {};
		takeVm.selectedJobOrder = {};
		takeVm.dateCreated = {opened:false};
		takeVm.drawingDate = {opened:false};
		takeVm.revisedDate = {opened:false};
		takeVm.dueDate = {opened:false};		
		takeVm.arc_text = "NEW";
		takeVm.spec_text = "NEW";
		takeVm.eng_text = "NEW";
		$scope.timers = [];
		takeVm.specSettings = {displayProp: 'detail', idProp: 'id'};
		
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
		takeVm.action = "Add New Takeoff";
		
		takeVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		takeVm.totalItems = -1;
		takeVm.currentPage = 1;
		takeVm.pageNumber = 1;
		takeVm.pageSize = 20;
		
		takeVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("TAKEOFF", size, function(status, size){
				if("success" === status){
					takeVm.pageSize = size;
					takeVm.pageChanged();
				}
			});
		}
		
		takeVm.getPageSize = function(){
			AjaxUtil.getPageSize("TAKEOFF", function(status, size){
				if("success" === status){
					takeVm.pageSize = size;
				}
			});
		}
		takeVm.pageChanged = function() {
			takeVm.listTakeoffs();
		};
		takeVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					takeVm.updateFileUploadCount(source, sourceId, filePattern);
				}
			});
		}
		takeVm.updateFileUploadCount = function(source, sourceId, docType){
			if(takeVm.takeoffs && takeVm.takeoffs.length > 0){
				for(var i = 0; i < takeVm.takeoffs.length; i++){
					if(takeVm.takeoffs[i].id === sourceId){
						if(source.includes("takeoff_drawing_doc")){
							takeVm.takeoffs[i].drawingDocCount = (parseInt(takeVm.takeoffs[i].drawingDocCount) + 1);
						}else if(source.includes("takeoff_doc")){
							takeVm.takeoffs[i].takeoffDocCount = (parseInt(takeVm.takeoffs[i].takeoffDocCount) + 1);
						}else if(source.includes("takeoff_vibro_doc")){
							takeVm.takeoffs[i].vibroDocCount = (parseInt(takeVm.takeoffs[i].vibroDocCount) + 1);
						}			
						break;
					}
				}
			}
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
		takeVm.createdDatePicker = function(){
			takeVm.dateCreated.opened = true;
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
		takeVm.editTakeoff = function(id){
			AjaxUtil.getData("/awacp/getTakeoff/"+id, Math.random())
			.success(function(data, status, headers){
				if(data && data.takeoff){
					$scope.$apply(function(){
						takeVm.takeoff = data.takeoff;	
						if(takeVm.takeoff.dateCreated){
							takeVm.currentDate = new Date(takeVm.takeoff.dateCreated);
						}
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
						if(takeVm.editQuote){
							takeVm.action = "Update Quote";
						}else{
							takeVm.action = takeVm.takeoff && takeVm.takeoff.id?"Update Quote":"Add New Quote";
						}
													
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.editTakeoff::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
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
				if(takeVm.editQuote){
					message = "Quote Detail Updated Successfully";
				}
				takeVm.takeoff.updatedById = StoreService.getUser().userId;
				takeVm.takeoff.updatedByUserCode = StoreService.getUser().userCode;
				takeVm.takeoff.auditMessage = "Updated Takeoff with ID:'"+takeVm.takeoff.takeoffId+"'";
				update = true;
			}else{
				takeVm.takeoff.createdById = StoreService.getUser().userId;
				takeVm.takeoff.createdByUserCode = StoreService.getUser().userCode;
				takeVm.takeoff.auditMessage = "Created Takeoff"; 
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
		takeVm.countOfTheYear = function(){
			AjaxUtil.getData("/awacp/totalRecordsForTheYear/"+takeVm.recordType, Math.random())
			.success(function(data, status, headers){
				if(data && data.totalCount){
					takeVm.totalCountOfTheYear = data.totalCount;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.totalCountOfTheYear::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.listTakeoffs = function(){
			takeVm.countOfTheYear();
			takeVm.takeoffs = [];
			AjaxUtil.getData("/awacp/listTakeoffs/"+takeVm.currentPage+"/"+takeVm.pageSize+"/"+takeVm.recordType, Math.random())
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
		takeVm.getTakeoffsNotQuoted = function(){
			takeVm.recordType = "not_quoted";
			takeVm.listTakeoffs();
		}
		takeVm.getTakeoffsQuoted = function(){
			takeVm.recordType = "quoted";
			takeVm.listTakeoffs();
		}
		takeVm.getAllTakeoffs = function(){
			takeVm.recordType = "all";
			takeVm.listTakeoffs();
		}
		takeVm.rptYearRange = [{id:2015, val:"2015"}, {id:2016, val:"2016"}, {id:2017, val:"2017"}, , {id:2018, val:"2018"}, , {id:2019, val:"2019"}, , {id:2020, val:"2020"}];
		takeVm.rptFromDate = {opened:false};
		takeVm.rptDrawingDate = {opened:false};
		takeVm.rptReviseDate = {opened:false};
		takeVm.rptToDate = {opened:false};
		takeVm.rptDueDateFrom ={opened:false};
		takeVm.rptDueDateTo ={opened:false};
		takeVm.rptDrawingDatePicker = function(){
			takeVm.rptDrawingDate.opened = true;
		}
		takeVm.rptReviseDatePicker = function(){
			takeVm.rptReviseDate.opened = true;
		}
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
			takeVm.getBidders();
			takeVm.getSpecs();
		}
		takeVm.toggleReportFormVisibility = function(){
			takeVm.showReportForm = !takeVm.showReportForm;
		}
		takeVm.rememberReportQueryParams = function(){			
			jQuery("#takeoff-rpt-btn").attr('disabled','disabled');
			jQuery("#takeoff-rpt-spinner").css('display','block');	
			takeVm.validateReportInputs(
				function(isValid, msg){
					if(isValid == false){
						AlertService.showAlert(
						'AWACP :: Message!',
						msg
						).then(function ()
							{	
								jQuery("#takeoff-rpt-btn").removeAttr('disabled');
								jQuery("#takeoff-rpt-spinner").css('display','none');
								return;
							},function (){	return; } );						
					}else{
						takeVm.generateReport();
					}
				}
			);	
		}
		takeVm.generateReport = function(){		
			takeVm.report.mode = 'input';
			takeVm.takeoffs = [];
			if(takeVm.takeoff.fromDate && !takeVm.takeoff.toDate){
				takeVm.takeoff.fromDate = new Date(takeVm.takeoff.fromDate);
				takeVm.takeoff.toDate = takeVm.takeoff.fromDate;
			}
			if(takeVm.takeoff.fromDueDate && !takeVm.takeoff.toDueDate){
				takeVm.takeoff.toDueDate = takeVm.takeoff.fromDueDate;
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
			takeVm.takeoff.pageNumber = takeVm.currentPage;
			takeVm.takeoff.pageSize = takeVm.pageSize;
			var formData = {};
			formData["takeoff"] = takeVm.takeoff;
			AjaxUtil.submitData("/awacp/generateTakeoffReport", formData)
			.success(function(data, status, headers){
				takeVm.report.mode = 'output';
				jQuery("#takeoff-rpt-btn").removeAttr('disabled');
				jQuery("#takeoff-rpt-spinner").css('display','none');
				
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
					}else{
						$scope.$digest();
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
		
		if($state.params.id != undefined || $state.params.quoteEditId){
			if( $state.params.quoteEditId != undefined){
				takeVm.editQuote = true;
				takeVm.editTakeoff($state.params.quoteEditId);
			}else{
				takeVm.editTakeoff($state.params.id);
			}
		}
		if($state.current.name === 'takeoff-reports'){
			takeVm.takeoff = {};
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		takeVm.getPageSize();
		
		
		takeVm.sTakeoff = {};
        takeVm.loading = false;
		takeVm.searchFieldName = "undefined";
		takeVm.autoCompleteOptions = {			
            minimumChars: 3,
            dropdownWidth: '200px',
            dropdownHeight: '200px',
			loading:function(fieldName){takeVm.searchFieldName = fieldName;},
			data: function (searchTerm) {
				var accessToken = StoreService.getAccessToken();
				var url = $rootScope.base + "/awacp/autoCompleteTakeoffList?keyword="+searchTerm+"&field="+takeVm.searchFieldName+"&"+Math.random();
                return $http.get(url, {headers : { 'Authorization' : 'Bearer ' + accessToken, 'Accept' : 'application/json' }})
                    .then(function (response) {
                        takeVm.loading = true;
						if(response.data && response.data.autoComplete){
							if(jQuery.isArray(response.data.autoComplete.result)){
								var res = [];
								jQuery.each(response.data.autoComplete.result, function(index, record){
									res.push(JSON.parse(record));
								});
								return res;
							}else{
								return [JSON.parse(response.data.autoComplete.result)];
							}														
						}                        
						takeVm.loading = false;
                    });
            },
			renderItem: function(item){
				return {'value':item.label, 'label':item.label};
			},
			itemSelected:function(selItem){	
				takeVm.setModelData(selItem);	
			}
        };
		takeVm.setModelData = function(data){
			takeVm.takeoff[data.item.field] = data.item.value;
			takeVm.search();			
		}
		takeVm.search = function(){
			takeVm.takeoffs = [];
			if(takeVm.sTakeoff.dateCreated){
				takeVm.takeoff.dateCreated = new Date(takeVm.sTakeoff.dateCreated);
			}
			if(takeVm.sTakeoff.dueDate){
				takeVm.takeoff.dueDate = new Date(takeVm.sTakeoff.dueDate);
			}
			takeVm.takeoff.pageNumber = takeVm.currentPage;
			takeVm.takeoff.pageSize = takeVm.pageSize;
			var formData = {};
			takeVm.takeoff.view = 'takeoff';
			formData["takeoff"] = takeVm.takeoff;
			//return;
			AjaxUtil.submitData("/awacp/searchTakeoffs", formData)
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
				}else{
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.generateReport::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.clearSearch = function(){
			takeVm.sTakeoff = {};
			takeVm.takeoff = {};
			takeVm.recordType = "all";
			takeVm.listTakeoffs();
		}
		takeVm.triggerSearch = function(){
			takeVm.search();
		}
	}		
})();



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('QuoteCtrl', QuoteCtrl);
	QuoteCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService', 'FileService'];
	function QuoteCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService, FileService){
		var qVm = this;
		qVm.sQuote = {};
		qVm.loading = false;
		qVm.showReportForm = true;
		qVm.selectedBidders = [];
		qVm.totalQuoteAmount = 0;
		qVm.report = {mode:'input'};
		qVm.quoteViewHeading = "View Quote";
		qVm.reportMode = false;
		qVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		qVm.quoteRevisions = [{id:"A", name:"A"}, {id:"B", name:"B"}, {id:"C", name:"C"}, {id:"D", name:"D"}, {id:"E", name:"E"}, {id:"F", name:"F"}, {id:"G", name:"G"}, {id:"H", name:"H"}, {id:"I", name:"I"}, {id:"J", name:"J"}, {id:"K", name:"K"}, {id:"L", name:"L"}, {id:"M", name:"M"}, {id:"N", name:"N"}, {id:"O", name:"O"}, {id:"P", name:"P"}, {id:"Q", name:"Q"}, {id:"R", name:"R"}, {id:"S", name:"S"}, {id:"T", name:"T"}, {id:"U", name:"U"}, {id:"V", name:"V"}, {id:"W", name:"W"}, {id:"X", name:"X"}, {id:"Y", name:"Y"}, {id:"Z", name:"Z"}];
		qVm.action = "Add";
		$scope.timers = [];
		qVm.totalItems = -1;
		qVm.currentPage = 1;
		qVm.pageNumber = 1;
		qVm.pageSize = 20;
		qVm.newQuotes = [];
		qVm.quotes = [];
		qVm.bidders = [];
		
		qVm.selectedNewQuote = {};
		qVm.selectedTakeoff = {};
		qVm.takeoffGcs  = [];
		qVm.takeoffBidders = [];
		qVm.selectedQuote = {};
		qVm.takeoff = {};
		qVm.architects = [];
		qVm.specs = [];
		qVm.engineers = [];
		qVm.rptYearRange = [{id:2015, val:"2015"}, {id:2016, val:"2016"}, {id:2017, val:"2017"}];
		qVm.dateCreated = {opened:false};
		qVm.rptFromDate = {opened:false};
		qVm.rptToDate = {opened:false};
		qVm.rptDueDateFrom ={opened:false};
		qVm.rptDueDateTo ={opened:false};
		qVm.createdDatePicker = function(){
			qVm.dateCreated.opened = true;
		}
		qVm.rptFromDatePicker = function(){
			qVm.rptFromDate.opened = true;
		}
		qVm.rptToDatePicker = function(){
			qVm.rptToDate.opened = true;
		}
		qVm.rptDueDateFromPicker = function(){
			qVm.rptDueDateFrom.opened = true;
		}
		qVm.rptDueDateToPicker = function(){
			qVm.rptDueDateTo.opened = true;
		}
		qVm.worksheetPdfView = function(quote){
			$window.open(quote.pdfFilePath);
		}
		
		qVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					qVm.updateFileUploadCount(sourceId, filePattern);
				}
			});
		}
		qVm.updateFileUploadCount = function(sourceId, docType){
			if(qVm.quotes && qVm.quotes.length > 0){
				for(var i = 0; i < qVm.quotes.length; i++){
					if(qVm.quotes[i].id === sourceId){
						if(docType.includes(".pdf")){
							qVm.quotes[i].quotePdfDocCount = (parseInt(qVm.quotes[i].quotePdfDocCount) + 1);
						}else if(docType.includes(".xls")){
							qVm.quotes[i].quoteXlsDocCount = (parseInt(qVm.quotes[i].quoteXlsDocCount) + 1);
						}else if(docType.includes(".doc")){
							qVm.quotes[i].quoteDocCount = (parseInt(qVm.quotes[i].quoteDocCount) + 1);
						}						
						break;
					}
				}
			}
		}
		
		qVm.setCurrentPageSize =function(size, src){
			AjaxUtil.setPageSize('new' === src? 'NEW-QUOTES':'QUOTES', size, function(status, size){
				if("success" === status){
					qVm.pageSize = size;
					qVm.pageChanged(src);
				}
			});
		}
		
		qVm.getPageSize = function(src){
			AjaxUtil.getPageSize('new' === src ? 'NEW-QUOTES':'QUOTES', function(status, size){
				if("success" === status){
					qVm.pageSize = size;
					$scope.$digest();
				}
			});
		}
		qVm.pageChanged = function(src) {
			if('new' === src){
				qVm.listNewTakeoffsForQuote();
			}else if('quotes' === src){
				qVm.listQuotes();
			}			
		};		
		qVm.cancelQuoteAction = function(){
			$state.go("quotes");
		}
		qVm.showNewQuoteInfo = function(quote){
			qVm.openInfoBox = true;
			qVm.selectedNewQuote =  quote;
		}
		qVm.newQuotePopover = {
			templateUrl: 'templates/new-quote-info.html',
			title: 'New Quote Detail'
		};
		qVm.showQuoteInfo = function(quote){
			qVm.openInfoBox = true;
			qVm.selectedQuote =  quote;
		}
		qVm.quotePopover = {
			templateUrl: 'templates/quote-info.html',
			title: 'Quote Detail'
		};
		qVm.showTakeoffInfo = function(takeoff){
			qVm.openInfoBox = true;
			qVm.selectedTakeoff =  takeoff;
		}
		qVm.takeoffPopover = {
			templateUrl: 'templates/quote-takeoff-info.html',
			title: 'Takeoff Detail'
		};
		
		qVm.GcsPopover = {
			templateUrl: 'templates/quote-gc-list.html',
			title: 'General Contractor(s)'
		};
		qVm.listGcsByTakeoff = function(takeoff){
			if(takeoff.generalContractors){
				qVm.takeoffGcs = takeoff.generalContractors;
			}else{
				qVm.takeoffGcs  = null;
			}
		}
		qVm.bidderPopover = {
			templateUrl: 'templates/quote-bidder-list.html',
			title: 'Bidder(s)'
		};
		qVm.listBiddersByTakeoff = function(takeoff){
			qVm.takeoffBidders = [];
			if(takeoff.bidders){
				qVm.takeoffBidders = takeoff.bidders;
			}else{
				qVm.takeoffBidders = null;
			}
		}
		
		qVm.makeQuote = function(id){
			AlertService.showConfirm(	'AWACP :: Confirmation!', "Are you sure to make quote?")
			.then(function (){
				AjaxUtil.getData("/awacp/makeQuote/"+id, Math.random())
				.success(function(data, status, headers){
					if(data && data.result){
						var msg = "Unable to make quote.";
						if("quote_create_success" === data.result){
							msg = "Quote created successfully.";
						}else if("quote_already_created" === data.result){
							msg = "Quote already created.";
						}
						AlertService.showAlert(	'AWACP :: Message!', msg)
						.then(function (){$state.go("quote-view"); return;},function (){return});
						return;
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "QuoteCtrl::qVm.makeQuote::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			},
			function (){
				return false;
			});
			
		}
		qVm.listQuotes = function(){
			qVm.quotes = [];
			qVm.pageNumber = qVm.currentPage;
			if($state.params.qSource != undefined && $state.params.qSource.length > 0){
				AjaxUtil.getData("/awacp/getTakeoff/"+$state.params.qSource, Math.random())
				.success(function(data, status, headers){
					if(data && data.takeoff){
						$scope.$apply(function(){
							qVm.quotes.push(data.takeoff);
						});
					}					
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "QuoteCtrl::qVm.listNewTakeoffsForQuote::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}else{
				AjaxUtil.getData("/awacp/listTakeoffsForView/"+qVm.pageNumber+"/"+qVm.pageSize+"/true", Math.random())
				.success(function(data, status, headers){
					if(data && data.stsResponse && data.stsResponse.totalCount){
						$scope.$apply(function(){
							qVm.totalItems = data.stsResponse.totalCount;
						});
					}
					if(data && data.stsResponse && data.stsResponse.results){
						var tmp = [];
						if(jQuery.isArray(data.stsResponse.results)) {
							jQuery.each(data.stsResponse.results, function(k, v){
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
							qVm.quotes  = tmp;
						});
					}		
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "QuoteCtrl::qVm.listNewTakeoffsForQuote::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
			}
			
		}
		qVm.listNewTakeoffsForQuote = function(){
			qVm.newQuotes = [];
			qVm.pageNumber = qVm.currentPage;
			AjaxUtil.getData("/awacp/listNewTakeoffsForQuote/"+qVm.pageNumber+"/"+qVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						qVm.totalItems = data.stsResponse.totalCount;
					});
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
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
						qVm.newQuotes = tmp;
					});
				}		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.listNewTakeoffsForQuote::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		qVm.getBidders = function(){
			qVm.bidders = [];
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
					qVm.bidders = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.getBidders::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		qVm.getUsers = function(){
			qVm.users = [];
			AjaxUtil.getData("/awacp/listUser/-1/-1", Math.random())
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
						qVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		qVm.toggleReportFormVisibility = function(){
			qVm.showReportForm = !qVm.showReportForm;
		}
		qVm.rememberReportQueryParams = function(){
			jQuery("#quote-rpt-btn").attr('disabled','disabled');
			jQuery("#quote-rpt-spinner").css('display','block');	
			qVm.validateReportInputs(
				function(isValid, msg){
					if(isValid == false){
						AlertService.showAlert(
						'AWACP :: Message!',
						msg
						).then(function (){	
								jQuery("#quote-rpt-btn").removeAttr('disabled');
								jQuery("#quote-rpt-spinner").css('display','none');
								return;
							},function (){	return; } );							
					}else{
						qVm.generateReport();
					}
				}
			);	
		}
		qVm.getEngineers = function(){
			qVm.engineers = [];
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
					qVm.engineers= tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.getEngineers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		qVm.getSpecs = function(){
			qVm.specs = [];
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
					qVm.specs = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.getSpecs::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
	
		qVm.getArchitects = function(){
			qVm.architects = [];
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
						qVm.architects = tmp;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}	
		qVm.initQuoteReportInputs = function(){
			qVm.takeoff = {};
			qVm.getUsers();
			qVm.getArchitects();
			qVm.getEngineers();
			qVm.getSpecs();
			qVm.getBidders();
		}
		qVm.isValidDateRange =function(fDate, lDate){
			var sDate = new Date(fDate);
			var eDate = new Date(lDate);
			return (eDate >= sDate);
		}
		qVm.validateReportInputs = function(callback){
			if(qVm.takeoff.toDate && !qVm.isValidDateRange(qVm.takeoff.fromDate, qVm.takeoff.toDate)){
				callback(false, "From date should be greater than or equal to To date.");
				return;
			}else if(qVm.takeoff.toDueDate && !qVm.isValidDateRange(qVm.takeoff.fromDueDate, qVm.takeoff.toDueDate)){
				callback(false, "From due date should be greater than or equal to To due date.");
				return;
			}else{
				callback(true, "");
				return;
			}
		}
		qVm.generateReport = function(){
			qVm.quotes = [];
			qVm.totalQuoteAmount = 0;
			qVm.report.mode = 'input';
			if(qVm.takeoff.fromDate && !qVm.takeoff.toDate){
				qVm.takeoff.toDate = qVm.takeoff.fromDate;
			}
			if(qVm.takeoff.fromDueDate && !qVm.takeoff.toDueDate){
				qVm.takeoff.toDueDate = qVm.takeoff.fromDueDate;
			}
			if(qVm.selectedBidders.length > 0){
				var ids = [];
				jQuery.each(qVm.selectedBidders, function(k, v){
					ids.push(v.id);
				});
				if(ids.length > 0){
					qVm.takeoff.biddersIds = ids;
				}
			}
			qVm.takeoff.pageNumber = qVm.currentPage;
			qVm.takeoff.pageSize = qVm.pageSize;
			var formData = {};
			formData["takeoff"] = qVm.takeoff;
			AjaxUtil.submitData("/awacp/generateTakeoffReport", formData)
			.success(function(data, status, headers){
				qVm.report.mode = 'output';
				jQuery("#quote-rpt-btn").removeAttr('disabled');
				jQuery("#quote-rpt-spinner").css('display','none');
				if(data && data.stsResponse && data.stsResponse.totalCount){
						qVm.totalItems = data.stsResponse.totalCount;
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
								if(v.amount){
									qVm.totalQuoteAmount = (parseFloat(qVm.totalQuoteAmount) + parseFloat(v.amount)); 
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
							if(data.stsResponse.results.amount){
								qVm.totalQuoteAmount = (parseFloat(qVm.totalQuoteAmount) + parseFloat(data.stsResponse.results.amount)); 
							}
							tmp.push(data.stsResponse.results);
						}
						$scope.$apply(function(){
							qVm.quotes = tmp;
						});
					}else{
						$scope.$digest();
					}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.generateReport::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		if('quote-new-view' === $state.current.name){
			qVm.getPageSize('new');
		}else if('quote-view' === $state.current.name){
			qVm.getPageSize('quotes');
		}
		
		qVm.searchFieldName = "undefined";
		qVm.autoCompleteOptions = {			
            minimumChars: 3,
            dropdownWidth: '200px',
            dropdownHeight: '200px',
			loading:function(fieldName){qVm.searchFieldName = fieldName;},
			data: function (searchTerm) {
				var accessToken = StoreService.getAccessToken();
				var url = $rootScope.base + "/awacp/autoCompleteTakeoffList?keyword="+searchTerm+"&field="+qVm.searchFieldName+"&"+Math.random();
                return $http.get(url, {headers : { 'Authorization' : 'Bearer ' + accessToken, 'Accept' : 'application/json' }})
                    .then(function (response) {
                        qVm.loading = true;
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
						qVm.loading = false;
                    });
            },
			renderItem: function(item){
				return {'value':item.label, 'label':item.label};
			},
			itemSelected:function(selItem){
				qVm.setModelData(selItem);				
			}
        };
		qVm.setModelData = function(data){
			qVm.takeoff[data.item.field] = data.item.value;
			qVm.search();			
		}
		qVm.search = function(){
			qVm.quotes = [];
			if(qVm.sQuote.dateCreated){
				qVm.takeoff.dateCreated = new Date(qVm.sQuote.dateCreated);
			}
			qVm.takeoff.pageNumber = qVm.currentPage;
			qVm.takeoff.pageSize = qVm.pageSize;
			var formData = {};
			qVm.takeoff.view = 'quote';
			formData["takeoff"] = qVm.takeoff;
			AjaxUtil.submitData("/awacp/searchTakeoffs", formData)
			.success(function(data, status, headers){				
				if(data && data.stsResponse && data.stsResponse.totalCount){
					qVm.totalItems = data.stsResponse.totalCount;
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
						qVm.quotes = tmp;							
					});
				}else{
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteCtrl::qVm.search::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		qVm.clearSearch = function(){
			qVm.sQuote = {};
			qVm.takeoff = {};
			qVm.listQuotes();
		}
		qVm.triggerSearch = function(){
			qVm.search();
		}
	}		
})();



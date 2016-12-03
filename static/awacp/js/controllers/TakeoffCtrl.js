(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TakeoffCtrl', TakeoffCtrl);
	TakeoffCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','FileService','$uibModal'];
	function TakeoffCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, FileService, $uibModal){
		var takeVm = this;
		takeVm.openAnother = true;		
		takeVm.selectedTakeoff = {};
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
		takeVm.showFileListingView = function(source, sourceId, title, size){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size);
		}
		takeVm.showTakeoffInfo = function(takeoff){
			takeoff.openInfoBox = true;
			takeVm.selectedTakeoff =  takeoff;
		}
		takeVm.takeoffInfoPopover = {
			templateUrl: 'templates/takeoff-info.html',
			title: 'Takeoff Detail'
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
		takeVm.getSpecs = function(){
			takeVm.specs = [];
			AjaxUtil.getData("/awacp/listSpecifications/1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(data.stsResponse.totalCount == 1){
						tmp.push(data.stsResponse.results);
					}else{
						$.each(data.stsResponse.results, function(k, v){
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
						$.each(data.stsResponse.results, function(k, v){
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
						$.each(data.stsResponse.results, function(k, v){
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
						$.each(data.stsResponse.results, function(k, v){
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
						$.each(data.stsResponse.results, function(k, v){							
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
						$.each(data.stsResponse.results, function(k, v){
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
		takeVm.saveTakeoff = function(){
			if(takeVm.takeoff.revisedDate){
				if(!takeVm.isValidDateRange(takeVm.takeoff.drawingDate, takeVm.takeoff.revisedDate)){
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

			if(!takeVm.isValidDateRange(takeVm.takeoff.drawingDate, takeVm.takeoff.dueDate)){
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
			if(takeVm.selectedBidders.length > 0){
				var ids = [];
				$.each(takeVm.selectedBidders, function(k, v){
					ids.push(v.id);
				});
				if(ids.length > 0){
					takeVm.takeoff.biddersIds = ids;
				}
			}
			if(takeVm.selectedContractors.length > 0){
				var ids = [];
				$.each(takeVm.selectedContractors, function(k, v){
					ids.push(v.id);
				});
				if(ids.length > 0){
					takeVm.takeoff.contractorsIds = ids;
				}
			}
			
			var formData = {};
			takeVm.takeoff.userNameOrEmail = StoreService.getUserName();
			takeVm.takeoff["createdByUserCode"] = StoreService.getUser().userCode;
			formData["takeoff"] = takeVm.takeoff;
			AjaxUtil.submitData("/awacp/saveTakeoff", formData)
			.success(function(data, status, headers){
				takeVm.takeoff = {};
				var message = "New Takeoff Detail Created Successfully, add more?";
				AlertService.showConfirm(	'AWACP :: Alert!', message)
				.then(function (){return},function (){takeVm.cancelTakeoffAction();});
				return;
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.saveTakeoff::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.listTakeoffs = function(){
			takeVm.takeoffs = [];
			AjaxUtil.getData("/awacp/listTakeoffs/"+takeVm.currentPage+"/"+takeVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					takeVm.totalItems = 	data.stsResponse.totalCount;
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if($.isArray(data.stsResponse.results)) {
						$.each(data.stsResponse.results, function(k, v){
							v.openInfoBox = false;
							tmp.push(v);
						});					
					} else {
						data.stsResponse.results.openInfoBox = false;
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
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TakeoffCtrl', TakeoffCtrl);
	TakeoffCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService','StoreService'];
	function TakeoffCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var takeVm = this;
		$scope.timers = [];
		takeVm.biddersSettings = {displayProp: 'bidderTitle', idProp: 'id'};
		takeVm.contractorsSettings = {displayProp: 'contractorTitle', idProp: 'id'};
		takeVm.totalItems = 0;
		takeVm.currentPage = 1;
		takeVm.pageSize = 5;
		takeVm.users = [];
		takeVm.engineers = [];
		takeVm.architects = [];	
		takeVm.contractors = [];
		takeVm.bidders = [];
		takeVm.takeoffs = [];
		takeVm.takeoff = {};
		takeVm.selectedBidders = [];
		takeVm.selectedContractors = [];
		takeVm.setPage = function (pageNo) {
			takeVm.currentPage = pageNo;
		};
		takeVm.pageChanged = function() {
			console.log('Page changed to: ' + takeVm.currentPage);
		};	
		takeVm.cancelTakeoffAction = function(){
			$state.go("takeoff-view");
		}
		takeVm.getUsers = function(){
			takeVm.users = [];
			AjaxUtil.getData("/awacp/listUser/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						tmp.push(v);
					});
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
			AjaxUtil.getData("/awacp/listEngineers/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						tmp.push(v);
					});
					$scope.$apply(function(){
						takeVm.engineers= tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getEngineers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		takeVm.getArchitects = function(){
			takeVm.architects = [];
			AjaxUtil.getData("/awacp/listArchitects/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						tmp.push(v);
					});
					$scope.$apply(function(){
						takeVm.architects = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}		
		takeVm.getContractors = function(){
			takeVm.contractors = [];
			AjaxUtil.getData("/awacp/listContractors/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						tmp.push(v);
					});
					$scope.$apply(function(){
						takeVm.contractors = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getContractors::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.getBidders = function(){
			takeVm.bidders = [];
			AjaxUtil.getData("/awacp/listBidders/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						tmp.push(v);
					});
					$scope.$apply(function(){
						takeVm.bidders = tmp;
					});
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
			formData["takeoff"] = takeVm.takeoff;
			AjaxUtil.submitData("/awacp/saveTakeoff", formData)
			.success(function(data, status, headers){
				takeVm.takeoff = {};
				var message = "User Detail Created Successfully, add more?";
				AlertService.showConfirm(	'AWACP :: Alert!', message)
				.then(function (){return},function (){$state.go("users")});
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
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						tmp.push(v);
					});
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



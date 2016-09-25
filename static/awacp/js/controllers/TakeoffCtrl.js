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
			AjaxUtil.getData("/awacp/listUser", Math.random())
			.success(function(data, status, headers){
				if(data && data.user && data.user.length > 0){
					$.each(data.user, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						$scope.$apply(function(){
							takeVm.users.push(v); 
					    });
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
			AjaxUtil.listEngineers(function(result, status){
				if("success" === status){
					$scope.$apply(function(){
						takeVm.engineers = result;
					});
				}else{
					jqXHR.errorSource = "TakeoffCtrl::takeVm.initCountries::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		
		takeVm.getArchitects = function(){
			takeVm.architects = [];
			AjaxUtil.listArchitects(function(result, status){			
				if("success" === status){
					$scope.$apply(function(){
						takeVm.architects = result;
					});	
					console.log(JSON.stringify(takeVm.architects, null, 4));
				}else{
					jqXHR.errorSource = "TakeoffCtrl::takeVm.getArchitectures::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}		
		takeVm.getContractors = function(){
			takeVm.contractors = [];
			AjaxUtil.listContractors(function(result, status){			
				if("success" === status){
					$scope.$apply(function(){
						takeVm.contractors = result;
					});	
				}else{
					jqXHR.errorSource = "TakeoffCtrl::takeVm.listContractors::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		takeVm.getBidders = function(){
			takeVm.bidders = [];
			AjaxUtil.listBidders(function(result, status){			
				if("success" === status){
					$scope.$apply(function(){
						takeVm.bidders = result;
					});	
				}else{
					jqXHR.errorSource = "TakeoffCtrl::takeVm.getBidders::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
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
				angular.element("takeoff-reset").trigger("click");
				AlertService.showAlert(	'AWACP :: Message!','Takeoff added successfully.')
				.then(function (){					
					return
				},function (){return;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.saveTakeoff::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		takeVm.listTakeoffs = function(){
			takeVm.takeoffs = [];
			AjaxUtil.getData("/awacp/listTakeoffs?pageNumber="+takeVm.currentPage+"&pageSize=10", Math.random())
			.success(function(data, status, headers){
				if(data && data.takeoff && data.takeoff.length > 0){
					var tmp = [];
					$.each(data.takeoff, function(k, v){
						tmp.push(v);
						$scope.$apply(function(){
							takeVm.takeoffs = tmp;
					    });
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::takeVm.getUsers::Error";
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



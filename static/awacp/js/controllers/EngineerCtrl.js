(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('EngineerCtrl', EngineerCtrl);
	EngineerCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function EngineerCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var engVm = this;
		
		engVm.action = "Add";
		engVm.totalItems = -1;
		engVm.currentPage = 1;
		engVm.pageSize = 20;
		engVm.pageNumber = 1;
		$scope.timers = [];
		engVm.engineers = [];
		engVm.engineer = {};
		engVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		engVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("ENGINEER", size, function(status, size){
				if("success" === status){
					engVm.pageSize = size;
					engVm.pageChanged();
				}
			});
		}
		
		engVm.getPageSize = function(){
			AjaxUtil.getPageSize("ENGINEER", function(status, size){
				if("success" === status){
					engVm.pageSize = size;
				}
			});
		}
		engVm.pageChanged = function() {
			engVm.getEngineers();
		};
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		engVm.cancelEngineerAction = function(){
			$state.go("engineers");
		}
		
		
		engVm.getUsers = function(){
			engVm.users = [];
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
						engVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::engVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		engVm.initEngineerMasterInputs = function(){
			engVm.getUsers();
		}
		engVm.deleteEngineer = function(id){
			AjaxUtil.getData("/awacp/deleteEngineer/"+id, Math.random())
			.success(function(data, status, headers){
				engVm.totalItems = (engVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Engineer Detail Deleted Successfully.')
					.then(function (){engVm.getEngineers();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Engineer Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "EngineerCtrl::engVm.deleteEngineer::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
	    engVm.editEngineer = function(){
			if($state.params.id != undefined){
				AjaxUtil.getData("/awacp/getEngineer/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.engineer){
						data.engineer.customName = data.engineer.userCode + " - "+ data.engineer.firstName;
						$scope.$apply(function(){
							engVm.engineer = data.engineer;	
							engVm.action = engVm.engineer && engVm.engineer.id?"Update":"Add";							
						});
						engVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "EngineerCtrl::engVm.getEngineers::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		
		engVm.getEngineers = function(){
			engVm.engineers = [];
			engVm.pageNumber = engVm.currentPage;
			AjaxUtil.getData("/awacp/listEngineers/"+engVm.currentPage+"/"+engVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					engVm.totalItems = 	data.stsResponse.totalCount;
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
						engVm.engineers = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "Engineer::engVm.getEngineers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		engVm.addEngineer = function(){
			var message = "Engineer Detail Created Successfully, add more?";
			var url = "/awacp/saveEngineer";
			var update = false;
			if(engVm.engineer && engVm.engineer.id){
				message = "Engineer Detail Updated Successfully";
				engVm.engineer.updatedById = StoreService.getUser().userId;				
				engVm.engineer.updatedByUserCode = StoreService.getUser().userCode;
				engVm.engineer.auditMessage = "Updated engineer with name '"+engVm.engineer.name+"'";
				url = "/awacp/updateEngineer";
				update = true;
			}else{
				engVm.engineer.createdByUserCode = StoreService.getUser().userCode;
				engVm.engineer.createdById = StoreService.getUser().userId;
				engVm.engineer.auditMessage = "Added engineer with name '"+engVm.engineer.name+"'";
			}
			var formData = {};
			
			formData["engineer"] = engVm.engineer;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){engVm.cancelEngineerAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){engVm.cancelEngineerAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Engineer with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}else if(1003 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Engineer with this name already exist, please use a different name.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "EngineerCtrl::engVm.addEngineer::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}				
			});
		}
		engVm.editEngineer();
		engVm.getPageSize();
	}		
})();



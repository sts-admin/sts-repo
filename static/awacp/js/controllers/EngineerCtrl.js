(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('EngineerCtrl', EngineerCtrl);
	EngineerCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function EngineerCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var engVm = this;
		engVm.action = "Add";
		engVm.totalItems = 0;
		engVm.currentPage = 1;
		engVm.pageSize = 5;
		$scope.timers = [];
		engVm.engineers = [];
		engVm.engineer = {};
		
		engVm.setPage = function (pageNo) {
			engVm.currentPage = pageNo;
		};
		engVm.pageChanged = function() {
			console.log('Page changed to: ' + engVm.currentPage);
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
						$.each(data.stsResponse.results, function(k, v){
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
		
	    engVm.editEngineer = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["engineer"] = engVm.engineer;
				AjaxUtil.getData("/awacp/getEngineer/"+$state.params.id, formData)
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
					jqXHR.errorSource = "EngineerCtrl::bidVm.getEngineers::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		
		engVm.getEngineers = function(){
			engVm.engineers = [];
			AjaxUtil.getData("/awacp/listEngineers/"+engVm.currentPage+"/"+engVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					engVm.totalItems = 	data.stsResponse.totalCount;
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if($.isArray(data.stsResponse.results)) {
						$.each(data.stsResponse.results, function(k, v){
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
				engVm.engineer.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateEngineer";
				update = true;
			}else{
				engVm.engineer.createdByUserCode = StoreService.getUser().userCode;
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
				}
				jqXHR.errorSource = "EngineerCtrl::engVm.addEngineer::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		engVm.editEngineer();
		
	}		
})();



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('WorksheetCtrl', WorksheetCtrl);
	WorksheetCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function WorksheetCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var wsVm = this;
		wsVm.action = "Add";
		$scope.timers = [];
		wsVm.architects= [];
		wsVm.architect = {};
		
		wsVm.cancelArchitectAction = function(){
			$state.go("architects");
		}
		
		wsVm.initWorksheet = function(){
			wsVm.getWorksheet();
		}
		
		wsVm.editArchitect = function(){
			if($state.params.id != undefined){
				var formData = {};
				AjaxUtil.getData("/awacp/getArchitect/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.architect){
						data.architect.customName = data.architect.userCode + " - "+ data.architect.firstName;
						$scope.$apply(function(){
							wsVm.architect = data.architect;	
							wsVm.action = wsVm.architect && wsVm.architect.id?"Update":"Add";							
						});
						wsVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "ArchitectrCtrl::wsVm.getArchitectrs::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		wsVm.deleteArchitect = function(id){
			AjaxUtil.getData("/awacp/deleteArchitect/"+id, Math.random())
			.success(function(data, status, headers){
				wsVm.totalItems = (wsVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Architect Detail Deleted Successfully.')
					.then(function (){wsVm.getArchitects();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Architect Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "WorksheetCtrl::wsVm.deleteArchitect::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		wsVm.getArchitects = function(){
			wsVm.architects = [];
			wsVm.pageNumber = wsVm.currentPage;
			AjaxUtil.getData("/awacp/listArchitects/"+wsVm.pageNumber+"/"+wsVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						wsVm.totalItems = data.stsResponse.totalCount;
					});
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
						wsVm.architects = tmp;
					});
				}		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "WorksheetCtrl::wsVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		wsVm.addArchitect = function(){
			var message = "Architecture Detail Created Successfully, add more?";
			var url = "/awacp/saveArchitect";
			var update = false;
			if(wsVm.architect && wsVm.architect.id){
				message = "Architecture Detail Updated Successfully";
				wsVm.architect.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateArchitect";
				update = true;
			}else{
				wsVm.architect.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};			
			formData["architect"] = wsVm.architect;
			
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){wsVm.cancelArchitectAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){wsVm.cancelArchitectAction();});
					return;
				}
				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "An Architect with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "WorksheetCtrl::wsVm.addArchitect::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		wsVm.editArchitect();
		wsVm.getPageSize();
	}		
})();



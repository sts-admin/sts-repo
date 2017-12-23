(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ArchitectCtrl', ArchitectCtrl);
	ArchitectCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function ArchitectCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var arcVm = this;
		arcVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		arcVm.action = "Add";
		$scope.timers = [];
		arcVm.totalItems = -1;
		arcVm.currentPage = 1;
		arcVm.pageNumber = 1;
		arcVm.pageSize = 20;
		arcVm.architects= [];
		arcVm.architect = {};
		arcVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("ARCHITECT", size, function(status, size){
				if("success" === status){
					arcVm.pageSize = size;
					arcVm.pageChanged();
				}
			});
		}
		
		arcVm.getPageSize = function(){
			AjaxUtil.getPageSize("ARCHITECT", function(status, size){
				if("success" === status){
					arcVm.pageSize = size;
				}
			});
		}
		arcVm.pageChanged = function() {
			arcVm.getArchitects();
		};		
		arcVm.cancelArchitectAction = function(){
			$state.go("architects");
		}
		arcVm.getUsers = function(){
			arcVm.users = [];
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
						arcVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ArchitectCtrl::arcVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		arcVm.initArchitectMasterInputs = function(){
			arcVm.getUsers();
		}
		
		arcVm.editArchitect = function(){
			if($state.params.id != undefined){
				AjaxUtil.getData("/awacp/getArchitect/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.architect){
						data.architect.customName = data.architect.userCode + " - "+ data.architect.firstName;
						$scope.$apply(function(){
							arcVm.architect = data.architect;	
							arcVm.action = arcVm.architect && arcVm.architect.id?"Update":"Add";							
						});
						arcVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "ArchitectrCtrl::arcVm.getArchitectrs::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		arcVm.deleteArchitect = function(id){
			AjaxUtil.getData("/awacp/deleteArchitect/"+id, Math.random())
			.success(function(data, status, headers){
				arcVm.totalItems = (arcVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Architect Detail Deleted Successfully.')
					.then(function (){arcVm.getArchitects();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Architect Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ArchitectCtrl::arcVm.deleteArchitect::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		arcVm.getArchitects = function(){
			arcVm.architects = [];
			arcVm.pageNumber = arcVm.currentPage;
			AjaxUtil.getData("/awacp/listArchitects/"+arcVm.pageNumber+"/"+arcVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						arcVm.totalItems = data.stsResponse.totalCount;
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
						arcVm.architects = tmp;
					});
				}		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ArchitectCtrl::arcVm.getArchitects::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		arcVm.addArchitect = function(){
			var message = "Architecture Detail Created Successfully, add more?";
			var url = "/awacp/saveArchitect";
			var update = false;
			if(arcVm.architect && arcVm.architect.id){
				message = "Architecture Detail Updated Successfully";
				arcVm.architect.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateArchitect";
				update = true;
			}else{
				arcVm.architect.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};			
			formData["architect"] = arcVm.architect;
			
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){arcVm.cancelArchitectAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){arcVm.cancelArchitectAction();});
					return;
				}
				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "An Architect with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}else if(1003 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Architect with this name already exist, please use a different name.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "ArchitectCtrl::arcVm.addArchitect::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		arcVm.editArchitect();
		arcVm.getPageSize();
	}		
})();



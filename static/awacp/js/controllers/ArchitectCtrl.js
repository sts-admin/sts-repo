(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('ArchitectCtrl', ArchitectCtrl);
	ArchitectCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function ArchitectCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var arcVm = this;
		arcVm.action = "Add";
		$scope.timers = [];
		arcVm.totalItems = -1;
		arcVm.currentPage = 1;
		arcVm.pageNumber = 1;
		arcVm.pageSize = 5;
		arcVm.architects= [];
		arcVm.architect = {};
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
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						tmp.push(v);
					});
					$scope.$apply(function(){
						arcVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::arcVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		arcVm.initArchitectMasterInputs = function(){
			arcVm.getUsers();
		}
		
		 arcVm.editArchitect = function(){
			if($state.params.id != undefined){
				var formData = {};
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
					jqXHR.errorSource = "ArchitectrCtrl::bidVm.getArchitectrs::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		arcVm.getArchitects = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			arcVm.architects = [];
			AjaxUtil.getData("/awacp/listArchitects/"+arcVm.pageNumber+"/"+arcVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						arcVm.totalItems = data.stsResponse.totalCount;
					});
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
			alert(JSON.stringify(formData, null, 4));
			
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
				var message = "";
				if(1002 == jqXHR.status){
					message = "Email ID already taken.";
				}
				if(message.length > 0){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "ArchitectCtrl::arcVm.addArchitect::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
		
		arcVm.editArchitect();
	}		
})();



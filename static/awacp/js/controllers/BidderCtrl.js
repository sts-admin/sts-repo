(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('BidderCtrl', BidderCtrl);
	BidderCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval','$compile', '$stateParams', 'AlertService', 'StoreService'];
	function BidderCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, $stateParams, AlertService, StoreService){
		var bidVm = this;
		bidVm.action = "Add";
		bidVm.spinnerUrl = "<img src='images/loading.gif' />";
		$scope.timers = [];
		bidVm.bidders= [];
		bidVm.bidder = {};
		bidVm.users = [];
		
		bidVm.totalItems = -1;
		bidVm.currentPage = 1;
		bidVm.pageNumber = 1;
		bidVm.pageSize = 20;
		bidVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		bidVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("BIDDER", size, function(status, size){
				if("success" === status){
					bidVm.pageSize = size;
					bidVm.pageChanged();
				}
			});
		}
		
		bidVm.getPageSize = function(){
			AjaxUtil.getPageSize("BIDDER", function(status, size){
				if("success" === status){
					bidVm.pageSize = size;
				}
			});
		}
		bidVm.pageChanged = function() {
			bidVm.getBidders();
		};

		bidVm.cancelBidderAction = function(){
			$state.go("bidders");
		}
		

		bidVm.getUsers = function(){
			bidVm.users = [];
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
						bidVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "BidderCtrl::bidVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
			
		}
		bidVm.editBidder = function(){
			if($state.params.id != undefined){
				AjaxUtil.getData("/awacp/getBidder/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.bidder){
						data.bidder.customName = data.bidder.userCode + " - "+ data.bidder.firstName;
						$scope.$apply(function(){
							bidVm.bidder = data.bidder;		
							bidVm.action = bidVm.bidder && bidVm.bidder.id?"Update":"Add New";	
						});
						bidVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "BidderCtrl::bidVm.editBidder::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		bidVm.addBidder = function(){
			//AjaxUtil.toggleSpinner('login-submit', 'loading_span', bidVm.spinnerUrl, "disable");
			var message = "Bidder Detail Created Successfully, add more?";
			var url = "/awacp/saveBidder";
			var update = false;
			if(bidVm.bidder && bidVm.bidder.id){
				message = "Bidder Detail Updated Successfully";
				bidVm.bidder.updatedByUserCode = StoreService.getUser().userCode;
				bidVm.bidder.updatedById = StoreService.getUser().userId;
				bidVm.bidder.auditMessage = "Updated Bidder with item name '"+ bidVm.bidder.name + "'";
				url = "/awacp/updateBidder";
				update = true;
			}else{
				bidVm.bidder.createdByUserCode = StoreService.getUser().userCode;
				bidVm.bidder.createdById = StoreService.getUser().userId;
				bidVm.bidder.auditMessage = "Created Bidder with item name '"+ bidVm.bidder.name + "'";
			}
			var formData = {};
			formData["bidder"] = bidVm.bidder;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){	
				bidVm.bidder = {};
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){bidVm.cancelBidderAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){bidVm.cancelBidderAction();});
					return;
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Bidder with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}else if(1003 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "A Bidder with this name already exist, please use a different name.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "BidderCtrl::engVm.addBidder::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}	
			});
		}
		bidVm.initBidderMasterInputs = function(){
			if($state.params.id == undefined){
			   bidVm.getUsers();
		    }
		}		
		bidVm.deleteBidder = function(id){
			AjaxUtil.getData("/awacp/deleteBidder/"+id, Math.random())
			.success(function(data, status, headers){
				bidVm.totalItems = (bidVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Bidder Detail Deleted Successfully.')
					.then(function (){bidVm.getBidders();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Bidder Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "BidderCtrl::bidVm.deleteBidder::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		bidVm.getBidders = function(){
			bidVm.bidders = [];
			bidVm.pageNumber = bidVm.currentPage;
			AjaxUtil.getData("/awacp/listBidders/"+bidVm.pageNumber+"/"+bidVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){//Already set
					$scope.$apply(function(){
						bidVm.totalItems = data.stsResponse.totalCount;
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
						bidVm.bidders = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "BidderCtrl::bidVm.getBidders::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		bidVm.editBidder();
		
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



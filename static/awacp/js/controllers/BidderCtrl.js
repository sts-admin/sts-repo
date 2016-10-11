(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('BidderCtrl', BidderCtrl);
	BidderCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$stateParams', '$compile', 'AlertService'];
	function BidderCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $stateParams){
		var bidVm = this;
		bidVm.spinnerUrl = "<img src='images/loading.gif' />";
		bidVm.totalItems = -1;
		bidVm.currentPage = 1;
		bidVm.pageNumber = 1;
		bidVm.pageSize = 5;
		
		$scope.timers = [];
		bidVm.bidders= [];
		bidVm.bidder = {};
		bidVm.countries = [];
		bidVm.states = [];
		bidVm.users = [];
		
		bidVm.pageChanged = function() {
			bidVm.getBidders();
		};

		bidVm.cancelBidderAction = function(){
			$state.go("bidders");
		}
		
	   bidVm.initCountries = function(){
			bidVm.countries = [];
			AjaxUtil.listCountries(function(result, status){
				if("success" === status){
					bidVm.countries = result;
				}else{
					jqXHR.errorSource = "BidderCtrl::bidVm.initCountries::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		bidVm.getStates = function(){
			bidVm.states = [];
			AjaxUtil.listStates(bidVm.bidder.country.id, function(result, status){				
				if("success" === status){
					$scope.$apply(function(){
						bidVm.states = result;
					});					
				}else{
					jqXHR.errorSource = "BidderCtrl::bidVm.getStates::Error, countryId = " + bidVm.bidder.country.id;
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		bidVm.getUsers = function(){
			bidVm.users = [];
			AjaxUtil.getData("/awacp/listUser/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results && data.stsResponse.results.length > 0){
					var tmp = [];
					$.each(data.stsResponse.results, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						tmp.push(v);
					});
					$scope.$apply(function(){
						bidVm.users = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TakeoffCtrl::bidVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
			
		}
		bidVm.editBidder = function(){
			if($state.params.id != undefined){
				var formData = {};
				formData["bidder"] = bidVm.bidder;
				AjaxUtil.getData("/awacp/getBidder/"+$state.params.id, formData)
				.success(function(data, status, headers){
					if(data && data.bidder){
						data.bidder.customName = data.bidder.userCode + " - "+ data.bidder.firstName;
						$scope.$apply(function(){
							bidVm.bidder = data.bidder;							
						});
						bidVm.initCountries();
						bidVm.getStates();
						bidVm.getUsers();
						
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "BidderCtrl::bidVm.getBidders::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		
		bidVm.addBidder = function(){
			AjaxUtil.toggleSpinner('login-submit', 'loading_span', bidVm.spinnerUrl, "disable");
			var formData = {};
			formData["bidder"] = bidVm.bidder;
			AjaxUtil.submitData("/awacp/saveBidder", formData)
			.success(function(data, status, headers){				
				bidVm.bidder = {};
				var message = "Bidder Detail Created Successfully, add more?";
				AlertService.showConfirm(	'AWACP :: Alert!', message)
				.then(function (){return},function (){bidVm.cancelBidderAction();});
				return;
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "BidderCtrl::bidVm.addBidder::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		bidVm.initBidderMasterInputs = function(){
			if($state.params.id == undefined){
			   bidVm.initCountries();
			   bidVm.getUsers();
		    }
		}
		
		
		bidVm.getBidders = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
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
					if(data.stsResponse.totalCount == 1){
						tmp.push(data.stsResponse.results);
					}else{
						$.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
						});
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



(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('BidderCtrl', BidderCtrl);
	BidderCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$stateParams', '$compile', 'AlertService'];
	function BidderCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $stateParams){
		var bidVm = this;
		bidVm.spinnerUrl = "<img src='images/loading.gif' />";
		bidVm.totalItems = 20;
		bidVm.currentPage = 1;
		$scope.pageNumber = 1;
		bidVm.pageSize = 5;
		$scope.pagination = {
			currentPage: 1,
			maxSize: 21,
			totalItems: 22
		};
		
		$scope.timers = [];
		bidVm.bidders= [];
		bidVm.bidder = {};
		bidVm.countries = [];
		bidVm.states = [];
		bidVm.users = [];
		
		
		bidVm.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
		
		bidVm.pageChanged = function() {
			bidVm.getBidders(bidVm.currentPage);
		};

		bidVm.cancelBidderAction = function(){
			$state.go("bidders");
		}
		
	    $scope.loadMore = function(){
          bidVm.getBidders($scope.pageNumber);
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
			AjaxUtil.getData("/awacp/listUser", Math.random())
			.success(function(data, status, headers){
				if(data && data.user && data.user.length > 0){
					var tmp = [];
					$.each(data.user, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						tmp.push(v);						
					});
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
						$scope.$apply(function(){
							bidVm.bidder = data.bidder;
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "BidderCtrl::bidVm.getBidders::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		bidVm.updateBidder = function(){
			
		}
		bidVm.addBidder = function(){
			AjaxUtil.toggleSpinner('login-submit', 'loading_span', bidVm.spinnerUrl, "disable");
			var formData = {};
			formData["bidder"] = bidVm.bidder;
			AjaxUtil.submitData("/awacp/saveBidder", formData)
			.success(function(data, status, headers){				
				bidVm.bidder = {};
				AlertService.showConfirm('AWACP :: Message!','Bidder added successfully. Add more?')
				.then(function (){	
					return
				},function (){
					bidVm.cancelBidderAction();
				});
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
		
		
		bidVm.getBidders = function(pageNumber){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			AjaxUtil.getData("/awacp/listBidders/"+pageNumber+"/5", Math.random())
			.success(function(data, status, headers){
				$scope.$apply(function(){
					$scope.pageNumber = $scope.pageNumber + 1;
				});
				
				if(data && data.bidder && data.bidder.length > 0){
					var tmp = [];
					console.log(JSON.stringify(data,null,4));
					$scope.$apply(function(){
					   bidVm.totalItems = data.bidder[0].countBidders;
				    });
					
					$.each(data.bidder, function(k, v){
						tmp.push(v);						
					});
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
		// if(!bidderId || bidderId != undefined ){
			// bidVm.getBidder();
		// }
		bidVm.editBidder();
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



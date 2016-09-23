(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('BidderCtrl', BidderCtrl);
	BidderCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService'];
	function BidderCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService){
		var bidVm = this;
		bidVm.totalItems = 0;
		bidVm.currentPage = 1;
		bidVm.setPage = function (pageNo) {
			$scope.currentPage = pageNo;
		};
		bidVm.pageChanged = function() {
			console.log('Page changed to: ' + bidVm.currentPage);
		};
		$scope.timers = [];
		bidVm.bidders= [];
		bidVm.bidder = {};
		bidVm.countries = [];
		bidVm.states = [];
		bidVm.users = [];
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
			AjaxUtil.getData("/awacp/listUser", Math.random())
			.success(function(data, status, headers){
				if(data && data.user && data.user.length > 0){
					$.each(data.user, function(k, v){
						v.customName = v.userCode + " - "+ v.firstName;
						bidVm.users.push(v);
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "BidderCtrl::bidVm.getUsers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		bidVm.editBidder = function(){
			
		}
		bidVm.updateBidder = function(){
			
		}
		bidVm.addBidder = function(){
			AjaxUtil.toggleSpinner('login-submit', 'loading_span', userVm.spinnerUrl, "disable");
			var formData = {};
			formData["bidder"] = bidVm.bidder;
			AjaxUtil.submitData("/awacp/saveBidder", formData)
			.success(function(data, status, headers){				
				AlertService.showAlert(	'AWACP :: Message!','Bidder added successfully.')
				.then(function (){					
					return
				},function (){return;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "BidderCtrl::bidVm.addBidder::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		bidVm.initBidderMasterInputs = function(){
			bidVm.initCountries();
			bidVm.getUsers();
		}
		bidVm.getBidders = function(){
			if(!AjaxUtil.isAuthorized()){
				return;
			}
			bidVm.bidders = [];
			AjaxUtil.getData("/awacp/listBidders", Math.random())
			.success(function(data, status, headers){
				if(data && data.bidder && data.bidder.length > 0){
					bidVm.totalItems = data.bidder.length;
					$.each(data.bidder, function(k, v){
						bidVm.bidders.push(v);
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "BidderCtrl::bidVm.getBidders::Error";
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




(function() {
    'use strict';
	//Local env
	var base ="http://localhost:8080/awacpservices";
	var resourceReadPath = "http://localhost/awacp/resource/img/";	
    angular.module('awacpApp', ['awacpApp.services', 'awacpApp.controllers','angular-storage','ui.router','checklist-model', 'angularMoment', 'ui.bootstrap', 'angularjs-dropdown-multiselect', 'ui.navbar', 'ui.bootstrap.tpls', 'ds.clock'])
		.constant("base", base).constant("resourceReadPath", resourceReadPath)
		.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
			$stateProvider				
			 .state('/',{
				url: '/',
				templateUrl:"templates/login.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				cache:false
			}).state('dashboard',{
				url: '/dashboard',
				templateUrl:"templates/dashboard.html",
				controller:"DashboardCtrl",
				controllerAs:"dashVm"
			})
			.state('admin',{
				url: '/admin',
				templateUrl:"templates/dashboard-admin.html",
				controller:"AdminCtrl",
				controllerAs:"adminVm"
			})
			.state('users',{
				url: '/users',
				templateUrl:"templates/users.html",
				controller:"UserCtrl",
				controllerAs:"userVm"
			}).state('add-user',{
				url: '/manage/user/add',
				templateUrl:"templates/user-add.html",
				controller:"UserCtrl",
				controllerAs:"userVm"
			}).state('edit-user',{
				url: '/manage/user/edit/:id',
				templateUrl:"templates/user-edit.html",
				controller:"UserCtrl",
				controllerAs:"userVm"
			}).state('roles',{
				url: '/manage/roles',
				templateUrl:"templates/roles.html",
				controller:"RoleCtrl",
				controllerAs:"roleVm"
			}).state('quote-new-view',{
				url: '/quote/add',
				templateUrl:"templates/quotes-new.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm"
			}).state('quote-view',{
				url: '/quotes',
				templateUrl:"templates/quotes.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm"
			}).state('quote-search',{
				url: '/quote/search',
				templateUrl:"templates/quote-search.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm"
			}).state('takeoff-view',{
				url: '/takeoffs',
				templateUrl:"templates/takeoffs.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm"
			}).state('takeoff-add',{
				url: '/takeoff/add',
				templateUrl:"templates/takeoff-add.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm"
			}).state('takeoff-search',{
				url: '/takeoff/search',
				templateUrl:"templates/takeoff-search.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm"
			}).state('takeoff-reports',{
				url: '/takeoff/reports',
				templateUrl:"templates/takeoff-report.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm"
			}).state('bidders',{
				url: '/bidders',
				templateUrl:"templates/bidders.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm"
			})
			.state('bidder-edit',{
				url: '/bidder/edit/:id',
				templateUrl:"templates/bidder-add.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm"
			}).state('bidder-add',{
				url: '/bidder/add',
				templateUrl:"templates/bidder-add.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm"
			}).state('engineers',{
				url: '/engineers',
				templateUrl:"templates/engineers.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm"
			}).state('engineer-add',{
				url: '/engineer/add',
				templateUrl:"templates/engineer-add.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm"
			}).state('engineer-edit',{
				url: '/engineer/edit/:id',
				templateUrl:"templates/engineer-add.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm"
			}).state('architects',{
				url: '/architects',
				templateUrl:"templates/architects.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm"
			}).state('architect-add',{
				url: '/architect/add',
				templateUrl:"templates/architect-add.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm"
			}).state('architect-edit',{
				url: '/architect/edit/:id',
				templateUrl:"templates/architect-add.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm"
			}).state('gcs',{
				url: '/gcs',
				templateUrl:"templates/gcs.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm"
			}).state('gc-add',{
				url: '/gc/add',
				templateUrl:"templates/gc-add.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm"
			}).state('gc-edit',{
				url: '/gc/edit/:id',
				templateUrl:"templates/gc-add.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm"
			}).state('contractors',{
				url: '/contractors',
				templateUrl:"templates/contractors.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm"
			}).state('contractor-add',{
				url: '/contractor/add',
				templateUrl:"templates/contractor-add.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm"
			}).state('contractor-edit',{
				url: '/contractor/edit/:id',
				templateUrl:"templates/contractor-add.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm"
			}).state('specifications',{
				url: '/specifications',
				templateUrl:"templates/specifications.html",
				controller:"SpecificationCtrl",
				controllerAs:"specVm"
			});
			// if none of the above states are matched, use this as the fallback
			$locationProvider.html5Mode(true);
			$urlRouterProvider.otherwise('/');
		}).run(function($rootScope, $state, store, $window, AjaxUtil, StoreService, $timeout, resourceReadPath, UserService) {
			$rootScope.gmtValue = 5.3;
			$rootScope.dayDiff = function(startdate, enddate) {
				var dayCount = 0;
				while(enddate >= startdate) {
					dayCount++;
					startdate.setDate(startdate.getDate() + 1);
				}
				return dayCount; 
			}
			$rootScope.resourceReadPath = resourceReadPath;
			$rootScope.dateFormats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
			$rootScope.dateFormat = $rootScope.dateFormats[0];
			$rootScope.altInputFormats = ['M!/d!/yyyy'];
			$rootScope.dateOptions = { formatYear: 'yy', maxDate: new Date(2025, 5, 22), minDate: new Date(), startingDay: 0, showWeeks: false};
			$rootScope.inlineOptions = { minDate: new Date(), showWeeks: true };
			$rootScope.logoutUser =function(){
				UserService.logout();
			};			
			$rootScope.user = {isLoggedIn:StoreService.isLoggedIn(), userDisplayName:StoreService.userDisplayName(), role:StoreService.getRole()};
			$rootScope.alert = {noService:false};
			
			$rootScope.setUpUserMenu = function(){
				UserService.initializeMenu(function(jqXHR, status){
					if("success" === status){
						$rootScope.$apply(function(){
							$rootScope.menus = jqXHR;
						});
					}else{
						jqXHR.errorSource = "RootScope::setUpUserMenu::Error";
						AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
					}
				});		
			}
			if($rootScope.user.isLoggedIn){
				$rootScope.setUpUserMenu();
			}
		});
})();

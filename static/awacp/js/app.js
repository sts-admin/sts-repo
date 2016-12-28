
(function() {
    'use strict';
	//Local env
	var base ="http://localhost:8080/awacpservices";
	var resourceReadPath = "http://localhost/awacp/resource/img/";
	var basePath = "/awacp/";
	//prod env
	/*var base ="http://awacptechnicalservices.com:8080/awacpservices";
	var resourceReadPath = "http://awacptechnicalservices.com/resource/img/";	
	var basePath = "/";*/
    angular.module('awacpApp', ['awacpApp.services', 'awacpApp.controllers','angular-storage','ui.router','checklist-model', 'angularMoment', 'ui.bootstrap', 'angularjs-dropdown-multiselect', 'ui.navbar', 'ui.bootstrap.tpls', 'ds.clock','ui.select', 'ngSanitize','ui-listView','ngFileUpload', 'angucomplete-alt'])
		.constant("base", base).constant("resourceReadPath", resourceReadPath).constant("basePath", basePath)
		.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
			$stateProvider				
			 .state('/',{
				url: '/',
				templateUrl:"templates/login.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				cache:false,
				requireAuth: false
			}).state('dashboard',{
				url: '/dashboard',
				templateUrl:"templates/dashboard.html",
				controller:"DashboardCtrl",
				controllerAs:"dashVm",
				requireAuth: true
			})
			.state('admin',{
				url: '/admin',
				templateUrl:"templates/dashboard-admin.html",
				controller:"AdminCtrl",
				controllerAs:"adminVm",
				requireAuth: true
			})
			.state('users',{
				url: '/users',
				templateUrl:"templates/users.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				requireAuth: true
			}).state('add-user',{
				url: '/manage/user/add',
				templateUrl:"templates/user-add.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				requireAuth: true
			}).state('edit-user',{
				url: '/manage/user/edit/:id',
				templateUrl:"templates/user-edit.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				requireAuth: true
			}).state('roles',{
				url: '/manage/roles',
				templateUrl:"templates/roles.html",
				controller:"RoleCtrl",
				controllerAs:"roleVm",
				requireAuth: true
			}).state('quote-new-view',{
				url: '/quote/add',
				templateUrl:"templates/quotes-new.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm",
				requireAuth: true
			}).state('quote-view',{
				url: '/quotes',
				templateUrl:"templates/quotes.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm",
				requireAuth: true
			}).state('quote-search',{
				url: '/quote/search',
				templateUrl:"templates/quote-search.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm",
				requireAuth: true
			}).state('takeoff-view',{
				url: '/takeoffs',
				templateUrl:"templates/takeoffs.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('takeoff-add',{
				url: '/takeoff/add',
				templateUrl:"templates/takeoff-add.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('takeoff-search',{
				url: '/takeoff/search',
				templateUrl:"templates/takeoff-search.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('takeoff-reports',{
				url: '/takeoff/reports',
				templateUrl:"templates/takeoff-report.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('bidders',{
				url: '/bidders',
				templateUrl:"templates/bidders.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm",
				requireAuth: true
			})
			.state('bidder-edit',{
				url: '/bidder/edit/:id',
				templateUrl:"templates/bidder-add.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm",
				requireAuth: true
			}).state('bidder-add',{
				url: '/bidder/add',
				templateUrl:"templates/bidder-add.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm",
				requireAuth: true
			}).state('engineers',{
				url: '/engineers',
				templateUrl:"templates/engineers.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm",
				requireAuth: true
			}).state('engineer-add',{
				url: '/engineer/add',
				templateUrl:"templates/engineer-add.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm",
				requireAuth: true
			}).state('engineer-edit',{
				url: '/engineer/edit/:id',
				templateUrl:"templates/engineer-add.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm",
				requireAuth: true
			}).state('architects',{
				url: '/architects',
				templateUrl:"templates/architects.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm",
				requireAuth: true
			}).state('architect-add',{
				url: '/architect/add',
				templateUrl:"templates/architect-add.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm",
				requireAuth: true
			}).state('architect-edit',{
				url: '/architect/edit/:id',
				templateUrl:"templates/architect-add.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm",
				requireAuth: true
			}).state('gcs',{
				url: '/gcs',
				templateUrl:"templates/gcs.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm",
				requireAuth: true
			}).state('gc-add',{
				url: '/gc/add',
				templateUrl:"templates/gc-add.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm",
				requireAuth: true
			}).state('gc-edit',{
				url: '/gc/edit/:id',
				templateUrl:"templates/gc-add.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm",
				requireAuth: true
			}).state('contractors',{
				url: '/contractors',
				templateUrl:"templates/contractors.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm",
				requireAuth: true
			}).state('contractor-add',{
				url: '/contractor/add',
				templateUrl:"templates/contractor-add.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm",
				requireAuth: true
			}).state('contractor-edit',{
				url: '/contractor/edit/:id',
				templateUrl:"templates/contractor-add.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm",
				requireAuth: true
			}).state('specifications',{
				url: '/specifications',
				templateUrl:"templates/specifications.html",
				controller:"SpecificationCtrl",
				controllerAs:"specVm",
				requireAuth: true
			}).state('ships',{
				url: '/ships',
				templateUrl:"templates/shipto.html",
				controller:"ShipToCtrl",
				controllerAs:"shipToVm",
				requireAuth: true
			}).state('truckers',{
				url: '/truckers',
				templateUrl:"templates/truckers.html",
				controller:"TruckerCtrl",
				controllerAs:"truckerVm",
				requireAuth: true
			}).state('trucker-edit',{
				url: '/trucker/edit/:id',
				templateUrl:"templates/trucker-add.html",
				controller:"TruckerCtrl",
				controllerAs:"truckerVm",
				requireAuth: true
			}).state('trucker-add',{
				url: '/trucker/add',
				templateUrl:"templates/trucker-add.html",
				controller:"TruckerCtrl",
				controllerAs:"truckerVm",
				requireAuth: true
			}).state('products',{
				url: '/products',
				templateUrl:"templates/products.html",
				controller:"ProductCtrl",
				controllerAs:"prodVm",
				requireAuth: true
			}).state('pndis',{
				url: '/pdnis',
				templateUrl:"templates/pdnis.html",
				controller:"PdniCtrl",
				controllerAs:"pdniVm",
				requireAuth: true
			}).state('qnotes',{
				url: '/qnotes',
				templateUrl:"templates/qnotes.html",
				controller:"QuoteNotesCtrl",
				controllerAs:"qnoteVm",
				requireAuth: true
			}).state('manufactures',{
				url: '/manufactures',
				templateUrl:"templates/manufactures.html",
				controller:"ManufactureCtrl",
				controllerAs:"manuVm",
				requireAuth: true
			}).state('iships',{
				url: '/iships',
				templateUrl:"templates/itemships.html",
				controller:"ItemShipCtrl",
				controllerAs:"itemVm",
				requireAuth: true
			}).state('vships',{
				url: '/vships',
				templateUrl:"templates/shippedvia.html",
				controller:"ShippedViaCtrl",
				controllerAs:"shipViaVm",
				requireAuth: true
			}).state('j-view',{
				url: '/j-view',
				templateUrl:"templates/jinventories.html",
				controller:"JInventoryCtrl",
				controllerAs:"jinvVm",
				requireAuth: true
			}).state('j-add',{
				url: '/j-add',
				templateUrl:"templates/jinventory-add.html",
				controller:"JInventoryCtrl",
				controllerAs:"jinvVm",
				requireAuth: true
			}).state('j-orders',{
				url: '/j-orders',
				templateUrl:"templates/jinventory-orders.html",
				controller:"JInventoryCtrl",
				controllerAs:"jinvVm",
				requireAuth: true
			}).state('spl-view',{
				url: '/spl-view',
				templateUrl:"templates/spl-inventories.html",
				controller:"SplInventoryCtrl",
				controllerAs:"splInvVm",
				requireAuth: true
			}).state('spl-add',{
				url: '/spl-add',
				templateUrl:"templates/spl-inventory-add.html",
				controller:"SplInventoryCtrl",
				controllerAs:"splInvVm",
				requireAuth: true
			}).state('spl-orders',{
				url: '/spl-orders',
				templateUrl:"templates/spl-inventory-orders.html",
				controller:"SplInventoryCtrl",
				controllerAs:"splInvVm",
				requireAuth: true
			}).state('deletefiles',{
				url: '/deletefiles',
				templateUrl:"templates/deletefiles.html",
				controller:"DeleteFileCtrl",
				controllerAs:"deleteVm",
				requireAuth: true
			});
			// if none of the above states are matched, use this as the fallback
			$locationProvider.html5Mode(true);
			$urlRouterProvider.otherwise('/');
		}).run(function($rootScope, $state, store, $window, AjaxUtil, StoreService, $timeout, resourceReadPath, UserService) {
			$rootScope.fileViewSource = "templates/file-listing.html";
			$rootScope.gmtValue = 5.3;
			$rootScope.dayDiff = function(startdate, enddate) {
				var dayCount = 0;
				while(enddate >= startdate) {
					dayCount++;
					startdate.setDate(startdate.getDate() + 1);
				}
				return dayCount; 
			}
			$rootScope.currentDate = new Date();
			$rootScope.resourceReadPath = resourceReadPath;
			$rootScope.dateFormats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
			$rootScope.dateFormat = $rootScope.dateFormats[0];
			$rootScope.altInputFormats = ['M!/d!/yyyy'];
			$rootScope.dateOptions = { formatYear: 'yy', maxDate: new Date(2025, 5, 22), minDate: new Date(), startingDay: 0, showWeeks: false};
			$rootScope.inlineOptions = { minDate: new Date(), showWeeks: true };
			$rootScope.logoutUser =function(){
				UserService.logout();
			};			
			$rootScope.user = {userCode:StoreService.getUserCode(), isLoggedIn:StoreService.isLoggedIn(), userDisplayName:StoreService.userDisplayName(), role:StoreService.getRole()};
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
			$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
				/*alert("stateChangeStart, oldUrl = "+JSON.stringify(fromState, null, 4)+ ", newUrl = "+JSON.stringify(toState, null, 4)+", newUrl.requireAuth = " + toState.requireAuth + ", is user logged in = "+ $rootScope.user.isLoggedIn);*/
				if(fromState.url === "/" && (toState.url === "/dashboard" || toState.url === "/admin")){ //Login to administrator view or dashboard
					return;
				}else{
					if (toState.requireAuth && !$rootScope.user.isLoggedIn) {	
						StoreService.removeAll();
						$window.location.href = basePath;
					}
				}				
			});
		});
})();

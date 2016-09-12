
(function() {
    'use strict';
	//Local env
	var base ="http://localhost:8080/awacpservices";
	var resourceReadPath = "http://localhost/awacp/resource/img/";	
    angular.module('awacpApp', ['awacpApp.services', 'awacpApp.controllers', 'angular-storage', 'ui.router','checklist-model', 'angularMoment', 'ui.bootstrap', 'angularjs-dropdown-multiselect', 'ui.navbar', 'ui.bootstrap.tpls'])
		.constant("base", base).constant("resourceReadPath", resourceReadPath)
		.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
			$stateProvider				
			 .state('/',{
				url: '/',
				templateUrl:"templates/login.html",
				controller:"UserCtrl",
				controllerAs:"userVm"
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
			}).state('users',{
				url: '/users',
				templateUrl:"templates/users.html",
				controller:"UserCtrl",
				controllerAs:"userVm"
			}).state('roles',{
				url: '/manage/roles',
				templateUrl:"templates/roles.html",
				controller:"RoleCtrl",
				controllerAs:"roleVm"
			}).state('add-user',{
				url: '/manage/users/add',
				templateUrl:"templates/new_user.html",
				controller:"UserCtrl",
				controllerAs:"userVm"
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
			}).state('architects',{
				url: '/architects',
				templateUrl:"templates/architects.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm"
			}).state('architect-add',{
				url: '/architect/add',
				templateUrl:"templates/architect-add.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm"
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
			});
			// if none of the above states are matched, use this as the fallback
			$locationProvider.html5Mode(true);
			$urlRouterProvider.otherwise('/');
		}).run(function($rootScope, $state, store, $window, AjaxUtil, StoreService, $timeout, resourceReadPath) {
			$rootScope.resourceReadPath = resourceReadPath;
			$rootScope.dateFormats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
			$rootScope.dateFormat = $rootScope.dateFormats[0];
			$rootScope.altInputFormats = ['M!/d!/yyyy'];
			$rootScope.dateOptions = { formatYear: 'yy', maxDate: new Date(2025, 5, 22), minDate: new Date(), startingDay: 0, showWeeks: false};
			$rootScope.inlineOptions = { minDate: new Date(), showWeeks: true };
			$rootScope.logoutUser =function(){
				AjaxUtil.logout();
			};			
			$rootScope.user = {isLoggedIn:StoreService.isLoggedIn(), profileImageUrl: StoreService.profileImageUrl()};
			$rootScope.alert = {noService:false};
		}).directive('blink', function($timeout) {
		return {
			restrict: 'E',
			transclude: true,
			scope: {},
			controller: function($scope, $element) {
				function showElement() {
					$element.css("visibility", "visible");
					$timeout(hideElement, 1000);
				}

				function hideElement() {
					$element.css("visibility", "hidden");
					$timeout(showElement, 1000);
				}
				showElement();
			},
			template: '<span ng-transclude></span>',
			replace: true
		};
	});
})();


(function() {
    'use strict';
	//Local env
	var base ="http://localhost:8080/awacpservices";
	var resourceReadPath = "http://localhost/awacp/resource/img/";	
    angular.module('awacpApp', ['awacpApp.controllers' ,'awacpApp.services', 'angular-storage', 'ui.router','checklist-model', 'angularMoment', 'ui.bootstrap', 'angularjs-dropdown-multiselect', 'ui.navbar', 'SS', 'ui.bootstrap.tpls'])
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
			}).state('admin',{
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

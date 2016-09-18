(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('NavigationCtrl', NavigationCtrl);
	NavigationCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'UserService'];
	function NavigationCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, UserService){

		var navVm = this;
		$scope.timers = [];
		navVm.takeOfMenu = 
		[
			{ name: "Add New", link: "takeoff-add" }, 
			{ name: "divider", link: "#" },
			{ name: "View/Search", link: "takeoffs" }, 
			{ name: "divider", link: "#" },
			{ name: "Reports", link: "takeoff-report" }
		];
		navVm.quoteMenu = 
		[
			{ name: "Add New", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View/Search", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Reports", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Follow Up", link: "#" }
		];
		navVm.orderMenu = 
		[
			{ name: "Add New", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View/Search", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Reports", link: "#" }
		];
		navVm.bookMenu = 
		[
			{ name: "Add New", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View/Search", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Reports", link: "#" }
		];
		navVm.awMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		navVm.awfMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		navVm.sbcMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		navVm.splMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		navVm.jobMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		navVm.claimMenu = 
		[
			{ name: "Trucker", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Factory", link: "#" }
		];
		navVm.marketingMenu = 
		[
			{ name: "Manage Template", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Send Mail", link: "#" }
		];
		navVm.bbtMenu = 
		[
			{ name: "Engineer", link: "engineers" }, 
			{ name: "divider", link: "#" },
			{ name: "Architect", link: "architects" },
			{ name: "divider", link: "#" },
			{ name: "Contractor", link: "contractors" },
			{ name: "divider", link: "#" },
			{ name: "Bidder", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Trucker", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Specification", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Product", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Ship To", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "PNDI", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Quote Notes", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Manufacture & Description", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Item Shipped", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Shipped Via", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Delete File", link: "#" }
		];
	
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});
	}		
})();



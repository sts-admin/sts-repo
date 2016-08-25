(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('NavigationCtrl', NavigationCtrl);
	NavigationCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile'];
	function NavigationCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile){
		var navVm = this;
		$scope.takeOfMenu = 
		[
			{ name: "Add New", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View/Search", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Reports", link: "#" }
		];
		$scope.quoteMenu = 
		[
			{ name: "Add New", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View/Search", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Reports", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Follow Up", link: "#" }
		];
		$scope.orderMenu = 
		[
			{ name: "Add New", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View/Search", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Reports", link: "#" }
		];
		$scope.bookMenu = 
		[
			{ name: "Add New", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View/Search", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Reports", link: "#" }
		];
		$scope.awMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		$scope.awfMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		$scope.sbcMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		$scope.splMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		$scope.jobMenu = 
		[
			{ name: "New Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Inventory", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "View Orders", link: "#" }
		];
		$scope.claimMenu = 
		[
			{ name: "Trucker", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Factory", link: "#" }
		];
		$scope.marketingMenu = 
		[
			{ name: "Manage Template", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Send Mail", link: "#" }
		];
		$scope.bbtMenu = 
		[
			{ name: "Engineer", link: "#" }, 
			{ name: "divider", link: "#" },
			{ name: "Architect", link: "#" },
			{ name: "divider", link: "#" },
			{ name: "Contractor", link: "#" },
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



(function() {
	'use strict';
	angular.module('awacpApp.services')
		.factory("AlertService",["$q", "$uibModal", function ($q, $uibModal){
			return {
				showAlert:function (title, message){
					var defer = $q.defer();
					var modalInstance = $uibModal.open({
						animation: true,
						size: "sm",
						templateUrl: 'templates/alert.html',
						controller: function ($scope, $uibModalInstance){
							$scope.title = title;
							$scope.message = message;
							$scope.ok = function (){
								modalInstance.dismiss();
								defer.resolve();
							};
						}
					});
					return defer.promise;
				}
			};

	}]);
})();







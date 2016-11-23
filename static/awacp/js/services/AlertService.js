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
						windowClass:'alert-zindex',
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
				},
				showConfirm:function (title, message){
					var defer = $q.defer();
					var modalInstance = $uibModal.open({
						animation: true,
						size: "sm",
						templateUrl: 'templates/confirm.html',
						windowClass:'alert-zindex ',
						controller: function ($scope, $uibModalInstance){
							$scope.title = title;
							$scope.message = message;
							$scope.yes = function (){
								modalInstance.dismiss();
								defer.resolve();
							};
							$scope.no = function (){
								modalInstance.dismiss();
								defer.reject();
							};
						}
					});
					return defer.promise;
				}
			};
		}
	]);
})();







(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('QuoteNotesCtrl', QuoteNotesCtrl);
	QuoteNotesCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService'];
	function QuoteNotesCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService){
		var qnoteVm = this;
	   
		$scope.timers = [];
		qnoteVm.quoteNotes= [];
		qnoteVm.quoteNote = {};
		qnoteVm.action = "Add";
		qnoteVm.totalItems = -1;
		qnoteVm.currentPage = 1;
		qnoteVm.pageNumber = 1;
		qnoteVm.pageSize = 20;
		qnoteVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		qnoteVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("QUOTE_NOTE", size, function(status, size){
				if("success" === status){
					qnoteVm.pageSize = size;
					qnoteVm.pageChanged();
				}
			});
		}
		
		qnoteVm.getPageSize = function(){
			AjaxUtil.getPageSize("QUOTE_NOTE", function(status, size){
				if("success" === status){
					qnoteVm.pageSize = size;
				}
			});
		}
		qnoteVm.addQuoteNote = function (title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/qnote-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.title = title;
					$scope.note = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.note || $scope.note.length <= 0){
							$scope.message = "Please Enter Quote Note Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {}, quoteNote = {};
						quoteNote["createdById"] = StoreService.getUser().userId;
						quoteNote["note"] = $scope.note;
						quoteNote["createdByUserCode"] = StoreService.getUser().userCode;
						quoteNote["auditMessage"] = "Added Quote Note '"+$scope.note+"'";
						formData["quoteNote"] = quoteNote;
						AjaxUtil.submitData("/awacp/saveQuoteNote", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Quote Note Detail Added Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								qnoteVm.getQuoteNotes();
							}, 1000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "QuoteNoteCtrl::qnoteVm.addQuoteNote::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
				}
			});
			return defer.promise;
		}		
		qnoteVm.pageChanged = function() {
			qnoteVm.getQuoteNotes();
		};		
		qnoteVm.cancelQuoteNote = function(){
		}
		qnoteVm.deleteQuoteNote = function(id){
			AjaxUtil.getData("/awacp/deleteQuoteNote/"+id, Math.random())
			.success(function(data, status, headers){
				qnoteVm.totalItems = (qnoteVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Quote Note Detail Deleted Successfully.')
					.then(function (){qnoteVm.getQuoteNotes();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Quote Note Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "QuoteNoteCtrl::qnoteVm.editQuoteNote::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}		
		qnoteVm.editQuoteNote = function (id, title){
			var defer = $q.defer();
			var modalInstance = $uibModal.open({
				animation: true,
				size: "md",
				templateUrl: 'templates/qnote-add.html',
				windowClass:'alert-zindex ',
				controller: function ($scope, $uibModalInstance){
					$scope.quoteNote = {};
					$scope.title = title;
					$scope.note = "";
					$scope.message = "";
					$scope.save = function (){
						if(!$scope.note || $scope.note.length <= 0){
							$scope.message = "Please Enter Quote Note Detail.";
							return;
						}
						jQuery(".actions").attr('disabled','disabled');
						jQuery(".spinner").css('display','block');
						var formData = {};
						$scope.quoteNote.note = $scope.note;
						$scope.quoteNote.updatedById = StoreService.getUser().userId;
						$scope.quoteNote.updatedByUserCode = StoreService.getUser().userCode;
						$scope.quoteNote.auditMessage = "Updated Quote Note '"+$scope.note+"'";
						formData["quoteNote"] = $scope.quoteNote;
						AjaxUtil.submitData("/awacp/updateQuoteNote", formData)
						.success(function(data, status, headers){
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							$scope.message = "Quote Note Detail Updated Successfully";
							$timeout(function(){
								$scope.message = "";
								modalInstance.dismiss();
								qnoteVm.getQuoteNotes();
							}, 1000);							
							return;
						})
						.error(function(jqXHR, textStatus, errorThrown){
							$scope.message = "";
							jQuery(".actions").removeAttr('disabled');
							jQuery(".spinner").css('display','none');
							jqXHR.errorSource = "QuoteNoteCtrl::qnoteVm.updateQuoteNote::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						});						
					};
					$scope.cancel = function (){						
						modalInstance.dismiss();
						defer.reject();
					};
					$scope.editQuoteNote = function(id){
						AjaxUtil.getData("/awacp/getQuoteNote/"+id, Math.random())
						.success(function(data, status, headers){
							if(data && data.quoteNote){
								$scope.quoteNote = data.quoteNote;
								$scope.note = data.quoteNote.note;
							} 
						})
						.error(function(jqXHR, textStatus, errorThrown){
							jqXHR.errorSource = "QuoteNoteCtrl::qnoteVm.editQuoteNote::Error";
							AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
						})
					}
					$scope.editQuoteNote(id);
				}
			});
			return defer.promise;
		}	
		qnoteVm.getQuoteNotes = function(){
			qnoteVm.quoteNotes = [];
			qnoteVm.pageNumber = qnoteVm.currentPage;
			AjaxUtil.getData("/awacp/listQuoteNotes/"+qnoteVm.pageNumber+"/"+qnoteVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						qnoteVm.totalItems = data.stsResponse.totalCount;
					});
				}
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
						});					
					} else {
					    tmp.push(data.stsResponse.results);
					}
					$scope.$apply(function(){
						qnoteVm.quoteNotes = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteNoteCtrl::qnoteVm.getQuoteNotes::Error";
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



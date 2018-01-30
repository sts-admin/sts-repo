(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('FactoryClaimsCtrl', FactoryClaimsCtrl);
	FactoryClaimsCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService', 'FileService'];
	function FactoryClaimsCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService, FileService){
		var fcVm = this;
		fcVm.action = "Add";
		fcVm.fcObSearchDone = false;
		fcVm.fcEdit = false;
		fcVm.orderBook = {};
	    
		$scope.timers = [];
		fcVm.claims= [];
		fcVm.factoryClaim = {};
		fcVm.contractors = [];
		fcVm.truckers = [];
		
		fcVm.totalItems = -1;
		fcVm.currentPage = 1;
		fcVm.pageNumber = 1;
		fcVm.pageSize = 20;
		fcVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		
		fcVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					fcVm.updateFileUploadCount(source, sourceId, filePattern);
				}
			});
		}
		
		fcVm.updateFileUploadCount = function(source, sourceId, docType){
			if(fcVm.claims && fcVm.claims.length > 0){
				for(var i = 0; i < fcVm.claims.length; i++){
					if(fcVm.claims[i].id === sourceId){
						if(source.includes("FC_PDF")){
							fcVm.claims[i].pdfDocCount = (parseInt(fcVm.claims[i].pdfDocCount) + 1);
						}			
						break;
					}
				}
			}
		}
		fcVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("FACTORY_CLAIM", size, function(status, size){
				if("success" === status){
					fcVm.pageSize = size;
					fcVm.pageChanged();
				}
			});
		}
		
		fcVm.getPageSize = function(){
			AjaxUtil.getPageSize("FACTORY_CLAIM", function(status, size){
				if("success" === status){
					fcVm.pageSize = size;
				}
			});
		}
		fcVm.getTruckers = function(){
			fcVm.truckers = [];
			fcVm.pageNumber = fcVm.currentPage;
			AjaxUtil.getData("/awacp/listTruckers/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					fcVm.totalItems = data.stsResponse.totalCount;
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
					fcVm.truckers = tmp;
					$scope.$digest();
				}		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "FactoryClaimCtrl::fcVm.getTruckers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		fcVm.getContractors = function(callback){
			fcVm.contractors = [];
			fcVm.pageNumber = fcVm.currentPage;
			AjaxUtil.getData("/awacp/listContractors/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						fcVm.totalItems = data.stsResponse.totalCount;
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
					fcVm.contractors = tmp;
					$scope.$digest();
				}
				if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
					callback(fcVm.contractors, "success");
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "FactoryClaimCtrl::fcVm.getContractors::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		fcVm.listFactoryClaims = function(src){
			fcVm.claims= [];
			AjaxUtil.getData("/awacp/listFactoryClaims/"+fcVm.pageNumber+"/"+fcVm.pageSize, Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
							tmp.push(v);
						});					
					} else {
					    tmp.push(data.stsResponse.results);
					}
					fcVm.claims = tmp;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "FactoryClaimCtrl::fcVm.listFactoryClaims::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		fcVm.saveClaim = function(){
			jQuery(".fc-add-action").attr('disabled','disabled');
			jQuery("#fc-add-spinner").css('display','block');	
			var update = false;
			var message = "Factory Claim Detail Created Successfully.";
			if(fcVm.factoryClaim && fcVm.factoryClaim.id){
				message = "Factory Claim Detail Updated Successfully";
				fcVm.factoryClaim.updatedByUserCode = StoreService.getUser().userCode;
				fcVm.factoryClaim.updatedById = StoreService.getUserId();
				fcVm.factoryClaim.auditMessage = "Updated Factory Claim for Order number # '"+ fcVm.factoryClaim.orderBookNumber + "'";
				update = true;
			}else{
				fcVm.factoryClaim.createdByUserCode = StoreService.getUser().userCode;
				fcVm.factoryClaim.createdById = StoreService.getUserId();
				fcVm.factoryClaim.auditMessage = "Created Factory Claim for Order number # '"+ fcVm.factoryClaim.orderBookNumber + "'";
			}
			var formData = {};
			fcVm.factoryClaim.userNameOrEmail = StoreService.getUserName();
			formData["factoryClaim"] = fcVm.factoryClaim;
			AjaxUtil.submitData("/awacp/saveFactoryClaim", formData)
			.success(function(data, status, headers){
				jQuery(".fc-add-action").removeAttr('disabled');
				jQuery("#fc-add-spinner").css('display','none');
				fcVm.factoryClaim = {};
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){$state.go('cfactories');},function (){return false;});
				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".fc-add-action").removeAttr('disabled');
				jQuery("#fc-add-spinner").css('display','none');
				jqXHR.errorSource = "FactoryClaimCtrl::fcVm.saveClaim::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		fcVm.editClaim = function(id){
			fcVm.fcObSearchDone = false;
			fcVm.fcEdit = false;
			AjaxUtil.getData("/awacp/getFactoryClaim/"+id, Math.random())
			.success(function(data, status, headers){
				fcVm.fcEdit = true;
				if(data && data.factoryClaim){										
					fcVm.factoryClaim = data.factoryClaim;
				}
				$scope.$digest();
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "FactoryClaimCtrl::fcVm.editClaim::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		fcVm.searchOrderBook = function(){
			if(fcVm.orderBook.orderBookNumber){
				var obn = fcVm.orderBook.orderBookNumber;
				fcVm.orderBook = {};
				fcVm.fcObSearchDone = false;
				fcVm.fcEdit = false;
				AjaxUtil.getData("/awacp/getOrderBookByNumber/"+obn, Math.random())
				.success(function(data, status, headers){
					fcVm.fcObSearchDone = true;
					if(data && data.orderBook){										
						fcVm.orderBook = data.orderBook;
						fcVm.factoryClaim.orbf = fcVm.orderBook.orbf;
						fcVm.factoryClaim.orderBookNumber = fcVm.orderBook.orderBookNumber;
						fcVm.factoryClaim.userCode = fcVm.orderBook.createdByUserCode;
						fcVm.factoryClaim.contractorId = fcVm.orderBook.contractorId;
						fcVm.factoryClaim.salesmanId = fcVm.orderBook.salesPersonId;
					}
					$scope.$digest();
					fcVm.init();
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "FactoryClaimCtrl::fcVm.searchOrderBook::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
				
			}else{
				AlertService.showAlert(	'AWACP :: Alert!', 'Please enter Order Book Number')
					.then(function (){return false;},function (){return false;});
			}
			
		}
		fcVm.pageChanged = function() {
			fcVm.listFactoryClaims();
		}
		fcVm.init = function(){
			fcVm.getContractors(function(data, status){
				if("success" === status){
					fcVm.getTruckers();
				}
			});
		}
		fcVm.getPageSize();
	}		
})();



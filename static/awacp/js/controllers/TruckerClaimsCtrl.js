(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('TruckerClaimsCtrl', TruckerClaimsCtrl);
	TruckerClaimsCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', '$uibModal', 'StoreService', 'FileService'];
	function TruckerClaimsCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, $uibModal, StoreService, FileService){
		var tcVm = this;
		tcVm.action = "Add";
		tcVm.tcObSearchDone = false;
		tcVm.tcEdit = false;
		tcVm.orderBook = {};
	    
		$scope.timers = [];
		tcVm.claims= [];
		tcVm.truckerClaim = {};
		tcVm.contractors = [];
		tcVm.truckers = [];
		
		tcVm.totalItems = -1;
		tcVm.currentPage = 1;
		tcVm.pageNumber = 1;
		tcVm.pageSize = 20;
		tcVm.pageSizeList = [20, 30, 40, 50, 60, 70, 80, 90, 100];
		
		tcVm.showFileListingView = function(source, sourceId, title, size, filePattern, viewSource){
			title = "File List";
			$rootScope.fileViewSource = "templates/file-listing.html";
			FileService.showFileViewDialog(source, sourceId, title, size, filePattern, viewSource, function(data, status){
				if("success" === status){
					tcVm.updateFileUploadCount(source, sourceId, filePattern);
				}
			});
		}
		tcVm.updateFileUploadCount = function(source, sourceId, docType){
			if(tcVm.claims && tcVm.claims.length > 0){
				for(var i = 0; i < tcVm.claims.length; i++){
					if(tcVm.claims[i].id === sourceId){
						if(source.includes("TC_PDF")){
							tcVm.claims[i].pdfDocCount = (parseInt(tcVm.claims[i].pdfDocCount) + 1);
						}			
						break;
					}
				}
			}
		}
		tcVm.setCurrentPageSize =function(size){
			AjaxUtil.setPageSize("TRUCKER_CLAIM", size, function(status, size){
				if("success" === status){
					tcVm.pageSize = size;
					tcVm.pageChanged();
				}
			});
		}
		
		tcVm.getPageSize = function(){
			AjaxUtil.getPageSize("TRUCKER_CLAIM", function(status, size){
				if("success" === status){
					tcVm.pageSize = size;
				}
			});
		}
		tcVm.getTruckers = function(){
			tcVm.truckers = [];
			tcVm.pageNumber = tcVm.currentPage;
			AjaxUtil.getData("/awacp/listTruckers/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					tcVm.totalItems = data.stsResponse.totalCount;
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
					tcVm.truckers = tmp;
					$scope.$digest();
				}		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TruckerClaimCtrl::tcVm.getTruckers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		tcVm.getContractors = function(callback){
			tcVm.contractors = [];
			tcVm.pageNumber = tcVm.currentPage;
			AjaxUtil.getData("/awacp/listContractors/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.totalCount){
					$scope.$apply(function(){
						tcVm.totalItems = data.stsResponse.totalCount;
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
					tcVm.contractors = tmp;
					$scope.$digest();
				}
				if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
					callback(tcVm.contractors, "success");
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TruckerClaimCtrl::tcVm.getContractors::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		tcVm.listTruckerClaims = function(src){
			tcVm.claims= [];
			AjaxUtil.getData("/awacp/listTruckerClaims/"+tcVm.pageNumber+"/"+tcVm.pageSize, Math.random())
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
					tcVm.claims = tmp;
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TruckerClaimCtrl::tcVm.listTruckerClaims::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		tcVm.saveClaim = function(){
			jQuery(".tc-add-action").attr('disabled','disabled');
			jQuery("#tc-add-spinner").css('display','block');	
			var update = false;
			var message = "Trucker Claim Detail Created Successfully";
			if(tcVm.truckerClaim && tcVm.truckerClaim.id){
				message = "Trucker Claim Detail Updated Successfully";
				tcVm.truckerClaim.updatedById = StoreService.getUser().userId;
				tcVm.truckerClaim.updatedByUserCode = StoreService.getUser().userCode;
				tcVm.truckerClaim.auditMessage = "Added Trucker Claim for Order Book # '"+ tcVm.truckerClaim.orderBookNumber+"'";
				update = true;
			}else{
				tcVm.truckerClaim.createdByUserCode = StoreService.getUser().userCode;
				tcVm.truckerClaim.createdById = StoreService.getUserId();
				tcVm.truckerClaim.auditMessage = "Updated Trucker Claim for Order Book # '"+ tcVm.truckerClaim.orderBookNumber+"'";
			}
			var formData = {};
			tcVm.truckerClaim.userNameOrEmail = StoreService.getUserName();
			formData["truckerClaim"] = tcVm.truckerClaim;
			AjaxUtil.submitData("/awacp/saveTruckerClaim", formData)
			.success(function(data, status, headers){
				jQuery(".tc-add-action").removeAttr('disabled');
				jQuery("#tc-add-spinner").css('display','none');
				tcVm.truckerClaim = {};
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){$state.go('claim-truckers');},function (){return false;});
					
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jQuery(".tc-add-action").removeAttr('disabled');
				jQuery("#tc-add-spinner").css('display','none');
				jqXHR.errorSource = "TruckerClaimCtrl::tcVm.saveClaim::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		tcVm.editClaim = function(id){
			tcVm.tcObSearchDone = false;
			tcVm.tcEdit = false;
			AjaxUtil.getData("/awacp/getTruckerClaim/"+id, Math.random())
			.success(function(data, status, headers){
				tcVm.tcEdit = true;
				if(data && data.truckerClaim){										
					tcVm.truckerClaim = data.truckerClaim;
				}
				$scope.$digest();
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "TruckerClaimCtrl::tcVm.editClaim::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		tcVm.searchOrderBook = function(){
			if(tcVm.orderBook.orderBookNumber){
				var obn = tcVm.orderBook.orderBookNumber;
				tcVm.orderBook = {};
				tcVm.tcObSearchDone = false;
				tcVm.tcEdit = false;
				AjaxUtil.getData("/awacp/getOrderBookByNumber/"+obn, Math.random())
				.success(function(data, status, headers){
					tcVm.tcObSearchDone = true;
					if(data && data.orderBook){										
						tcVm.orderBook = data.orderBook;
						tcVm.truckerClaim.orbf = tcVm.orderBook.orbf;
						tcVm.truckerClaim.orderBookNumber = tcVm.orderBook.orderBookNumber;
						tcVm.truckerClaim.userCode = tcVm.orderBook.createdByUserCode;
						tcVm.truckerClaim.contractorId = tcVm.orderBook.contractorId;
						tcVm.truckerClaim.salesmanId = tcVm.orderBook.salesPersonId;
					}
					$scope.$digest();
					tcVm.init();
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "TruckerClaimCtrl::tcVm.searchOrderBook::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				});
				
			}else{
				AlertService.showAlert(	'AWACP :: Alert!', 'Please enter Order Book Number')
					.then(function (){return false;},function (){return false;});
			}
			
		}
		tcVm.pageChanged = function() {
			tcVm.listTruckerClaims();
		}
		tcVm.init = function(){
			tcVm.getContractors(function(data, status){
				if("success" === status){
					tcVm.getTruckers();
				}
			});
		}
		tcVm.getPageSize();
	}		
})();



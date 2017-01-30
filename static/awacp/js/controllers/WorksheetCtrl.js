(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('WorksheetCtrl', WorksheetCtrl);
	WorksheetCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function WorksheetCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var wsVm = this;
		wsVm.action = "Add";
		$scope.timers = [];
		wsVm.disableAction = false;
		wsVm.specialCase = true;
		wsVm.specialMultiplier = 27;
		
		wsVm.manufacturers = [];
		wsVm.quoteNotes = [];
		wsVm.products = [];
		wsVm.pdnis = [];
		wsVm.multipliers = [];
		
		
		wsVm.productItems = [] //Used to collect selected Products
		wsVm.sPdnis = [{}] //Used to collect selected Pdnis
		wsVm.sNotes = [{}] //Used to collect selected Notes
		
		wsVm.quote = {};
		wsVm.manufacturerItems = [{id:1}];
		wsVm.worksheet = {};
		
		wsVm.wsAction = "Save Worksheet";
		wsVm.saveOrUpdateWorksheet = function(){
			alert(JSON.stringify(wsVm.worksheet, null, 4));
		}
		wsVm.listManufaturers = function(){
			wsVm.manufacturers = [];
			AjaxUtil.getData("/awacp/listMnDs/-1/-1", Math.random())
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
					$scope.$apply(function(){
						wsVm.manufacturers = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ManufactureCtrl::wsVm.listManufaturers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		wsVm.listNotes = function(){
			wsVm.quoteNotes = [];
			AjaxUtil.getData("/awacp/listQuoteNotes/-1/-1", Math.random())
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
					$scope.$apply(function(){
						wsVm.quoteNotes = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "QuoteNoteCtrl::wsVm.getQuoteNotes::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		wsVm.listProducts = function(){
			wsVm.products = [];
			AjaxUtil.getData("/awacp/listProducts/-1/-1", Math.random())
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
					$scope.$apply(function(){
						wsVm.products = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "ProductCtrl::wsVm.listProducts::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		wsVm.listPdnis = function(){
			wsVm.pdnis = [];
			AjaxUtil.getData("/awacp/listPdnis/-1/-1", Math.random())
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
					$scope.$apply(function(){
						wsVm.pdnis = tmp;
					});
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "PdniCtrl::wsVm.listProducts::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		wsVm.addPdni = function(){
			wsVm.sPdnis.push({});
		}
		wsVm.removePdni = function(){
			if(wsVm.sPdnis.length <= 0) return;
			wsVm.sPdnis.splice(wsVm.sPdnis.length-1, 1);
		}
		wsVm.addNewProduct = function(index, blockId){
			var manufacturerItems, productItems;
			if(!wsVm.worksheet.manufacturerItems){	
				wsVm.worksheet["manufacturerItems"] = {};
				wsVm.worksheet.manufacturerItems[blockId]={};		
				wsVm.worksheet.manufacturerItems[blockId].productItems={};
			}
			wsVm.worksheet.manufacturerItems[blockId].productItems[index] = {quantity:0, listAmount:0, netAmount:0};	
			wsVm.productItems.push({});
			/*wsVm.doCalculation(blockId, wsVm.productItems);*/
		}
		wsVm.removeNewProduct = function(index, blockId){
			if(wsVm.productItems.length <= 0) return;
			wsVm.productItems.splice(index, 1);
			var tmp = {};
			var ind = -1;
			angular.forEach(wsVm.worksheet.manufacturerItems[blockId].productItems, function(v, k){
				if(index != k){
					tmp[++ind] = v;
				}
			});
			wsVm.worksheet.manufacturerItems[blockId].productItems = tmp;
			wsVm.doCalculation(blockId);			
		}
		wsVm.addWorksheetInfoBlock = function(){
			wsVm.manufacturerItems.push({});
		}
		wsVm.removeWorksheetInfoBlock = function(){
			if(wsVm.manufacturerItems.length <= 0) return;
			wsVm.manufacturerItems.splice(wsVm.manufacturerItems.length-1, 1);
		}
		wsVm.addWorksheetNote = function(){
			wsVm.sNotes.push({});
		}
		wsVm.removeWorksheetNote = function(){
			if(wsVm.sNotes.length <= 0) return;
			wsVm.sNotes.splice(wsVm.sNotes.length-1, 1);
		}
		wsVm.cancelWorksheetAction = function(){
			$state.go("quotes");
		}
		wsVm.getQuoteDetail = function(){
			if($state.params.takeoffId != undefined){
				AjaxUtil.getData("/awacp/getTakeoffWithDetail/"+$state.params.takeoffId, Math.random())
				.success(function(data, status, headers){					
					if(data && data.takeoff){
						wsVm.quote = data.takeoff;
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "WorksheetrCtrl::wsVm.getQuoteDetail::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		wsVm.sumListAmount = function(blockId, arr){			
			var amount = 0, tmp;
			angular.forEach(wsVm.worksheet.manufacturerItems[blockId].productItems, function(value, key){
				tmp = Number(value.listAmount);
				if(tmp === 'NaN'){
					return false;
				}
				amount = amount + Number(value.listAmount) ; 
			});
			angular.element("#listTotal-"+blockId).val(amount);
			angular.element("#percentAmount-"+blockId).val(amount);
		}
		wsVm.sumNetAmount = function(blockId, arr){			
			var amount = 0, tmp;
			angular.forEach(wsVm.worksheet.manufacturerItems[blockId].productItems, function(value, key){
				tmp = Number(value.netAmount);
				if(tmp === 'NaN'){
					return false;
				}
				amount = amount + Number(value.netAmount) ; 
			});
			angular.element("#netTotal-"+blockId).val(amount);
			angular.element("#netTotal2-"+blockId).val(amount);
		}
		wsVm.doCalculation = function(blockId){
			wsVm.sumListAmount(blockId);
			wsVm.sumNetAmount(blockId);
		}
		wsVm.getMultiplier = function(amount){
			if(Number(amount) === 'NaN'){
				return -1;
			}
			var amt = Number(amount);
			
			if(amt > 0 && amt <= 3000){
				return 0.5;
			} else if(amt > 3000 && amt <= 10000){
				return 0.42;
			}else if(amt > 10000 && amt <= 17000){
				return 0.4;
			}else if(amt > 17000 && amt <= 25000){
				return 0.38;
			}else if(amt > 25000 && amt <= 40000){
				return 0.36;
			}else if(amt > 40000){
				return 0.33;
			}
			return 0;
		}
		
		wsVm.initWorksheet = function(){
			wsVm.addNewProduct(0, 0);
			
			wsVm.getQuoteDetail();	
			wsVm.listManufaturers();
			wsVm.listPdnis();
			wsVm.listProducts();
			wsVm.listNotes();
			if($state.params.id != undefined){
				wsVm.editWorksheet();
			}	
			wsVm.multipliers = [];			
			for(var i = 1; i <= 99; i++){
				wsVm.multipliers.push({multiplier:i/100});
			}
		}
		
		wsVm.editWorksheet = function(){
			if($state.params.id != undefined){
				AjaxUtil.getData("/awacp/getWorksheet/"+$state.params.id, Math.random())
				.success(function(data, status, headers){
					if(data && data.worksheet){
						$scope.$apply(function(){
							wsVm.worksheet = data.wsVm.getQuoteDetail();;	
							wsVm.wsAction = wsVm.worksheet && wsVm.worksheet.id?"Update Worksheet":"Save Worksheet";							
						});
					}
				})
				.error(function(jqXHR, textStatus, errorThrown){
					jqXHR.errorSource = "WorksheetrCtrl::wsVm.editWorksheet::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				})
			}
		}
		wsVm.deleteWorksheet = function(id){
			AjaxUtil.getData("/awacp/deleteWorksheet/"+id, Math.random())
			.success(function(data, status, headers){
				wsVm.totalItems = (wsVm.totalItems - 1);
				AlertService.showAlert(	'AWACP :: Alert!', 'Worksheet Detail Deleted Successfully.')
					.then(function (){wsVm.getWorksheets();},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(666666 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Error!', "Unable to Delete Worksheet Detail.")
					.then(function (){return},function (){return});
					return;
				}
				jqXHR.errorSource = "WorksheetCtrl::wsVm.deleteWorksheet::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
		}
		
		
		wsVm.addWorksheet = function(){
			var message = "Worksheet Detail Created Successfully";
			var url = "/awacp/saveWorksheet";
			var update = false;
			if(wsVm.Worksheet && wsVm.Worksheet.id){
				message = "Worksheet Detail Updated Successfully";
				wsVm.Worksheet.updatedByUserCode = StoreService.getUser().userCode;
				url = "/awacp/updateWorksheet";
				update = true;
			}else{
				wsVm.Worksheet.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};			
			formData["worksheet"] = wsVm.worksheet;
			
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				if(update){
					AlertService.showAlert(	'AWACP :: Alert!', message)
					.then(function (){wsVm.cancelWorksheetAction();},function (){return false;});
					return;
				}else{
					AlertService.showConfirm(	'AWACP :: Alert!', message)
					.then(function (){return},function (){wsVm.cancelWorksheetAction();});
					return;
				}
				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				if(1002 == jqXHR.status){
					AlertService.showAlert(	'AWACP :: Alert!', "An Worksheet with this email ID already exist, please use a different email ID.")
					.then(function (){return},function (){return});
					return;
				}else{
					jqXHR.errorSource = "WorksheetCtrl::wsVm.addWorksheet::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
				}
			});
		}
		$scope.$on("$destroy", function(){
			for(var i = 0; i < $scope.timers.length; i++){
				$timeout.cancel($scope.timers[i]);
			}
		});		
		wsVm.initWorksheet();
		
	}		
})();



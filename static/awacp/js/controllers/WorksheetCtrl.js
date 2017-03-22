(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('WorksheetCtrl', WorksheetCtrl);
	WorksheetCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function WorksheetCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		var wsVm = this;
		wsVm.action = "Add";
		$scope.timers = [];
		
		wsVm.manufacturers = [];
		wsVm.quoteNotes = [];
		wsVm.products = [];
		wsVm.pdnis = [];
		wsVm.multipliers = [];
		wsVm.mndMultipliers = {};
		
		wsVm.productItems = [] //Used to collect selected Products
		wsVm.sPdnis = [{}] //Used to collect selected Pdnis
		wsVm.sNotes = [{}] //Used to collect selected Notes
		
		wsVm.quote = {};
		wsVm.worksheet = {};
		
		wsVm.wsAction = "Save Worksheet";
		
		wsVm.sendQuoteToBidders = function(worksheetId){
			AjaxUtil.getData("/awacp/sendEmailToBidders/"+worksheetId, Math.random())
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!', 'Quote sent to bidders successfully.')
					.then(function (){return false;},function (){return false;});
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "WorksheetCtrl::wsVm.sendQuoteToBidders::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		wsVm.getAppliedPercent = function(manufacturerId){
			var percent = 0;
			if(!wsVm.mndMultipliers || wsVm.mndMultipliers.length <= 0) return 0;
			angular.forEach(wsVm.mndMultipliers, function(v, k){
				if(v.id == manufacturerId){
					percent = v.wsMultiplier;
					return false;
				}
			});
			return percent;
		}
		
		wsVm.listManufaturers = function(){
			wsVm.manufacturers = [];
			wsVm.mndMultipliers = {};
			AjaxUtil.getData("/awacp/listMnDs/-1/-1", Math.random())
			.success(function(data, status, headers){
				if(data && data.stsResponse && data.stsResponse.results){
					var tmp = [];
					if(jQuery.isArray(data.stsResponse.results)) {
						jQuery.each(data.stsResponse.results, function(k, v){
							var mult = 0;
							if(v.wsMultiplier){
								mult = v.wsMultiplier;
							}
							wsVm.mndMultipliers[v.id] = {id:v.id, wsMultiplier:mult};
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
				jqXHR.errorSource = "WorksheetCtrl::wsVm.listManufaturers::Error";
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
				jqXHR.errorSource = "WorksheetCtrl::wsVm.listNotes::Error";
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
				jqXHR.errorSource = "WorksheetCtrl::wsVm.listProducts::Error";
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
				jqXHR.errorSource = "WorksheetCtrl::wsVm.listProducts::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		wsVm.addPdni = function(pdnis){
			pdnis.push({});
		}
		wsVm.removePdni = function(index, pdnis){
			pdnis.splice(index, 1);
		}
		wsVm.addNewProduct = function(productItems){			
			productItems.push({quantity:1, listAmount:0, netAmount:0});	
		}
		wsVm.removeProduct = function(index, manufacturerItem, productItems){
			productItems.splice(index, 1);
			wsVm.doCalculation(index, indexmanufacturerItem);			
		}
		wsVm.addWorksheetInfoBlock = function(){
			var length = (!wsVm.worksheet.manufacturerItems)? 0 : wsVm.worksheet.manufacturerItems.length;
			if(length <= 0){
				wsVm.worksheet["specialNotes"] = "";
				wsVm.worksheet["notes"] = [];
				wsVm.worksheet["manufacturerItems"] = [];	
				length = wsVm.worksheet.manufacturerItems.length;
			}
			wsVm.worksheet["notes"].push({});
			wsVm.worksheet["manufacturerItems"].push({multiplier: 0.5, freight:0, "pdnis": [{}], "productItems": [{quantity:1, listAmount:0, netAmount:0}]});
			
		}
		wsVm.removeWorksheetInfoBlock = function(index){
			if(wsVm.worksheet.manufacturerItems.length <= 0) return;
			wsVm.worksheet.manufacturerItems.splice(index, 1);
		}
		wsVm.addWorksheetNote = function(notes){
			notes.push({});
		}
		wsVm.removeWorksheetNote = function(notes){
			notes.splice(notes.length-1, 1);
		}
		wsVm.cancelWorksheetAction = function(){
			$state.go("quotes");
		}
		wsVm.getQuoteDetail = function(takeoffId){		
			AjaxUtil.getData("/awacp/getTakeoffWithDetail/"+takeoffId, Math.random())
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
		wsVm.sumListAmount = function(blockIndex, manufacturerItem, multiplier){
			var amount = 0, tmp;
			angular.forEach(manufacturerItem.productItems, function(value, key){
				tmp = Number(value.listAmount);
				if(tmp === 'NaN'){
					return false;
				}
				amount = amount + Number(value.listAmount) ; 
			});
			manufacturerItem.listTotal = amount;
			/*manufacturerItem.multPercentAmount = amount;*/
			manufacturerItem.multiplier = wsVm.getMultiplier(amount);
			if(multiplier && multiplier > 0){
				manufacturerItem.multiplier = multiplier;
			}
			var percent = 0, pAmount = 0;
			if(manufacturerItem.manufacturer){
				percent =wsVm.getAppliedPercent(manufacturerItem.manufacturer.id);
				manufacturerItem.percent = (manufacturerItem.listTotal + (manufacturerItem.listTotal * percent / 100));
			}else{
				manufacturerItem.percent = manufacturerItem.listTotal;
			}
			manufacturerItem.manufacturer.wsMultiplier = percent;
			manufacturerItem.multPercentAmount = manufacturerItem.percent * manufacturerItem.multiplier;
		}
		
		wsVm.sumNetAmount = function(blockIndex, manufacturerItem){			
			var amount = 0, tmp;
			angular.forEach( manufacturerItem.productItems, function(value, key){
				tmp = Number(value.netAmount);
				if(tmp === 'NaN'){
					return false;
				}
				amount = amount + Number(value.netAmount) ; 
			});
			manufacturerItem.netTotal = amount;		
			var mp = 0, np = 0, fp = 0;
			mp = Number(manufacturerItem.multPercentAmount);
			np = Number(manufacturerItem.netTotal);
			fp = Number(manufacturerItem.freight);
			manufacturerItem.totalAmount = mp + np + fp;	
			manufacturerItem.quoteAmount = manufacturerItem.totalAmount;
		}
		
		wsVm.doCalculation = function(blockIndex, manufacturerItem, multiplier){	
			wsVm.sumListAmount(blockIndex, manufacturerItem, multiplier);
			wsVm.sumNetAmount(blockIndex, manufacturerItem);			
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
			if($state.params.takeoffId != undefined){
				wsVm.getQuoteDetail($state.params.takeoffId);	
			}
			
			wsVm.listManufaturers();
			wsVm.listPdnis();
			wsVm.listProducts();
			wsVm.listNotes();
			if($state.params.takeoffId){
				wsVm.worksheet.takeoffId = $state.params.takeoffId;
			}
			if($state.params.worksheetId != undefined){
				wsVm.editWorksheet($state.params.worksheetId);
			}else{
				if(!wsVm.worksheet.manufacturerItems){
					wsVm.addWorksheetInfoBlock();
				}
				wsVm.worksheet.manufacturerItems[0].multiplier = 0.5;
			}
			wsVm.multipliers = [];			
			for(var i = 1; i <= 99; i++){
				wsVm.multipliers.push({multiplier:i/100});
			}
			
		}
		wsVm.setProductItems = function(mItem){
			var productItems = [];
			if(mItem.productItems){								
				if(jQuery.isArray(mItem.productItems)) {
					jQuery.each(mItem.productItems, function(k, v){
						productItems.push(v);
					});
				}else{
					productItems.push(mItem.productItems);
				}
			}else{
				productItems.push({});
			}
			mItem["productItems"] = productItems;
		}
		wsVm.setPdnis = function(mItem){
			var tmpPdnis = [];
			if(mItem.pdnis){								
				if(jQuery.isArray(mItem.pdnis)) {
					jQuery.each(mItem.pdnis, function(k, v){
						tmpPdnis.push(v);
					});
				}else{
					tmpPdnis.push(mItem.pdnis);
				}
			}else{
				tmpPdnis.push({});
			}
			mItem["pdnis"] = tmpPdnis;
		}
		
		wsVm.editWorksheet = function(worksheetId){		
			wsVm.worksheet["notes"] = [];
			wsVm.worksheet["manufacturerItems"] = [];
			AjaxUtil.getData("/awacp/getWorksheet/"+worksheetId, Math.random())
			.success(function(data, status, headers){				
				if(data && data.worksheet){	
					wsVm.worksheet.id = data.worksheet.id;
					wsVm.worksheet.takeoffId = data.worksheet.takeoffId;
					if(data.worksheet.grandTotal){
						wsVm.worksheet.grandTotal = data.worksheet.grandTotal;
					}
					if(data.worksheet.createdByUserCode){
						wsVm.worksheet.createdByUserCode = data.worksheet.createdByUserCode;
					}
					if(data.worksheet.updatedByUserCode){
						wsVm.worksheet.updatedByUserCode = data.worksheet.updatedByUserCode;
					}
				
					wsVm.getQuoteDetail(data.worksheet.takeoffId);
					if(data.worksheet.specialNotes){
						wsVm.worksheet.specialNotes = data.worksheet.specialNotes;
					}
					if(jQuery.isArray(data.worksheet.manufacturerItems)) {
						jQuery.each(data.worksheet.manufacturerItems, function(k, v){
							wsVm.setProductItems(v);
							wsVm.setPdnis(v);
							wsVm.worksheet.manufacturerItems.push(v);
						});
					}else{
						wsVm.setProductItems(data.worksheet.manufacturerItems);
						wsVm.setPdnis(data.worksheet.manufacturerItems);
						wsVm.worksheet.manufacturerItems.push(data.worksheet.manufacturerItems);
					}
					if(jQuery.isArray(data.worksheet.notes)) {
						jQuery.each(data.worksheet.notes, function(k, v){
							wsVm.worksheet.notes.push(v);
						});
					}else{
						wsVm.worksheet.notes.push(data.worksheet.notes);
					}
					wsVm.wsAction = "Update Worksheet";
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "WorksheetrCtrl::wsVm.editWorksheet::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			})
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
			var hasNotes = true;
			var hasPdnis = true;
			var hasManufacturer = true;
			var hasProduct = true;
			jQuery.each(wsVm.worksheet.notes, function(k, v){
				if(!v.id){
					hasNotes = false;
					return false;
				}
			});
			if(!hasNotes){
				AlertService.showAlert(	'AWACP :: Alert!', "Please add notes")
					.then(function (){return false;},function (){return false;});
				return;
			}
			jQuery.each(wsVm.worksheet.manufacturerItems, function(k, v){
				if(v.productItems){
					jQuery.each(v.productItems, function(k, d){
						if(!d.product || !d.product.id){
							hasProduct = false;
						}
					});
				}else{
					hasProduct = false;
				}
				if(v.pdnis){
					jQuery.each(v.pdnis, function(k, d){
						if(!d.id){
							hasPdnis = false;
						}
					});
				}else{
					hasPdnis = false;
				}
				if(v.manufacturer && v.manufacturer.id){
					hasManufacturer = true;
				}else{
					hasManufacturer = false;
				}
			});
			
			if(!hasManufacturer){
				AlertService.showAlert(	'AWACP :: Alert!', "Please select manufacturer")
					.then(function (){return false;},function (){return false;});
				return;
			}
			if(!hasProduct){
				AlertService.showAlert(	'AWACP :: Alert!', "Please select product")
					.then(function (){return false;},function (){return false;});
				return;
			}
			if(!hasPdnis){
				AlertService.showAlert(	'AWACP :: Alert!', "Please select P.D.N.I")
					.then(function (){return false;},function (){return false;});
				return;
			}
			var message = "Worksheet Detail Created Successfully";
			var url = "/awacp/saveWorksheet";
			var update = false;
			if(wsVm.worksheet && wsVm.worksheet.id){
				message = "Worksheet Detail Updated Successfully";
				wsVm.worksheet.updatedByUserCode = StoreService.getUser().userCode;
				update = true;
			}else{
				wsVm.worksheet.createdByUserCode = StoreService.getUser().userCode;
			}
			var formData = {};			
			formData["worksheet"] = wsVm.worksheet;
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){$state.go("quote-view");},function (){return false;});
				return;		
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "WorksheetCtrl::wsVm.addWorksheet::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
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



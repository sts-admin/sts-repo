(function() {
	'use strict';
	angular.module('awacpApp.controllers').controller('WorksheetCtrl', WorksheetCtrl);
	WorksheetCtrl.$inject = ['$scope', '$state', '$location', '$http', 'AjaxUtil', 'store', '$q', '$timeout', '$window', '$rootScope', '$interval', '$compile', 'AlertService', 'StoreService'];
	function WorksheetCtrl($scope, $state, $location, $http, AjaxUtil, store, $q, $timeout, $window, $rootScope, $interval, $compile, AlertService, StoreService){
		
		var wsVm = this;
		wsVm.action = "Add";
		$scope.timers = [];
		wsVm.actionClass = "btn btn-primary";
		
		wsVm.manufacturers = [];
		wsVm.quoteNotes = [];
		wsVm.products = [];
		wsVm.pdnis = [];
		wsVm.multipliers = [];
		wsVm.mndMultipliers = {};
		wsVm.quoteMailTrackers = [];
		
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
		wsVm.listProducts = function(mndId, callback){			
			wsVm.products = [];
			AjaxUtil.getData("/awacp/listMnDTypes/"+mndId+"/-1/-1", Math.random())
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
				if (typeof callback !== 'undefined' && jQuery.isFunction(callback)) {
					callback();
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
				wsVm.worksheet["notes"] = [{}];
				wsVm.worksheet["manufacturerItems"] = [];	
				length = wsVm.worksheet.manufacturerItems.length;
			}
			/*wsVm.worksheet["notes"].push({});*/
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
					if(wsVm.quote.bidders){
						var tmp = [];
						if(jQuery.isArray(wsVm.quote.bidders)) {							
							jQuery.each(wsVm.quote.bidders, function(k, v){
								tmp.push(v);
							});
						}else{
							tmp.push(wsVm.quote.bidders);
						}
						wsVm.quote.bidders = tmp;
					}
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "WorksheetrCtrl::wsVm.getQuoteDetail::Error";
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
		wsVm.sumListAmount = function(blockIndex, manufacturerItem, multiplier){
			var amount = 0, tmp;
			angular.forEach(manufacturerItem.productItems, function(value, key){
				tmp = Number(value.listAmount?value.listAmount:0);
				if(tmp === 'NaN'){
					return false;
				}
				amount = amount + Number(value.listAmount?value.listAmount:0) ; 
			});
			manufacturerItem.listTotal = amount;
			if(!manufacturerItem.manualMultiplierSet){
				if(multiplier && multiplier > 0){
					manufacturerItem.multiplier = multiplier;
					manufacturerItem.manualMultiplierSet = true;
				}else{
					var tot = (amount + (manufacturerItem.freight?manufacturerItem.freight:0));
					manufacturerItem.multiplier = wsVm.getMultiplier(tot);
				}
			}
			var percent = 0, pAmount = 0;
			if(manufacturerItem.manufacturer){
				percent =wsVm.getAppliedPercent(manufacturerItem.manufacturer.id);
				manufacturerItem.percent = (manufacturerItem.listTotal + (manufacturerItem.listTotal * percent / 100));
			}else{
				manufacturerItem.percent = manufacturerItem.listTotal;
			}
			manufacturerItem.manufacturer.wsMultiplier = percent;
			manufacturerItem.multPercentAmount = manufacturerItem.percent;
			if(manufacturerItem.multiplier && manufacturerItem.multiplier > 0){
				manufacturerItem.multPercentAmount = manufacturerItem.percent * manufacturerItem.multiplier;
			}			
		}
		
		wsVm.sumNetAmount = function(blockIndex, manufacturerItem){			
			var amount = 0, tmp;
			angular.forEach( manufacturerItem.productItems, function(value, key){
				tmp = Number(value.netAmount?value.netAmount:0);
				if(tmp === 'NaN'){
					return false;
				}
				amount = amount + Number(value.netAmount) ; 
			});
			manufacturerItem.netTotal = amount;		
			var mp = 0, np = 0, fp = 0;
			mp = Number(manufacturerItem.multPercentAmount?manufacturerItem.multPercentAmount:0);
			np = Number(manufacturerItem.netTotal?manufacturerItem.netTotal:0);
			fp = Number(manufacturerItem.freight?manufacturerItem.freight:0);
			manufacturerItem.totalAmount = mp + np + fp;
			manufacturerItem.quoteAmount = (parseFloat(manufacturerItem.totalAmount) * .88);
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
		wsVm.listQuoteMailTrackers = function(worksheetId, takeoffId){
			wsVm.quoteMailTrackers = [];
			AjaxUtil.getData("/awacp/listQuoteMailTrackers/"+worksheetId+"/"+takeoffId, Math.random())
			.success(function(data, status, headers){
				if(data && data.quoteMailTracker){
					var tmp = [];
					if(jQuery.isArray(data.quoteMailTracker)) {
						jQuery.each(data.quoteMailTracker, function(k, v){
							tmp.push(v);
						});					
					} else {
					    tmp.push(data.quoteMailTracker);
					}
					$scope.$apply(function(){
						wsVm.quoteMailTrackers = tmp;
					});
				}				
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "WorksheetCtrl::wsVm.listQuoteMailTrackers::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
		}
		
		wsVm.initWorksheet = function(){
			if($state.params.takeoffId != undefined){
				wsVm.getQuoteDetail($state.params.takeoffId);	
				wsVm.worksheet.takeoffId = $state.params.takeoffId;
				wsVm.addWorksheetInfoBlock(); // new worksheet: add empty worksheet info block to add data.
				wsVm.worksheet.manufacturerItems[0].multiplier = 0.5;
			}
			//Preview quote
			if(($state.params.prevWsId != undefined && $state.params.prevToId != undefined) || $state.params.officeWorksheetId != undefined){ // Preview Quote
				var worksheetId = $state.params.officeWorksheetId != undefined?$state.params.officeWorksheetId:$state.params.prevWsId;
				wsVm.editWorksheet(worksheetId);
				if($state.params.officeWorksheetId != undefined && $state.params.officeQuoteId != undefined){
					wsVm.listQuoteMailTrackers($state.params.officeWorksheetId, $state.params.officeQuoteId);
				}
			}else{ //Edit Worksheet
				wsVm.listManufaturers();
				wsVm.listPdnis();
				wsVm.listNotes();
				
				if($state.params.worksheetId != undefined){ //Open worksheet in edit mode with data pre-populated.
					wsVm.editWorksheet($state.params.worksheetId);
				}
				wsVm.multipliers = [];			
				for(var i = 1; i <= 99; i++){
					wsVm.multipliers.push({multiplier:i/100});
				}
			}
		}
		wsVm.setProductItems = function(mItem){
			wsVm.listProducts(mItem.manufacturer.id, function(){
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
			});
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
		wsVm.generateQuotePreview = function(worksheetId, takeoffId){
			wsVm.getQuoteDetail(takeoffId);
		}
		wsVm.previewQuote = function(worksheetId, takeoffId){
			/*var url = $state.href('quote-preview', {prevWsId: worksheetId, prevToId:takeoffId});
			$window.open(url,'_blank');*/
			$state.go('quote-preview', {prevWsId: worksheetId, prevToId:takeoffId});
		}
		wsVm.generatePdf = function(pdfViewWorksheetId){
			//Add authentication headers as params
			var accessToken = StoreService.getAccessToken();
			//Add authentication headers in URL
			var url = $rootScope.base + '/awacp/generatePdfUrl/'+pdfViewWorksheetId+'?'+Math.random();
			$http({
				url : url,
				method : 'GET',
				headers : {
					'Authorization' : 'Bearer ' + accessToken,
					'Accept' : 'application/json'
				}
			}).success(function(data){
				AlertService.showAlert(	'AWACP :: Message!', "Pdf of the quote generated successfully.")
					.then(function (){
						if(wsVm.worksheet.takeoffId){
							$state.go('quote-view-single', {qSource: wsVm.worksheet.takeoffId});
						}else{
							return;
						}
					},
					function (){
						return
					}
				);
			}).error(function(error){
				alert("Unable to generate PDF View, reason: "+ JSON.stringify(error, null, 4));
			});
		}
		wsVm.viewPDF = function(quote){
			$window.open(quote.pdfFilePath);
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
					wsVm.actionClass = "btn btn-success";
					$scope.$digest();
				}
			})
			.error(function(jqXHR, textStatus, errorThrown){
				jqXHR.errorSource = "WorksheetrCtrl::wsVm.editWorksheet::Error";
				AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
			});
			
		}
		wsVm.deleteWorksheet = function(id){
			AlertService.showConfirm(	'AWACP :: Confirmation!', "Are you sure to delete this worksheet?")
			.then(function (){				
				AjaxUtil.getData("/awacp/deleteWorksheet/"+id, Math.random())
				.success(function(data, status, headers){
					wsVm.totalItems = (wsVm.totalItems - 1);
					AlertService.showAlert(	'AWACP :: Alert!', 'Worksheet Detail Deleted Successfully.')
						.then(function (){$state.go('quote-view');},function (){return false;});
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
			},
			function (){
				return false;
			});			
		}
		
		
		wsVm.addWorksheet = function(){
			var hasNotes = false;
			var hasPdnis = false;
			var hasManufacturer = true;
			var hasProduct = true;
			if(wsVm.worksheet.notes){
				jQuery.each(wsVm.worksheet.notes, function(k, v){
				if(v && v.id){
						hasNotes = true;
					}else{
						wsVm.worksheet.notes.splice(k, 1);
					}
				});
			}
			
			if(!hasNotes){
				wsVm.worksheet.notes = [];
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
						if(d.id){
							hasPdnis = true;
						}else{
							v.pdnis.splice(k, 1);
						}
					});
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
				jQuery.each(wsVm.worksheet.manufacturerItems, function(k, v){
					v.pdnis = [];
				});
			}
			var message = "Worksheet Detail Created Successfully";
			var url = "/awacp/saveWorksheet";
			var update = false;
			if(wsVm.worksheet && wsVm.worksheet.id){
				message = "Worksheet Detail Updated Successfully";
				wsVm.worksheet.updatedByUserCode = StoreService.getUser().userCode;
				wsVm.worksheet.updatedById = StoreService.getUser().userId;
				wsVm.worksheet.auditMessage = "Updated Worksheet for takeoff# '"+ wsVm.worksheet.takeoffId + "'";
				update = true;
			}else{
				wsVm.worksheet.createdByUserCode = StoreService.getUser().userCode;
				wsVm.worksheet.createdById = StoreService.getUserId();
				wsVm.worksheet.auditMessage = "Created Worksheet for takeoff# '"+ wsVm.worksheet.takeoffId + "'";
			}
			var formData = {};			
			formData["worksheet"] = wsVm.worksheet;
			console.log(formData);
			AjaxUtil.submitData(url, formData)
			.success(function(data, status, headers){
				wsVm.wsAction = "Update Worksheet";
				AlertService.showAlert(	'AWACP :: Alert!', message)
				.then(function (){
					if(update){
						wsVm.editWorksheet(data.worksheet.id);
					}else{
						$state.go('worksheet-edit',{worksheetId: data.worksheet.id});
					}					
				},function (){return false;});
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



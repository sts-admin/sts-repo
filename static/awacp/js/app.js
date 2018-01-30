if (!String.prototype.startsWith) {
    String.prototype.startsWith = function (searchString, position) {
        position = position || 0;
        return this.substr(position, searchString.length) === searchString;
    };
}
(function() {
    'use strict';
	//Local env
	var base ="http://localhost:8080/awacpservices";
	var resourceReadPath = "http://localhost/tutorial/resource/";
	var basePath = "/tutorial/";
	//prod env
	/*var base ="http://awacptechnicalservices.com:8080/awacpservices";
	var resourceReadPath = "http://awacptechnicalservices.com/resource/";	
	var basePath = "/";*/
	//prod env
    angular.module('awacpApp', ['awacpApp.services', 'awacpApp.controllers','angular-storage','ui.router','checklist-model', 'angularMoment', 'ui.bootstrap', 'angularjs-dropdown-multiselect', 'ui.navbar', 'ui.bootstrap.tpls', 'ds.clock','ui.select', 'ngSanitize','ui-listView','ngFileUpload', 'angucomplete-alt', 'ui.tinymce', 'autoCompleteModule'])
		.constant("base", base).constant("resourceReadPath", resourceReadPath).constant("basePath", basePath)
		.config(function($stateProvider, $urlRouterProvider, $locationProvider) {
			$stateProvider				
			 .state('/',{
				url: '/',
				templateUrl:"templates/login.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				cache:false,
				requireAuth: false
			}).state('dashboard',{
				url: '/dashboard',
				templateUrl:"templates/dashboard.html",
				controller:"DashboardCtrl",
				controllerAs:"dashVm",
				requireAuth: true
			})
			.state('admin',{
				url: '/admin',
				templateUrl:"templates/dashboard-admin.html",
				controller:"AdminCtrl",
				controllerAs:"adminVm",
				requireAuth: true
			})
			.state('users',{
				url: '/users',
				templateUrl:"templates/users.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				requireAuth: true
			}).state('add-user',{
				url: '/manage/user/add',
				templateUrl:"templates/user-add.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				requireAuth: true
			}).state('edit-user',{
				url: '/manage/user/edit/:id',
				templateUrl:"templates/user-edit.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				requireAuth: true
			}).state('users-deleted',{
				url: '/manage/users/deleted',
				templateUrl:"templates/deleted-users.html",
				controller:"UserCtrl",
				controllerAs:"userVm",
				cache:false,
				requireAuth: true
			}).state('roles',{
				url: '/manage/roles',
				templateUrl:"templates/roles.html",
				controller:"RoleCtrl",
				controllerAs:"roleVm",
				requireAuth: true
			}).state('invmultipliers',{
				url: '/invmultipliers',
				templateUrl:"templates/inventory-multipliers.html",
				controller:"InvMultiplierCtrl",
				controllerAs:"invMultiVm",
				requireAuth: true
			}).state('quote-new-view',{
				url: '/quote/add',
				templateUrl:"templates/quotes-new.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm",
				requireAuth: true
			}).state('quote-view',{
				url: '/quotes',
				templateUrl:"templates/quotes.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm",
				requireAuth: true
			}).state('quote-view-single',{
				url: '/quotes/:qSource',
				templateUrl:"templates/quotes.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm",
				requireAuth: true
			}).state('quote-search',{
				url: '/quote/search',
				templateUrl:"templates/quote-search.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm",
				requireAuth: true
			}).state('takeoff-view',{
				url: '/takeoffs',
				templateUrl:"templates/takeoffs.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('takeoff-add',{
				url: '/takeoff/add',
				templateUrl:"templates/takeoff-add.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('takeoff-edit',{
				url: '/takeoff/edit/:id',
				templateUrl:"templates/takeoff-add.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('quote-edit',{
				url: '/quote/edit/:quoteEditId',
				templateUrl:"templates/takeoff-add.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('takeoff-search',{
				url: '/takeoff/search',
				templateUrl:"templates/takeoff-search.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('takeoff-reports',{
				url: '/takeoff/report-input',
				templateUrl:"templates/takeoff-report-input.html",
				controller:"TakeoffCtrl",
				controllerAs:"takeVm",
				requireAuth: true
			}).state('quote-reports',{
				url: '/quote/report-input',
				templateUrl:"templates/quote-report-input.html",
				controller:"QuoteCtrl",
				controllerAs:"qVm",
				requireAuth: true
			}).state('bidders',{
				url: '/bidders',
				templateUrl:"templates/bidders.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm",
				requireAuth: true
			})
			.state('bidder-edit',{
				url: '/bidder/edit/:id',
				templateUrl:"templates/bidder-add.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm",
				requireAuth: true
			}).state('bidder-add',{
				url: '/bidder/add',
				templateUrl:"templates/bidder-add.html",
				controller:"BidderCtrl",
				controllerAs:"bidVm",
				requireAuth: true
			}).state('engineers',{
				url: '/engineers',
				templateUrl:"templates/engineers.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm",
				requireAuth: true
			}).state('engineer-add',{
				url: '/engineer/add',
				templateUrl:"templates/engineer-add.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm",
				requireAuth: true
			}).state('engineer-edit',{
				url: '/engineer/edit/:id',
				templateUrl:"templates/engineer-add.html",
				controller:"EngineerCtrl",
				controllerAs:"engVm",
				requireAuth: true
			}).state('architects',{
				url: '/architects',
				templateUrl:"templates/architects.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm",
				requireAuth: true
			}).state('architect-add',{
				url: '/architect/add',
				templateUrl:"templates/architect-add.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm",
				requireAuth: true
			}).state('architect-edit',{
				url: '/architect/edit/:id',
				templateUrl:"templates/architect-add.html",
				controller:"ArchitectCtrl",
				controllerAs:"arcVm",
				requireAuth: true
			}).state('gcs',{
				url: '/gcs',
				templateUrl:"templates/gcs.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm",
				requireAuth: true
			}).state('gc-add',{
				url: '/gc/add',
				templateUrl:"templates/gc-add.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm",
				requireAuth: true
			}).state('gc-edit',{
				url: '/gc/edit/:id',
				templateUrl:"templates/gc-add.html",
				controller:"GeneralContractorCtrl",
				controllerAs:"gcVm",
				requireAuth: true
			}).state('contractors',{
				url: '/contractors',
				templateUrl:"templates/contractors.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm",
				requireAuth: true
			}).state('contractor-add',{
				url: '/contractor/add',
				templateUrl:"templates/contractor-add.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm",
				requireAuth: true
			}).state('contractor-edit',{
				url: '/contractor/edit/:id',
				templateUrl:"templates/contractor-add.html",
				controller:"ContractorCtrl",
				controllerAs:"conVm",
				requireAuth: true
			}).state('specifications',{
				url: '/specifications',
				templateUrl:"templates/specifications.html",
				controller:"SpecificationCtrl",
				controllerAs:"specVm",
				requireAuth: true
			}).state('ships',{
				url: '/ships',
				templateUrl:"templates/shipto.html",
				controller:"ShipToCtrl",
				controllerAs:"shipToVm",
				requireAuth: true
			}).state('claim-truckers',{
				url: '/truckerClaims',
				templateUrl:"templates/trucker-claims.html",
				controller:"TruckerClaimsCtrl",
				controllerAs:"tcVm",
				requireAuth: true
			}).state('trucker-claim-add',{
				url: '/truckerClaim/add',
				templateUrl:"templates/trucker-claim-add.html",
				controller:"TruckerClaimsCtrl",
				controllerAs:"tcVm",
				requireAuth: true
			}).state('cfactories',{
				url: '/factoryClaims',
				templateUrl:"templates/factory-claims.html",
				controller:"FactoryClaimsCtrl",
				controllerAs:"fcVm",
				requireAuth: true
			}).state('factory-claim-add',{
				url: '/factoryClaim/add',
				templateUrl:"templates/factory-claim-add.html",
				controller:"FactoryClaimsCtrl",
				controllerAs:"fcVm",
				requireAuth: true
			}).state('truckers',{
				url: '/truckers',
				templateUrl:"templates/truckers.html",
				controller:"TruckerCtrl",
				controllerAs:"truckerVm",
				requireAuth: true
			}).state('trucker-edit',{
				url: '/trucker/edit/:id',
				templateUrl:"templates/trucker-add.html",
				controller:"TruckerCtrl",
				controllerAs:"truckerVm",
				requireAuth: true
			}).state('trucker-add',{
				url: '/trucker/add',
				templateUrl:"templates/trucker-add.html",
				controller:"TruckerCtrl",
				controllerAs:"truckerVm",
				requireAuth: true
			}).state('products',{
				url: '/products',
				templateUrl:"templates/products.html",
				controller:"ProductCtrl",
				controllerAs:"prodVm",
				requireAuth: true
			}).state('pndis',{
				url: '/pdnis',
				templateUrl:"templates/pdnis.html",
				controller:"PdniCtrl",
				controllerAs:"pdniVm",
				requireAuth: true
			}).state('qnotes',{
				url: '/qnotes',
				templateUrl:"templates/qnotes.html",
				controller:"QuoteNotesCtrl",
				controllerAs:"qnoteVm",
				requireAuth: true
			}).state('manufactures',{
				url: '/manufactures',
				templateUrl:"templates/manufactures.html",
				controller:"ManufactureCtrl",
				controllerAs:"manuVm",
				requireAuth: true
			}).state('mtypes',{
				url: '/mtypes/:id',
				templateUrl:"templates/mtypes.html",
				controller:"MTypeCtrl",
				controllerAs:"mTypeVm",
				requireAuth: true
			}).state('iships',{
				url: '/iships',
				templateUrl:"templates/itemships.html",
				controller:"ItemShipCtrl",
				controllerAs:"itemVm",
				requireAuth: true
			}).state('vships',{
				url: '/vships',
				templateUrl:"templates/shippedvia.html",
				controller:"ShippedViaCtrl",
				controllerAs:"shipViaVm",
				requireAuth: true
			}).state('j-view',{
				url: '/j-view',
				templateUrl:"templates/jinventories.html",
				controller:"JInventoryCtrl",
				controllerAs:"jinvVm",
				requireAuth: true
			}).state('j-add',{
				url: '/j-add',
				templateUrl:"templates/jinventory-add.html",
				controller:"JInventoryCtrl",
				controllerAs:"jinvVm",
				requireAuth: true
			}).state('j-edit',{
				url: '/j-edit/:id',
				templateUrl:"templates/jinventory-add.html",
				controller:"JInventoryCtrl",
				controllerAs:"jinvVm",
				requireAuth: true
			}).state('j-orders',{
				url: '/j/orders',
				templateUrl:"templates/j-orders.html",
				controller:"JInventoryCtrl",
				controllerAs:"jinvVm",
				requireAuth: true
			}).state('spl-view',{
				url: '/spl-view',
				templateUrl:"templates/spl-inventories.html",
				controller:"SplInventoryCtrl",
				controllerAs:"splInvVm",
				requireAuth: true
			}).state('spl-add',{
				url: '/spl-add',
				templateUrl:"templates/spl-inventory-add.html",
				controller:"SplInventoryCtrl",
				controllerAs:"splInvVm",
				requireAuth: true
			}).state('spl-edit',{
				url: '/spl-edit/:id',
				templateUrl:"templates/spl-inventory-add.html",
				controller:"SplInventoryCtrl",
				controllerAs:"splInvVm",
				requireAuth: true
			}).state('spl-orders',{
				url: '/spl/orders',
				templateUrl:"templates/spl-orders.html",
				controller:"SplInventoryCtrl",
				controllerAs:"splInvVm",
				requireAuth: true
			}).state('sbc-view',{
				url: '/sbc-view',
				templateUrl:"templates/sbc-inventories.html",
				controller:"SbcInventoryCtrl",
				controllerAs:"sbcInvVm",
				requireAuth: true
			}).state('sbc-orders',{
				url: '/sbc/orders',
				templateUrl:"templates/sbc-orders.html",
				controller:"SbcInventoryCtrl",
				controllerAs:"sbcInvVm",
				requireAuth: true
			}).state('sbc-add',{
				url: '/sbc-add',
				templateUrl:"templates/sbc-inventory-add.html",
				controller:"SbcInventoryCtrl",
				controllerAs:"sbcInvVm",
				requireAuth: true
			}).state('sbc-edit',{
				url: '/sbc-edit/:id',
				templateUrl:"templates/sbc-inventory-add.html",
				controller:"SbcInventoryCtrl",
				controllerAs:"sbcInvVm",
				requireAuth: true
			}).state('awf-view',{
				url: '/awf-view',
				templateUrl:"templates/awf-inventories.html",
				controller:"AwfInventoryCtrl",
				controllerAs:"awfInvVm",
				requireAuth: true
			}).state('awf-add',{
				url: '/awf-add',
				templateUrl:"templates/awf-inventory-add.html",
				controller:"AwfInventoryCtrl",
				controllerAs:"awfInvVm",
				requireAuth: true
			}).state('awf-edit',{
				url: '/awf-edit/:id',
				templateUrl:"templates/awf-inventory-add.html",
				controller:"AwfInventoryCtrl",
				controllerAs:"awfInvVm",
				requireAuth: true
			}).state('awf-orders',{
				url: '/awf/orders',
				templateUrl:"templates/awf-orders.html",
				controller:"AwfInventoryCtrl",
				controllerAs:"awfInvVm",
				requireAuth: true
			}).state('aw-view',{
				url: '/aw-view',
				templateUrl:"templates/aw-inventories.html",
				controller:"AwInventoryCtrl",
				controllerAs:"awInvVm",
				requireAuth: true
			}).state('aw-add',{
				url: '/aw-add',
				templateUrl:"templates/aw-inventory-add.html",
				controller:"AwInventoryCtrl",
				controllerAs:"awInvVm",
				requireAuth: true
			}).state('aw-edit',{
				url: '/aw-edit/:id',
				templateUrl:"templates/aw-inventory-add.html",
				controller:"AwInventoryCtrl",
				controllerAs:"awInvVm",
				requireAuth: true
			}).state('aw-orders',{
				url: '/aw/orders',
				templateUrl:"templates/aw-orders.html",
				controller:"AwInventoryCtrl",
				controllerAs:"awInvVm",
				requireAuth: true
			}).state('deletefiles',{
				url: '/deletefiles',
				templateUrl:"templates/deletefiles.html",
				controller:"DeleteFileCtrl",
				controllerAs:"deleteVm",
				requireAuth: true
			}).state('marketing-templates',{
				url: '/marketing-templates',
				templateUrl:"templates/marketing-templates.html",
				controller:"MarketingTemplateCtrl",
				controllerAs:"mktTmpVm",
				requireAuth: true
			}).state('marketing-template-add',{
				url: '/marketing-template-add',
				templateUrl:"templates/marketing-template-add.html",
				controller:"MarketingTemplateCtrl",
				controllerAs:"mktTmpVm",
				requireAuth: true
			}).state('marketing-template-edit',{
				url: '/marketing-template-edit/:id',
				templateUrl:"templates/marketing-template-add.html",
				controller:"MarketingTemplateCtrl",
				controllerAs:"mktTmpVm",
				requireAuth: true
			}).state('office-worksheet',{
				url: '/office-worksheet/:officeWorksheetId/:officeQuoteId',
				templateUrl:"templates/office-worksheet.html",
				controller:"WorksheetCtrl",
				controllerAs:"wsVm",
				requireAuth: true
			}).state('worksheet-new',{
				url: '/worksheet-new/:takeoffId',
				templateUrl:"templates/worksheet.html",
				controller:"WorksheetCtrl",
				controllerAs:"wsVm",
				requireAuth: true
			}).state('worksheet-edit',{
				url: '/worksheet-edit/:worksheetId',
				templateUrl:"templates/worksheet.html",
				controller:"WorksheetCtrl",
				controllerAs:"wsVm",
				requireAuth: true,
				cache:false
			}).state('quote-preview',{
				url: '/quote-preview/:prevWsId/:prevToId',
				templateUrl:"templates/quote-preview.html",
				controller:"WorksheetCtrl",
				controllerAs:"wsVm",
				requireAuth: true,
				cache:false
			}).state('quote-follow',{
				url: '/quote-followup/:takeoffId/:worksheetId',
				templateUrl:"templates/quote-followup.html",
				controller:"QuoteFollowupCtrl",
				controllerAs:"qfVm",
				requireAuth: true,
				cache:false
			}).state('tax-entries',{
				url: '/tax-entries',
				templateUrl:"templates/tax-entries.html",
				controller:"TaxCtrl",
				controllerAs:"taxVm",
				requireAuth: true,
				cache:false
			}).state('invoice-generate',{
				url: '/invoice/:invoiceOrderId',
				templateUrl:"templates/invoice.html",
				controller:"InvoiceCtrl",
				controllerAs:"invoiceVm",
				requireAuth: true, 
				cache:false
			}).state('bill-add',{
				url: '/bill-add/:jobId/:orderNumber',
				templateUrl:"templates/bill.html",
				controller:"InvoiceCtrl",
				controllerAs:"invoiceVm",
				requireAuth: true, 
				cache:false
			}).state('bill-edit',{
				url: '/bill-edit/:jobId/:orderNumber/:invoiceId/:activeTabIndex',
				templateUrl:"templates/bill.html",
				controller:"InvoiceCtrl",
				controllerAs:"invoiceVm",
				requireAuth: true, 
				cache:false
			}).state('joborder-add',{
				url: '/joborder-add',
				templateUrl:"templates/joborder-add.html",
				controller:"JobOrderCtrl",
				controllerAs:"jobVm",
				requireAuth: true
			}).state('joborder-edit',{
				url: '/joborder-edit/:id',
				templateUrl:"templates/joborder-add.html",
				controller:"JobOrderCtrl",
				controllerAs:"jobVm",
				requireAuth: true,
				cache:false
			}).state('joborder-view',{
				url: '/joborders',
				templateUrl:"templates/job-orders.html",
				controller:"JobOrderCtrl",
				controllerAs:"jobVm",
				requireAuth: true
			}).state('joborder-view-single',{
				url: '/joborders/:oSource',
				templateUrl:"templates/job-orders.html",
				controller:"JobOrderCtrl",
				controllerAs:"jobVm",
				requireAuth: true
			}).state('joborder-reports',{
				url: '/joborder/report-input',
				templateUrl:"templates/joborder-report-input.html",
				controller:"JobOrderCtrl",
				controllerAs:"jobVm",
				requireAuth: true
			}).state('orderbook-reports',{
				url: '/orderbook/report-input',
				templateUrl:"templates/orderbook-report-input.html",
				controller:"OrderBookCtrl",
				controllerAs:"obVm",
				requireAuth: true
			}).state('orbf-edit',{
				url: '/orbf/edit/:orbfOrderBookId/:orbfJobOrderId',
				templateUrl:"templates/orbf.html",
				controller:"OrbfCtrl",
				controllerAs:"orbfVm",
				requireAuth: true
			}).state('tracking',{
				url: '/inv/tracking/:invOrderBookId/:invOrderItemId/:invType/:invOrderBookNumber',
				templateUrl:"templates/tracking-link.html",
				controller:"TrackingCtrl",
				controllerAs:"trackingVm",
				requireAuth: true
			}).state('factories',{
				url: '/factories',
				templateUrl:"templates/factories.html",
				controller:"FactoryCtrl",
				controllerAs:"facVm",
				requireAuth: true,
				cache:false
			}).state('orderbook-view',{
				url: '/orderbooks',
				templateUrl:"templates/order-books.html",
				controller:"OrderBookCtrl",
				controllerAs:"obVm",
				requireAuth: true,
				cache:false
			}).state('orderbook-add',{
				url: '/orderbook-add',
				templateUrl:"templates/orderbook-add.html",
				controller:"OrderBookCtrl",
				controllerAs:"obVm",
				requireAuth: true
			}).state('orderbook-edit',{
				url: '/orderbook-edit/:id',
				templateUrl:"templates/orderbook-add.html",
				controller:"OrderBookCtrl",
				controllerAs:"obVm",
				requireAuth: true,
				cache:false
			}).state('premium-order',{
				url: '/order/:inventory/:orderBookId',
				templateUrl:"templates/premium-order.html",
				controller:"PremiumOrderCtrl",
				controllerAs:"pOrderVm",
				requireAuth: true,
				cache:false
			}).state('fsr-trucker',{
				url: '/trucker/order/followup/:claimId',
				templateUrl:"templates/trucker-fsr.html",
				controller:"FsrCtrl",
				controllerAs:"fsrVm",
				requireAuth: true,
				cache:false
			}).state('fsr-factory',{
				url: '/factory/order/followup/:claimId',
				templateUrl:"templates/factory-fsr.html",
				controller:"FsrCtrl",
				controllerAs:"fsrVm",
				requireAuth: true,
				cache:false
			}).state('app-setting',{
				url: '/app/setting/:target',
				templateUrl:"templates/app-setting.html",
				controller:"AppSettingCtrl",
				controllerAs:"asVm",
				requireAuth: true,
				cache:false
			}).state('cleanup',{
				url: '/cleanup',
				templateUrl:"templates/admin-bbt.html",
				requireAuth: true,
				cache:false
			}).state('logs',{
				url: '/logs',
				templateUrl:"templates/logs.html",
				controller:"SystemLogCtrl",
				controllerAs:"sysVm",
				requireAuth: true,
				cache:false
			});
			// if none of the above states are matched, use this as the fallback
			$locationProvider.html5Mode(true);
			$urlRouterProvider.otherwise('/');
		}).run(function($rootScope, $state, store, $window, AjaxUtil, StoreService, $timeout, resourceReadPath, UserService, base, ChatService) {
			$rootScope.storeKeys = [];
			$rootScope.rightTrayClicked = false;
			$rootScope.openChatWindow = false;
			$rootScope.targetUser = {};
			$rootScope.myConversations = [];
			$rootScope.onlineUsers = [];
			$rootScope.offlineUsers = [];
			$rootScope.totalUsers = 0;
			$rootScope.listChatMessage = function(targetUser){
				$rootScope.myConversations = [];
				var myUserId = StoreService.getUserId();
				var otherUserId = targetUser.id;
				AjaxUtil.getData("/awacp/listMyConversationsWith/"+myUserId+"/"+otherUserId, Math.random())
				.success(function (data, status, headers) {						
					if(data && data.chatMessage){
						if(jQuery.isArray(data.chatMessage)){
							jQuery.each(data.chatMessage, function(k, v){
								if(v.sourceUserId == myUserId){
									v.msgSource = 'me';
									v.myMsg = v.message;
								}else{
									v.msgSource = 'other';
									v.otherMsg = v.message;
								}
								$rootScope.myConversations.push(v);
							});
						}else{
							if(data.chatMessage.sourceUserId == myUserId){
									data.chatMessage.msgSource = 'me';
									data.chatMessage.myMsg = data.chatMessage.message;
								}else{
									data.chatMessage.msgSource = 'other';
									data.chatMessage.otherMsg = data.chatMessage.message;
								}
							$rootScope.myConversations.push(data.chatMessage);
						}
					}
					$rootScope.$digest();
					
				})
				.error(function (jqXHR, textStatus, errorThrown) {
					jqXHR.errorSource = "ChatCtrl::openChatWindow::Error";
					AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);				
				});
			}
			
			$rootScope.listOnlineUsers = function(){
				$rootScope.onlineUsers = [];
				ChatService.listUsers("online", function(result, status){
					if("success" === status){
						$rootScope.onlineUsers = result;
						$rootScope.listOfflineUsers();	
					}
				});
			}
			$rootScope.listOfflineUsers = function(){
				$rootScope.offlineUsers = [];
				ChatService.listUsers("offline", function(result, status){
					if("success" === status){
						$rootScope.offlineUsers = result;
						$rootScope.totalUsers = parseInt($rootScope.onlineUsers.length + $rootScope.offlineUsers.length + 1);
						$rootScope.$digest();
					}
				});
			}
			$rootScope.toggleChatWindow = function(val, targetUser, isOnline){
				if(targetUser){
					targetUser.online = (isOnline == 'online'?true:false);
					$rootScope.targetUser = targetUser;	
					$rootScope.listChatMessage(targetUser);
					
				}else{
					$rootScope.targetUser = {};
				}
				$rootScope.openChatWindow = val;
				$rootScope.listOnlineUsers();
			}
			$rootScope.toggleRightTray = function(){
				$rootScope.rightTrayClicked = !$rootScope.rightTrayClicked;				
			}
			/*$rootScope.loadChartDetail = function(){
				alert("loadChartDetail");
			}*/
			$rootScope.fileViewSource = "templates/file-listing.html";
			$rootScope.gmtValue = 5.3;
			$rootScope.dayDiff = function(startdate, enddate) {
				var dayCount = 0;
				while(enddate >= startdate) {
					dayCount++;
					startdate.setDate(startdate.getDate() + 1);
				}
				return dayCount; 
			}
			$rootScope.currentDate = new Date();
			$rootScope.resourceReadPath = resourceReadPath;
			$rootScope.base = base;
			$rootScope.dateFormats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
			$rootScope.dateFormat = $rootScope.dateFormats[0];
			$rootScope.altInputFormats = ['M!/d!/yyyy'];
			var minDate = new Date();
			minDate.setMonth(minDate.getMonth() -6);
			$rootScope.dateOptions = { formatYear: 'yy', maxDate: new Date(2030, 5, 22), minDate: minDate, startingDay: 0, showWeeks: false};
			$rootScope.inlineOptions = { minDate: minDate, showWeeks: true };
			$rootScope.logoutUser =function(){
				UserService.logout();
			};			
			$rootScope.user = {userCode:StoreService.getUserCode(), isLoggedIn:StoreService.isLoggedIn(), userDisplayName:StoreService.userDisplayName(), role:StoreService.getRole()};
			$rootScope.alert = {noService:false};
			
			$rootScope.setUpUserMenu = function(){
				UserService.initializeMenu(function(jqXHR, status){
					if("success" === status){
						$rootScope.$apply(function(){
							$rootScope.menus = jqXHR;
						});
					}else{
						jqXHR.errorSource = "RootScope::setUpUserMenu::Error";
						AjaxUtil.saveErrorLog(jqXHR, "Unable to fulfil request due to communication error", true);
					}
				});		
			}
			if($rootScope.user.isLoggedIn){
				$rootScope.setUpUserMenu();
			}
			$rootScope.goBack= function() { 
			  $window.history.back();
			};
			$rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams) {
				/*alert("stateChangeStart, oldUrl = "+JSON.stringify(fromState, null, 4)+ ", newUrl = "+JSON.stringify(toState, null, 4)+", newUrl.requireAuth = " + toState.requireAuth + ", is user logged in = "+ $rootScope.user.isLoggedIn);*/
				if(fromState.url === "/" && (toState.url === "/dashboard" || toState.url === "/admin")){ //Login to administrator view or dashboard
					return;
				}else{
					if (toState.requireAuth && !$rootScope.user.isLoggedIn) {	
						StoreService.removeAll();
						$window.location.href = basePath;
					}
				}				
			});
		});
})();
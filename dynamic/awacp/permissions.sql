/*Takeoff permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('takeoff_c',0,1,CURRENT_TIMESTAMP,NULL,'Create New',NULL,1,'Takeoff', 'takeoff-add'),
('takeoff_r',0,1,CURRENT_TIMESTAMP,NULL,'View Takeoff',NULL,1,'Takeoff', 'takeoff-view'),
('takeoff_s',0,1,CURRENT_TIMESTAMP,NULL,'Search Takeoff',NULL,1,'Takeoff', 'takeoff-search'),
('takeoff_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Takeoff', NULL),
('takeoff_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Takeoff', NULL),
('takeoff_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Takeoff', 'takeoff-reports'),
('takeoff_new_email',0,1,CURRENT_TIMESTAMP,NULL,'New Takeoff Email',NULL,1,'Takeoff', NULL);
/*Takeoff permission end*/

/*Quote permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('quote_new_view',0,1,CURRENT_TIMESTAMP,NULL,'New Quote',NULL,1,'Quote', 'quote-new-view'),
('quote_c',0,1,CURRENT_TIMESTAMP,NULL,'Make Quote',NULL,1,'Quote', NULL),
('quote_r',0,1,CURRENT_TIMESTAMP,NULL,'View Quote',NULL,1,'Quote', 'quote-view'),
('quote_s',0,1,CURRENT_TIMESTAMP,NULL,'Search Quote',NULL,1,'Quote', 'quote-search'),
('quote_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Quote', NULL),
('quote_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Quote', NULL),
('quote_follow',0,1,CURRENT_TIMESTAMP,NULL,'Follow/Email',NULL,1,'Quote', 'quote-follow'),
('quote_send_follow',0,1,CURRENT_TIMESTAMP,NULL,'Send Followup',NULL,1,'Quote', null),
('quote_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Quote', 'quote-reports'),
('quote_undead',0,1,CURRENT_TIMESTAMP,NULL,'Undead Quote',NULL,1,'Quote', NULL),
('quote_bcc',0,1,CURRENT_TIMESTAMP,NULL,'Quote BCC',NULL,1,'Quote', NULL),
('quote_cc_uc',0,1,CURRENT_TIMESTAMP,NULL,'Quote CC To UC',NULL,1,'Quote', NULL),
('quote_cc_sp',0,1,CURRENT_TIMESTAMP,NULL,'Quote CC To SP',NULL,1,'Quote', NULL),
('quote_rev_email',0,1,CURRENT_TIMESTAMP,NULL,'Quote Rev Email',NULL,1,'Quote', NULL);
/*Quote permission end*/

/*Job Order permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('joborder_c',0,1,CURRENT_TIMESTAMP,NULL,'New Job Order',NULL,1,'Job Order','joborder-add'),
('joborder_r',0,1,CURRENT_TIMESTAMP,NULL,'View Job Order',NULL,1,'Job Order', 'joborder-view'),
('joborder_s',0,1,CURRENT_TIMESTAMP,NULL,'Search Job Order',NULL,1,'Job Order', 'joborder-search'),
('joborder_u',0,1,CURRENT_TIMESTAMP,NULL,'Update/Final',NULL,1,'Job Order', NULL),
('joborder_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Job Order', NULL),
('joborder_rollback',0,1,CURRENT_TIMESTAMP,NULL,'Roll Back',NULL,1,'Job Order', NULL),
('joborder_final_update',0,1,CURRENT_TIMESTAMP,NULL,'Job Final Update',NULL,1,'Job Order', NULL),
('joborder_invoice',0,1,CURRENT_TIMESTAMP,NULL,'Job Invoice',NULL,1,'Job Order', NULL),
('joborder_delete_invoice_email',0,1,CURRENT_TIMESTAMP,NULL,'Delete Invoice Email',NULL,1,'Job Order', NULL),
('joborder_monday_remind',0,1,CURRENT_TIMESTAMP,NULL,'Monday Remind',NULL,1,'Job Order', NULL),
('joborder_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Job Order', 'joborder-reports'),
('joborder_tax',0,1,CURRENT_TIMESTAMP,NULL,'Tax',NULL,1,'Job Order', NULL);
/*Job Order permission end*/

/*Order Book permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('orderbook_c',0,1,CURRENT_TIMESTAMP,NULL,'New Order',NULL,1,'Order Book', 'orderbook-add'),
('orderbook_r',0,1,CURRENT_TIMESTAMP,NULL,'View Order Book',NULL,1,'Order Book', 'orderbook-view'),
('orderbook_s',0,1,CURRENT_TIMESTAMP,NULL,'Search Order',NULL,1,'Order Book', 'orderbook-search'),
('orderbook_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Order Book', NULL),
('orderbook_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Order Book', NULL),
('orderbook_ship',0,1,CURRENT_TIMESTAMP,NULL,'Ship To',NULL,1,'Order Book', NULL),
('orderbook_orbf',0,1,CURRENT_TIMESTAMP,NULL,'ORBF',NULL,1,'Order Book', NULL),
('orderbook_remind',0,1,CURRENT_TIMESTAMP,NULL,'Est Remind',NULL,1,'Order Book', NULL),
('orderbook_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Order Book', 'orderbook-reports'),
('orderbook_add_reg',0,1,CURRENT_TIMESTAMP,NULL,'Add Reg',NULL,1,'Order Book', NULL),
('orderbook_add_aw',0,1,CURRENT_TIMESTAMP,NULL,'Add AW',NULL,1,'Order Book', NULL),
('orderbook_add_awf',0,1,CURRENT_TIMESTAMP,NULL,'Add AWF',NULL,1,'Order Book', NULL),
('orderbook_add_sbc',0,1,CURRENT_TIMESTAMP,NULL,'Add SBC',NULL,1,'Order Book', NULL),
('orderbook_add_spl',0,1,CURRENT_TIMESTAMP,NULL,'ADD SPL',NULL,1,'Order Book', NULL),
('orderbook_add_q',0,1,CURRENT_TIMESTAMP,NULL,'ADD Q',NULL,1,'Order Book', NULL),
('orderbook_add_j',0,1,CURRENT_TIMESTAMP,NULL,'ADD J',NULL,1,'Order Book', NULL),
('orderbook_change',0,1,CURRENT_TIMESTAMP,NULL,'Change UC',NULL,1,'Order Book', NULL);
/*Order Book permission end*/

/*AW permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('aw_c',0,1,CURRENT_TIMESTAMP,NULL,'New AW Inventory',NULL,1,'AW', 'aw-add'),
('aw_r',0,1,CURRENT_TIMESTAMP,NULL,'View AW Inventory',NULL,1,'AW', 'aw-view'),
('aw_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'AW', NULL),
('aw_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'AW', NULL),
('aw_orders',0,1,CURRENT_TIMESTAMP,NULL,'View AW Orders',NULL,1,'AW', 'aw-orders'),
('aw_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'AW', NULL);
/*AW permission end*/

/*AWF permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('awf_c',0,1,CURRENT_TIMESTAMP,NULL,'New AWF Inventory',NULL,1,'AWF', 'awf-add'),
('awf_r',0,1,CURRENT_TIMESTAMP,NULL,'View AWF Inventory',NULL,1,'AWF', 'awf-view'),
('awf_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'AWF', NULL),
('awf_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'AWF', NULL),
('awf_orders',0,1,CURRENT_TIMESTAMP,NULL,'View AWF Orders',NULL,1,'AWF', 'awf-orders'),
('awf_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'AWF', NULL);
/*AWF permission end*/

/*SBC permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('sbc_c',0,1,CURRENT_TIMESTAMP,NULL,'New SBC Inventory',NULL,1,'SBC', 'sbc-add'),
('sbc_r',0,1,CURRENT_TIMESTAMP,NULL,'View SBC Inventory',NULL,1,'SBC', 'sbc-view'),
('sbc_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'SBC', NULL),
('sbc_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'SBC', NULL),
('sbc_orders',0,1,CURRENT_TIMESTAMP,NULL,'View SBC Orders',NULL,1,'SBC', 'sbc-orders'),
('sbc_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'SBC', NULL);
/*SBC permission end*/

/*SPL permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('spl_c',0,1,CURRENT_TIMESTAMP,NULL,'New SPL Inventory',NULL,1,'SPL', 'spl-add'),
('spl_r',0,1,CURRENT_TIMESTAMP,NULL,'View SPL Inventory',NULL,1,'SPL', 'spl-view'),
('spl_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'SPL', NULL),
('spl_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'SPL', NULL),
('spl_orders',0,1,CURRENT_TIMESTAMP,NULL,'View SPL Orders',NULL,1,'SPL', 'spl-orders'),
('spl_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'SPL', NULL);
/*SPL permission end*/

/*J permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('j_c',0,1,CURRENT_TIMESTAMP,NULL,'New J Inventory',NULL,1,'J', 'j-add'),
('j_r',0,1,CURRENT_TIMESTAMP,NULL,'View J Inventory',NULL,1,'J', 'j-view'),
('j_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'J', NULL),
('j_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'J', NULL),
('j_orders',0,1,CURRENT_TIMESTAMP,NULL,'View J Orders',NULL,1,'J', 'j-orders'),
('j_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'J', NULL);
/*JOB permission end*/

/*Collection permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('c_r',0,1,CURRENT_TIMESTAMP,NULL,'Manage Collection',NULL,1,'Collection', 'collections');
/*Collection permission end*/

/*Marketing permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('marketing_template',0,1,CURRENT_TIMESTAMP,NULL,'Manage Template',NULL,1,'Marketing', 'marketing-templates'),
('marketing_mail',0,1,CURRENT_TIMESTAMP,NULL,'Send Mail',NULL,1,'Marketing', 'marketing-mails');
/*Marketing permission end*/

/*Claim permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('claim_trucker',0,1,CURRENT_TIMESTAMP,NULL,'Trucker',NULL,1,'Claim', 'claim-truckers'),
('claim_factory',0,1,CURRENT_TIMESTAMP,NULL,'Factory',NULL,1,'Claim', 'cfactories');
/*Claim permission end*/

/*BBT permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('bbt_manage',0,1,CURRENT_TIMESTAMP,NULL,'Manage',NULL,1,'BBT', NULL);
/*BBT permission end*/
/*Takeoff PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('takeoff_c',0,1,CURRENT_TIMESTAMP,NULL,'New Takeoff',NULL,1,'Takeoff', 'takeoff-add', 1, 1),
('takeoff_r',0,1,CURRENT_TIMESTAMP,NULL,'View Takeoff',NULL,1,'Takeoff', 'takeoff-view', 2, 1),
/*('takeoff_s',0,1,CURRENT_TIMESTAMP,NULL,'Search Takeoff',NULL,1,'Takeoff', 'takeoff-search', 3, 1),*/
('takeoff_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Takeoff', 'takeoff-reports', 4, 1),
('takeoff_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Takeoff', NULL, 5, 1),
('takeoff_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Takeoff', NULL, 6, 1),
('takeoff_new_email',0,1,CURRENT_TIMESTAMP,NULL,'New Takeoff Email',NULL,1,'Takeoff', NULL, 7, 1);
/*Takeoff PERMISSION end*/

/*Quote PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('quote_new_view',0,1,CURRENT_TIMESTAMP,NULL,'New Quote',NULL,1,'Quote', 'quote-new-view', 1, 1),
('quote_r',0,1,CURRENT_TIMESTAMP,NULL,'View Quote',NULL,1,'Quote', 'quote-view', 2, 1),
/*('quote_s',0,1,CURRENT_TIMESTAMP,NULL,'Search Quote',NULL,1,'Quote', 'quote-search', 3, 1),*/
('quote_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Quote', 'quote-reports', 4, 1),
/*('quote_follow',0,1,CURRENT_TIMESTAMP,NULL,'Quote Followup',NULL,1,'Quote', 'quote-follow', 5, 1),*/
('quote_c',0,1,CURRENT_TIMESTAMP,NULL,'Make Quote',NULL,1,'Quote', NULL, 6, 1),
('quote_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Quote', NULL, 7, 1),
('quote_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Quote', NULL, 8, 1),
('quote_send_follow',0,1,CURRENT_TIMESTAMP,NULL,'Send Followup',NULL,1,'Quote', NULL, 9, 1),
('quote_dead',0,1,CURRENT_TIMESTAMP,NULL,'Dead Quote',NULL,1,'Quote', NULL, 10, 1),
('quote_undead',0,1,CURRENT_TIMESTAMP,NULL,'Undead Quote',NULL,1,'Quote', NULL, 11, 1),
('quote_bcc',0,1,CURRENT_TIMESTAMP,NULL,'Quote BCC',NULL,1,'Quote', NULL, 12, 1),
('quote_cc_uc',0,1,CURRENT_TIMESTAMP,NULL,'Quote CC To UC',NULL,1,'Quote', NULL, 13, 1),
('quote_cc_sp',0,1,CURRENT_TIMESTAMP,NULL,'Quote CC To SP',NULL,1,'Quote', NULL, 14, 1),
('quote_rev_email',0,1,CURRENT_TIMESTAMP,NULL,'Quote Rev Email',NULL,1,'Quote', NULL, 15, 1);
/*Quote PERMISSION end*/

/*Job Order PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('joborder_c',0,1,CURRENT_TIMESTAMP,NULL,'New Job Order',NULL,1,'Job Order','joborder-add', 1, 1),
('joborder_r',0,1,CURRENT_TIMESTAMP,NULL,'View Job Order',NULL,1,'Job Order', 'joborder-view', 2, 1),
/*('joborder_s',0,1,CURRENT_TIMESTAMP,NULL,'Search Job Order',NULL,1,'Job Order', 'joborder-search', 3, 1),*/
('joborder_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Job Order', 'joborder-reports', 4, 1),
('joborder_u',0,1,CURRENT_TIMESTAMP,NULL,'Update/Final',NULL,1,'Job Order', NULL, 5, 1),
('joborder_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Job Order', NULL, 6, 1),
('joborder_rollback',0,1,CURRENT_TIMESTAMP,NULL,'Roll Back',NULL,1,'Job Order', NULL, 7, 1),
('joborder_final_update',0,1,CURRENT_TIMESTAMP,NULL,'Job Final Update',NULL,1,'Job Order', NULL, 8, 1),
('joborder_invoice',0,1,CURRENT_TIMESTAMP,NULL,'Job Invoice',NULL,1,'Job Order', NULL, 9, 1),
('joborder_delete_invoice_email',0,1,CURRENT_TIMESTAMP,NULL,'Delete Invoice Email',NULL,1,'Job Order', NULL, 10, 1),
('joborder_monday_remind',0,1,CURRENT_TIMESTAMP,NULL,'Monday Remind',NULL,1,'Job Order', NULL, 11, 1),
('joborder_tax',0,1,CURRENT_TIMESTAMP,NULL,'Tax',NULL,1,'Job Order', NULL, 12, 1);
/*Job Order PERMISSION end*/

/*Order Book PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('orderbook_c',0,1,CURRENT_TIMESTAMP,NULL,'New Order',NULL,1,'Order Book', 'orderbook-add', 1, 1),
('orderbook_r',0,1,CURRENT_TIMESTAMP,NULL,'View Order Book',NULL,1,'Order Book', 'orderbook-view', 2, 1),
/*('orderbook_s',0,1,CURRENT_TIMESTAMP,NULL,'Search Order',NULL,1,'Order Book', 'orderbook-search', 3, 1),*/
('orderbook_report',0,1,CURRENT_TIMESTAMP,NULL,'Order Reports',NULL,1,'Order Book', 'orderbook-reports', 4, 1),
('orderbook_add_reg',0,1,CURRENT_TIMESTAMP,NULL,'Reg Order',NULL,1,'Order Book', NULL, 5, 2),
('orderbook_add_aw',0,1,CURRENT_TIMESTAMP,NULL,'AW Order',NULL,1,'Order Book', NULL, 6, 2),
('orderbook_add_awf',0,1,CURRENT_TIMESTAMP,NULL,'AWF Order',NULL,1,'Order Book', NULL, 7, 2),
('orderbook_add_sbc',0,1,CURRENT_TIMESTAMP,NULL,'SBC Order',NULL,1,'Order Book', NULL, 8, 2),
('orderbook_add_spl',0,1,CURRENT_TIMESTAMP,NULL,'SPL Order',NULL,1,'Order Book', NULL, 9, 2),
('orderbook_j_view',0,1,CURRENT_TIMESTAMP,NULL,'J Order',NULL,1,'Order Book', NULL, 10, 2),

('orderbook_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Order Book', NULL, 11, 1),
('orderbook_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Order Book', NULL, 12, 1),
('orderbook_ship',0,1,CURRENT_TIMESTAMP,NULL,'Ship To',NULL,1,'Order Book', NULL, 13, 1),
('orderbook_orbf',0,1,CURRENT_TIMESTAMP,NULL,'ORBF',NULL,1,'Order Book', NULL, 14, 1),
('orderbook_remind',0,1,CURRENT_TIMESTAMP,NULL,'Est Remind',NULL,1,'Order Book', NULL, 15, 1),
('orderbook_add_q',0,1,CURRENT_TIMESTAMP,NULL,'ADD Q',NULL,1,'Order Book', NULL, 16, 1),
('orderbook_add_j',0,1,CURRENT_TIMESTAMP,NULL,'ADD J',NULL,1,'Order Book', NULL, 17, 1),
('orderbook_change',0,1,CURRENT_TIMESTAMP,NULL,'Change UC',NULL,1,'Order Book', NULL, 18, 1);
/*Order Book PERMISSION end*/

/*AW PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('aw_c',0,1,CURRENT_TIMESTAMP,NULL,'New AW Inventory',NULL,1,'AW', 'aw-add', 1, 1),
('aw_r',0,1,CURRENT_TIMESTAMP,NULL,'View AW Inventory',NULL,1,'AW', 'aw-view', 2, 1),
('aw_orders',0,1,CURRENT_TIMESTAMP,NULL,'View AW Orders',NULL,1,'AW', 'aw-orders', 3, 1),
('aw_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'AW', NULL, 4, 1),
('aw_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'AW', NULL, 5, 1),
('aw_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'AW', NULL, 6, 1);
/*AW PERMISSION end*/

/*AWF PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('awf_c',0,1,CURRENT_TIMESTAMP,NULL,'New AWF Inventory',NULL,1,'AWF', 'awf-add', 1, 1),
('awf_r',0,1,CURRENT_TIMESTAMP,NULL,'View AWF Inventory',NULL,1,'AWF', 'awf-view', 2, 1),
('awf_orders',0,1,CURRENT_TIMESTAMP,NULL,'View AWF Orders',NULL,1,'AWF', 'awf-orders', 3, 1),
('awf_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'AWF', NULL, 4, 1),
('awf_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'AWF', NULL, 5, 1),
('awf_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'AWF', NULL, 6, 1);
/*AWF PERMISSION end*/

/*SBC PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('sbc_c',0,1,CURRENT_TIMESTAMP,NULL,'New SBC Inventory',NULL,1,'SBC', 'sbc-add', 1, 1),
('sbc_r',0,1,CURRENT_TIMESTAMP,NULL,'View SBC Inventory',NULL,1,'SBC', 'sbc-view', 2, 1),
('sbc_orders',0,1,CURRENT_TIMESTAMP,NULL,'View SBC Orders',NULL,1,'SBC', 'sbc-orders', 3, 1),
('sbc_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'SBC', NULL, 4, 1),
('sbc_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'SBC', NULL, 5, 1),
('sbc_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'SBC', NULL, 6, 1);
/*SBC PERMISSION end*/

/*SPL PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('spl_c',0,1,CURRENT_TIMESTAMP,NULL,'New SPL Inventory',NULL,1,'SPL', 'spl-add', 1, 1),
('spl_r',0,1,CURRENT_TIMESTAMP,NULL,'View SPL Inventory',NULL,1,'SPL', 'spl-view', 2, 1),
('spl_orders',0,1,CURRENT_TIMESTAMP,NULL,'View SPL Orders',NULL,1,'SPL', 'spl-orders', 3, 1),
('spl_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'SPL', NULL, 4, 1),
('spl_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'SPL', NULL, 5, 1),
('spl_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'SPL', NULL, 6, 1);
/*SPL PERMISSION end*/

/*J PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('j_c',0,1,CURRENT_TIMESTAMP,NULL,'New J Inventory',NULL,1,'J', 'j-add', 1, 1),
('j_r',0,1,CURRENT_TIMESTAMP,NULL,'View J Inventory',NULL,1,'J', 'j-view', 2, 1),
('j_orders',0,1,CURRENT_TIMESTAMP,NULL,'View J Orders',NULL,1,'J', 'j-orders', 3, 1),
('j_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'J', NULL, 4, 1),
('j_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'J', NULL, 5, 1),
('j_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'J', NULL, 6, 1);
/*JOB PERMISSION end*/

/*Collection PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('c_r',0,1,CURRENT_TIMESTAMP,NULL,'Manage Collection',NULL,1,'Collection', 'collections', 1, 1);
/*Collection PERMISSION end*/

/*Marketing PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('marketing_template',0,1,CURRENT_TIMESTAMP,NULL,'Manage Template',NULL,1,'Marketing', 'marketing-templates', 1, 1),
('marketing_mail',0,1,CURRENT_TIMESTAMP,NULL,'Send Mail',NULL,1,'Marketing', 'marketing-mails', 2, 1);
/*Marketing PERMISSION end*/

/*Claim PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('claim_trucker',0,1,CURRENT_TIMESTAMP,NULL,'Trucker',NULL,1,'Claim', 'claim-truckers', 1, 1),
('claim_factory',0,1,CURRENT_TIMESTAMP,NULL,'Factory',NULL,1,'Claim', 'cfactories', 2, 1);
/*Claim PERMISSION end*/

/*BBT PERMISSION begin*/
INSERT INTO `PERMISSION`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`, `DISPLAYORDER`,`HIERARCHY`)
VALUES('bbt_manage_engineer',0,1,CURRENT_TIMESTAMP,NULL,'Manage Engineer',NULL,1,'BBT', 'engineers', 2, 1),
('bbt_manage_contractor',0,1,CURRENT_TIMESTAMP,NULL,'Manage Contractor',NULL,1,'BBT', 'contractors', 3, 1),
('bbt_manage_architecture',0,1,CURRENT_TIMESTAMP,NULL,'Manage Architect',NULL,1,'BBT', 'architects', 4, 1),
('bbt_manage_bidder',0,1,CURRENT_TIMESTAMP,NULL,'Manage Bidder',NULL,1,'BBT', 'bidders', 5, 1),
('bbt_manage_ships',0,1,CURRENT_TIMESTAMP,NULL,'Manage Ship To',NULL,1,'BBT', 'ships', 6, 1),
('bbt_manage_trucker',0,1,CURRENT_TIMESTAMP,NULL,'Manage Trucker',NULL,1,'BBT', 'truckers', 7, 1),
('bbt_manage_pndi',0,1,CURRENT_TIMESTAMP,NULL,'Manage PDNI',NULL,1,'BBT', 'pndis', 8, 1),
('bbt_manage_qnotes',0,1,CURRENT_TIMESTAMP,NULL,'Manage Quote Notes',NULL,1,'BBT', 'qnotes', 9, 1),
('bbt_manage_manufacture',0,1,CURRENT_TIMESTAMP,NULL,'Manage Manufacture & Description',NULL,1,'BBT', 'manufactures', 10, 1),
('bbt_manage_iships',0,1,CURRENT_TIMESTAMP,NULL,'Manage Item Shipped',NULL,1,'BBT', 'iships', 11, 1),
('bbt_manage_vships',0,1,CURRENT_TIMESTAMP,NULL,'Manage Shipped Via',NULL,1,'BBT', 'vships', 12, 1),
('bbt_manage_deletefiles',0,1,CURRENT_TIMESTAMP,NULL,'Delete File',NULL,1,'BBT', 'deletefiles', 13, 1),
('bbt_manage_specifications',0,1,CURRENT_TIMESTAMP,NULL,'Manage Specification',NULL,1,'BBT', 'specifications', 14, 1),
('bbt_manage_products',0,1,CURRENT_TIMESTAMP,NULL,'Manage Product',NULL,1,'BBT', 'products', 15, 1),		
('bbt_manage_gcs',0,1,CURRENT_TIMESTAMP,NULL,'Manage GCs',NULL,1,'BBT', 'gcs', 16, 1);
/*BBT PERMISSION end*/
/*Roles:begin*/
INSERT INTO `awacp`.`role`(`ROLENAME`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`UPDATEDBYID`,`VERSION`,`ROLEDESCRIPTION`)
VALUES('role_guest',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Guest'),
('role_user',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Employee/Executive'),
('role_puser',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Advance/Power User'),
('role_admin',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Administrator'),
('role_superadmin',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Super Administrator');
/*Roles:end*/

/*Takeoff permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('takeoff_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Takeoff', 'takeoff-add'),
('takeoff_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Takeoff', 'takeoff-view'),
('takeoff_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Takeoff', NULL),
('takeoff_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Takeoff', NULL),
('takeoff_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Takeoff', 'takeoff-reports'),
('takeoff_loginemail',0,1,CURRENT_TIMESTAMP,NULL,'Login Email',NULL,1,'Takeoff', NULL);
/*Takeoff permission end*/

/*Quote permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('quote_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Quote', 'quote-add'),
('quote_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Quote', 'quote-view'),
('quote_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Quote', NULL),
('quote_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Quote', NULL),
('quote_follow',0,1,CURRENT_TIMESTAMP,NULL,'Follow/Email',NULL,1,'Quote', 'quote-follow'),
('quote_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Quote', 'quote-reports'),
('quote_undead',0,1,CURRENT_TIMESTAMP,NULL,'Undead Quote',NULL,1,'Quote', NULL);
/*Quote permission end*/

/*Job Order permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('joborder_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Job Order','joborder-add'),
('joborder_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Job Order', 'joborder-view'),
('joborder_u',0,1,CURRENT_TIMESTAMP,NULL,'Update/Final',NULL,1,'Job Order', NULL),
('joborder_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Job Order', NULL),
('joborder_rollback',0,1,CURRENT_TIMESTAMP,NULL,'Roll Back',NULL,1,'Job Order', NULL),
('joborder_remind',0,1,CURRENT_TIMESTAMP,NULL,'Remind',NULL,1,'Job Order', NULL),
('joborder_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Job Order', 'joborder-reports'),
('joborder_tax',0,1,CURRENT_TIMESTAMP,NULL,'Tax',NULL,1,'Job Order', NULL);
/*Job Order permission end*/

/*Order Book permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('orderbook_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Order Book', 'orderbook-add'),
('orderbook_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Order Book', 'orderbook-view'),
('orderbook_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Order Book', NULL),
('orderbook_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Order Book', NULL),
('orderbook_ship',0,1,CURRENT_TIMESTAMP,NULL,'Ship To',NULL,1,'Order Book', NULL),
('orderbook_orbf',0,1,CURRENT_TIMESTAMP,NULL,'ORBF',NULL,1,'Order Book', NULL),
('orderbook_remind',0,1,CURRENT_TIMESTAMP,NULL,'Est Remind',NULL,1,'Order Book', NULL),
('orderbook_report',0,1,CURRENT_TIMESTAMP,NULL,'Reports',NULL,1,'Order Book', 'orderbook-reports'),
('orderbook_change',0,1,CURRENT_TIMESTAMP,NULL,'Change UC',NULL,1,'Order Book', NULL);
/*Order Book permission end*/

/*AW permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('aw_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'AW', 'aw-add'),
('aw_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'AW', 'aw-view'),
('aw_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'AW', NULL),
('aw_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'AW', NULL),
('aw_orders',0,1,CURRENT_TIMESTAMP,NULL,'View Orders',NULL,1,'AW', 'aw-orders'),
('aw_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'AW', NULL);
/*AW permission end*/

/*AWF permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('awf_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'AWF', 'awf-add'),
('awf_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'AWF', 'awf-view'),
('awf_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'AWF', NULL),
('awf_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'AWF', NULL),
('awf_orders',0,1,CURRENT_TIMESTAMP,NULL,'View Orders',NULL,1,'AWF', 'awf-orders'),
('awf_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'AWF', NULL);
/*AWF permission end*/

/*SBC permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('sbc_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'SBC', 'sbc-add'),
('sbc_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'SBC', 'sbc-view'),
('sbc_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'SBC', NULL),
('sbc_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'SBC', NULL),
('sbc_orders',0,1,CURRENT_TIMESTAMP,NULL,'View Orders',NULL,1,'SBC', 'sbc-orders'),
('sbc_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'SBC', NULL);
/*SBC permission end*/

/*SPL permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('spl_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'SPL', 'spl-add'),
('spl_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'SPL', 'spl-view'),
('spl_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'SPL', NULL),
('spl_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'SPL', NULL),
('spl_orders',0,1,CURRENT_TIMESTAMP,NULL,'View Orders',NULL,1,'SPL', 'spl-orders'),
('spl_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'SPL', NULL);
/*SPL permission end*/

/*J permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`, `URL`)
VALUES('j_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'J', 'j-add'),
('j_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'J', 'j-view'),
('j_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'J', NULL),
('j_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'J', NULL),
('j_orders',0,1,CURRENT_TIMESTAMP,NULL,'View Orders',NULL,1,'J', 'j-orders'),
('j_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'J', NULL);
/*JOB permission end*/

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
/*Roles:begin*/
INSERT INTO `awacp`.`role`(`ROLENAME`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`UPDATEDBYID`,`VERSION`,`ROLEDESCRIPTION`)
VALUES('role_guest',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Guest'),
('role_user',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Employee/Executive'),
('role_puser',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Advance/Power User'),
('role_admin',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Administrator'),
('role_superadmin',0,1,CURRENT_TIMESTAMP,NULL,NULL,1,'Super Administrator');
/*Roles:end*/

/*Takeoff permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('takeoff_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Takeoff'),
('takeoff_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Takeoff'),
('takeoff_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Takeoff'),
('takeoff_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Takeoff'),
('takeoff_loginemail',0,1,CURRENT_TIMESTAMP,NULL,'Login Email',NULL,1,'Takeoff');
/*Takeoff permission end*/

/*Quote permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('quote_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Quote'),
('quote_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Quote'),
('quote_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Quote'),
('quote_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Quote'),
('quote_follow',0,1,CURRENT_TIMESTAMP,NULL,'Follow/Email',NULL,1,'Quote'),
('quote_undead',0,1,CURRENT_TIMESTAMP,NULL,'Undead Quote',NULL,1,'Quote');
/*Quote permission end*/

/*Job Order permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('joborder_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Job Order'),
('joborder_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Job Order'),
('joborder_u',0,1,CURRENT_TIMESTAMP,NULL,'Update/Final',NULL,1,'Job Order'),
('joborder_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Job Order'),
('joborder_rollback',0,1,CURRENT_TIMESTAMP,NULL,'Roll Back',NULL,1,'Job Order'),
('joborder_remind',0,1,CURRENT_TIMESTAMP,NULL,'Remind',NULL,1,'Job Order'),
('joborder_tax',0,1,CURRENT_TIMESTAMP,NULL,'Tax',NULL,1,'Job Order');
/*Job Order permission end*/

/*Order Book permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('orderbook_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Order Book'),
('orderbook_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Order Book'),
('orderbook_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Order Book'),
('orderbook_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Order Book'),
('orderbook_ship',0,1,CURRENT_TIMESTAMP,NULL,'Ship To',NULL,1,'Order Book'),
('orderbook_orbf',0,1,CURRENT_TIMESTAMP,NULL,'ORBF',NULL,1,'Order Book'),
('orderbook_remind',0,1,CURRENT_TIMESTAMP,NULL,'Est Remind',NULL,1,'Order Book'),
('orderbook_change',0,1,CURRENT_TIMESTAMP,NULL,'Change UC',NULL,1,'Order Book');
/*Order Book permission end*/

/*AW permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('aw_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'AW'),
('aw_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'AW'),
('aw_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'AW'),
('aw_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'AW'),
('aw_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'AW');
/*AW permission end*/

/*AWF permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('awf_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'AWF'),
('awf_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'AWF'),
('awf_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'AWF'),
('awf_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'AWF'),
('awf_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'AWF');
/*AWF permission end*/

/*SBC permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('sbc_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'SBC'),
('sbc_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'SBC'),
('sbc_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'SBC'),
('sbc_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'SBC'),
('sbc_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'SBC');
/*SBC permission end*/

/*SPL permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('spl_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'SPL'),
('spl_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'SPL'),
('spl_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'SPL'),
('spl_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'SPL'),
('spl_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'SPL');
/*SPL permission end*/

/*JOB permission begin*/
INSERT INTO `awacp`.`permission`(`AUTHORITY`,`ARCHIVED`,`CREATEDBYID`,`DATECREATED`,`DATEUPDATED`,`DESCRIPTION`,`UPDATEDBYID`,`VERSION`,`LABEL`)
VALUES('job_c',0,1,CURRENT_TIMESTAMP,NULL,'Create/Add',NULL,1,'Job'),
('job_r',0,1,CURRENT_TIMESTAMP,NULL,'Read/View/Search',NULL,1,'Job'),
('job_u',0,1,CURRENT_TIMESTAMP,NULL,'Update',NULL,1,'Job'),
('job_d',0,1,CURRENT_TIMESTAMP,NULL,'Delete',NULL,1,'Job'),
('job_remind',0,1,CURRENT_TIMESTAMP,NULL,'Low Inv Remind',NULL,1,'Job');
/*JOB permission end*/
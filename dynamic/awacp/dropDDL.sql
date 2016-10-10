ALTER TABLE TWITTERPROFILE DROP FOREIGN KEY FK_TWITTERPROFILE_PHOTOID;
ALTER TABLE CONTRACTOR DROP FOREIGN KEY FK_CONTRACTOR_COUNTRY_ID;
ALTER TABLE CONTRACTOR DROP FOREIGN KEY FK_CONTRACTOR_STATE_ID;
ALTER TABLE ADDRESS DROP FOREIGN KEY FK_ADDRESS_COUNTRYID;
ALTER TABLE ADDRESS DROP FOREIGN KEY FK_ADDRESS_STATEID;
ALTER TABLE BIDDER DROP FOREIGN KEY FK_BIDDER_STATE_ID;
ALTER TABLE BIDDER DROP FOREIGN KEY FK_BIDDER_COUNTRY_ID;
ALTER TABLE WORKSHEET DROP FOREIGN KEY FK_WORKSHEET_STATEID;
ALTER TABLE WORKSHEET DROP FOREIGN KEY FK_WORKSHEET_COUNTRYID;
ALTER TABLE LINKEDINPROFILE DROP FOREIGN KEY FK_LINKEDINPROFILE_PHOTOID;
ALTER TABLE ENGINEER DROP FOREIGN KEY FK_ENGINEER_STATE_ID;
ALTER TABLE ENGINEER DROP FOREIGN KEY FK_ENGINEER_COUNTRY_ID;
ALTER TABLE PASSWORDRESETHISTORY DROP FOREIGN KEY FK_PASSWORDRESETHISTORY_USERID;
ALTER TABLE TAXENTRY DROP FOREIGN KEY FK_TAXENTRY_COUNTRYID;
ALTER TABLE TAXENTRY DROP FOREIGN KEY FK_TAXENTRY_STATEID;
ALTER TABLE GENERALCONTRACTOR DROP FOREIGN KEY FK_GENERALCONTRACTOR_STATE_ID;
ALTER TABLE GENERALCONTRACTOR DROP FOREIGN KEY FK_GENERALCONTRACTOR_COUNTRY_ID;
ALTER TABLE ARCHITECT DROP FOREIGN KEY FK_ARCHITECT_STATE_ID;
ALTER TABLE ARCHITECT DROP FOREIGN KEY FK_ARCHITECT_COUNTRY_ID;
ALTER TABLE FACEBOOKPROFILE DROP FOREIGN KEY FK_FACEBOOKPROFILE_PHOTOID;
ALTER TABLE USER DROP FOREIGN KEY FK_USER_PHOTOID;
ALTER TABLE USER DROP FOREIGN KEY FK_USER_ROLE_NAME;
ALTER TABLE STATE DROP FOREIGN KEY FK_STATE_COUNTRYID;
ALTER TABLE TAKEOFF_BIDDER DROP FOREIGN KEY FK_TAKEOFF_BIDDER_TAKEOFF;
ALTER TABLE TAKEOFF_BIDDER DROP FOREIGN KEY FK_TAKEOFF_BIDDER_BIDDER;
ALTER TABLE TAKEOFF_GC DROP FOREIGN KEY FK_TAKEOFF_GC_GC;
ALTER TABLE TAKEOFF_GC DROP FOREIGN KEY FK_TAKEOFF_GC_TAKEOFF;
ALTER TABLE ROLE_PERMISSION DROP FOREIGN KEY FK_ROLE_PERMISSION_ROLEID;
ALTER TABLE ROLE_PERMISSION DROP FOREIGN KEY FK_ROLE_PERMISSION_PERMISSIONID;
DROP TABLE TWITTERPROFILE;
DROP TABLE CONTRACTOR;
DROP TABLE ADDRESS;
DROP TABLE PRODUCT;
DROP TABLE COUNTRY;
DROP TABLE BIDDER;
DROP TABLE PERMISSION;
DROP TABLE CUSTOMER;
DROP TABLE SITECOLOR;
DROP TABLE USERMAILHISTORY;
DROP TABLE INVENTORY;
DROP TABLE TAKEOFF;
DROP TABLE ORDERBOOK;
DROP TABLE WORKSHEET;
DROP TABLE LINKEDINPROFILE;
DROP TABLE ROLE;
DROP TABLE ENGINEER;
DROP TABLE SITESETTING;
DROP TABLE PASSWORDRESETHISTORY;
DROP TABLE PRODUCTTYPE;
DROP TABLE TAXENTRY;
DROP TABLE COSTSHEET;
DROP TABLE SPECIFICATION;
DROP TABLE GENERALCONTRACTOR;
DROP TABLE NOTE;
DROP TABLE IMAGE;
DROP TABLE MARKETINGTEMPLE;
DROP TABLE ERRORLOG;
DROP TABLE ARCHITECT;
DROP TABLE FACEBOOKPROFILE;
DROP TABLE USER;
DROP TABLE INVOICE;
DROP TABLE STATE;
DROP TABLE TAKEOFF_BIDDER;
DROP TABLE TAKEOFF_GC;
DROP TABLE ROLE_PERMISSION;

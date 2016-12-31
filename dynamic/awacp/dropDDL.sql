ALTER TABLE TWITTERPROFILE DROP FOREIGN KEY FK_TWITTERPROFILE_PHOTOID;
ALTER TABLE ADDRESS DROP FOREIGN KEY FK_ADDRESS_COUNTRYID;
ALTER TABLE ADDRESS DROP FOREIGN KEY FK_ADDRESS_STATEID;
ALTER TABLE TAKEOFF DROP FOREIGN KEY FK_TAKEOFF_SPECID;
ALTER TABLE WORKSHEET DROP FOREIGN KEY FK_WORKSHEET_STATEID;
ALTER TABLE WORKSHEET DROP FOREIGN KEY FK_WORKSHEET_COUNTRYID;
ALTER TABLE LINKEDINPROFILE DROP FOREIGN KEY FK_LINKEDINPROFILE_PHOTOID;
ALTER TABLE PASSWORDRESETHISTORY DROP FOREIGN KEY FK_PASSWORDRESETHISTORY_USERID;
ALTER TABLE TAXENTRY DROP FOREIGN KEY FK_TAXENTRY_COUNTRYID;
ALTER TABLE TAXENTRY DROP FOREIGN KEY FK_TAXENTRY_STATEID;
ALTER TABLE TRUCKER DROP FOREIGN KEY FK_TRUCKER_LOGOID;
ALTER TABLE FACEBOOKPROFILE DROP FOREIGN KEY FK_FACEBOOKPROFILE_PHOTOID;
ALTER TABLE USER DROP FOREIGN KEY FK_USER_PHOTOID;
ALTER TABLE STATE DROP FOREIGN KEY FK_STATE_COUNTRYID;
ALTER TABLE TAKEOFF_BIDDER DROP FOREIGN KEY FK_TAKEOFF_BIDDER_TAKEOFF;
ALTER TABLE TAKEOFF_BIDDER DROP FOREIGN KEY FK_TAKEOFF_BIDDER_BIDDER;
ALTER TABLE TAKEOFF_GC DROP FOREIGN KEY FK_TAKEOFF_GC_GC;
ALTER TABLE TAKEOFF_GC DROP FOREIGN KEY FK_TAKEOFF_GC_TAKEOFF;
ALTER TABLE USER_PERMISSION DROP FOREIGN KEY FK_USER_PERMISSION_USERID;
ALTER TABLE USER_PERMISSION DROP FOREIGN KEY FK_USER_PERMISSION_PERMISSIONID;
DROP TABLE TWITTERPROFILE;
DROP TABLE CONTRACTOR;
DROP TABLE QUOTENOTE;
DROP TABLE ADDRESS;
DROP TABLE PRODUCT;
DROP TABLE SHIPTO;
DROP TABLE COUNTRY;
DROP TABLE JOBFOLLOWUP;
DROP TABLE BIDDER;
DROP TABLE PERMISSION;
DROP TABLE SBCINVENTORY;
DROP TABLE SHIPPEDVIA;
DROP TABLE SITECOLOR;
DROP TABLE USERMAILHISTORY;
DROP TABLE TAKEOFF;
DROP TABLE ORDERBOOK;
DROP TABLE JOBDOCUMENT;
DROP TABLE WORKSHEET;
DROP TABLE LINKEDINPROFILE;
DROP TABLE ITEMSHIPPED;
DROP TABLE ENGINEER;
DROP TABLE PDNI;
DROP TABLE SITESETTING;
DROP TABLE SPEC;
DROP TABLE FILE;
DROP TABLE MND;
DROP TABLE PASSWORDRESETHISTORY;
DROP TABLE PRODUCTTYPE;
DROP TABLE MNDTYPE;
DROP TABLE TAXENTRY;
DROP TABLE MARKETINGTEMPLATE;
DROP TABLE TRUCKER;
DROP TABLE COSTSHEET;
DROP TABLE GENERALCONTRACTOR;
DROP TABLE NOTE;
DROP TABLE JINVENTORY;
DROP TABLE AWINVENTORY;
DROP TABLE ERRORLOG;
DROP TABLE AWFINVENTORY;
DROP TABLE CLAIMDOCUMENT;
DROP TABLE ARCHITECT;
DROP TABLE FACEBOOKPROFILE;
DROP TABLE USER;
DROP TABLE INVOICE;
DROP TABLE SPLINVENTORY;
DROP TABLE JOBORDER;
DROP TABLE STATE;
DROP TABLE TAKEOFF_BIDDER;
DROP TABLE TAKEOFF_GC;
DROP TABLE USER_PERMISSION;

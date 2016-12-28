CREATE TABLE TWITTERPROFILE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, PHONENUMBER VARCHAR(255), TWITTERID VARCHAR(255), TWITTERTOKEN VARCHAR(255), UPDATEDBYID BIGINT, USERID BIGINT, USERNAME VARCHAR(255), VERIFIED TINYINT(1) default 0, VERSION BIGINT, PHOTOID BIGINT, PRIMARY KEY (ID));
CREATE TABLE CONTRACTOR (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(250), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100), FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE QUOTENOTE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, NOTE VARCHAR(100) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE ADDRESS (ID BIGINT AUTO_INCREMENT NOT NULL, APPARTMENT VARCHAR(255), ARCHIVED TINYINT(1) default 0, CITY VARCHAR(255), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, LATITUDE DOUBLE, LONGITUDE DOUBLE, PLACEID VARCHAR(255), STREET VARCHAR(255), UPDATEDBYID BIGINT, USERID BIGINT NOT NULL, VERSION BIGINT, ZIPCODE VARCHAR(255), COUNTRYID BIGINT NOT NULL, STATEID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE PRODUCT (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, PRODUCTNAME VARCHAR(100) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE SHIPTO (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, SHIPTOADDRESS VARCHAR(250) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE COUNTRY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, COUNTRYCODE VARCHAR(2) NOT NULL, COUNTRYNAME VARCHAR(25) NOT NULL, PRIMARY KEY (ID));
CREATE TABLE JOBFOLLOWUP (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, COMMENT VARCHAR(250) NOT NULL, CREATEDBYID BIGINT, DATECREATED DATETIME NOT NULL, DATEUPDATED DATETIME, JOBID VARCHAR(10) NOT NULL, NEXTREMINDDATE DATETIME NOT NULL, REMIDERDISMISSED TINYINT(1) default 0 NOT NULL, STATUS TINYINT(1) default 0 NOT NULL, UPDATEDBYID BIGINT, USERCODE VARCHAR(10) NOT NULL, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE BIDDER (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(250), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), PHONEOFFICE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE PERMISSION (AUTHORITY VARCHAR(30) NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, DESCRIPTION VARCHAR(35) NOT NULL, DISPLAYORDER INTEGER, HIERARCHY INTEGER, LABEL VARCHAR(255), UPDATEDBYID BIGINT, URL VARCHAR(255), VERSION BIGINT, PRIMARY KEY (AUTHORITY));
CREATE TABLE SBCINVENTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, BILLABLECOST DOUBLE, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, ITEM VARCHAR(255), QUANTITY INTEGER, REORDERQUANTITY INTEGER, SIZE DOUBLE, UNITPRICE DOUBLE, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE SHIPPEDVIA (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, SHIPPEDVIAADDRESS VARCHAR(100) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE SITECOLOR (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE USERMAILHISTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, content LONGTEXT NOT NULL, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, EVENT VARCHAR(255), UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE TAKEOFF (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHITECTUREID BIGINT, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, DRAWINGDATE DATETIME, DRAWINGRECEIVEDFROM VARCHAR(100), DUEDATE DATETIME, ENGINEERID BIGINT, JOBADDRESS VARCHAR(250) NOT NULL, JOBID VARCHAR(255), JOBNAME VARCHAR(100) NOT NULL, PROJECTNUMBER VARCHAR(25), QUOTEID VARCHAR(255), REVISEDDATE DATETIME, SALESPERSON BIGINT NOT NULL, TAKEOFFCOMMENT LONGTEXT, TAKEOFFID VARCHAR(255), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), USERCODE VARCHAR(10) NOT NULL, VERSION BIGINT, VIBROLAYIN VARCHAR(100), SPECID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE ORDERBOOK (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE JOBDOCUMENT (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, COLUMNVALUE VARCHAR(200) NOT NULL, CREATEDBYID BIGINT, DATECREATED DATETIME NOT NULL, DATEUPDATED DATETIME, DOCUMENTNAME VARCHAR(100) NOT NULL, DOCUMENTTYPE VARCHAR(4) NOT NULL, ORDERID VARCHAR(20) NOT NULL, UPDATEDBYID BIGINT, USERCODE VARCHAR(10) NOT NULL, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE WORKSHEET (ID BIGINT AUTO_INCREMENT NOT NULL, APPARTMENT VARCHAR(255), ARCHIVED TINYINT(1) default 0, CITY VARCHAR(255), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, LATITUDE DOUBLE, LONGITUDE DOUBLE, PLACEID VARCHAR(255), STREET VARCHAR(255), UPDATEDBYID BIGINT, USERID BIGINT, VERSION BIGINT, ZIPCODE VARCHAR(255), COUNTRYID BIGINT NOT NULL, STATEID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE LINKEDINPROFILE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, LINKEDINID VARCHAR(255), LINKEDINTOKEN VARCHAR(255), PHONENUMBER VARCHAR(255), UPDATEDBYID BIGINT, USERID BIGINT NOT NULL, USERNAME VARCHAR(255), VERIFIED TINYINT(1) default 0, VERSION BIGINT, PHOTOID BIGINT, PRIMARY KEY (ID));
CREATE TABLE ITEMSHIPPED (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, ITEM VARCHAR(250) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE ENGINEER (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(250), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100), FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE PDNI (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, PDNINAME VARCHAR(100) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE SITESETTING (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE SPEC (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, DETAIL VARCHAR(250) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE FILE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CONTENTTYPE VARCHAR(50) NOT NULL, CREATEDBYID BIGINT, CREATEDNAME VARCHAR(25) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EXTENSION VARCHAR(4) NOT NULL, FILESOURCE VARCHAR(255), FILESOURCEID BIGINT, ORIGINALNAME VARCHAR(255), OTHER VARCHAR(255), SIZE VARCHAR(255), UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE MND (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, PRODUCTNAME VARCHAR(100) NOT NULL, TYPE VARCHAR(255), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE PASSWORDRESETHISTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, NEWPASSWORD VARCHAR(255), OLDPASSWORD VARCHAR(255), OTP VARCHAR(255), UPDATEDBYID BIGINT, VERSION BIGINT, USERID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE PRODUCTTYPE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, COLOR INTEGER NOT NULL, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, MESSAGE VARCHAR(255), PRODUCTID BIGINT NOT NULL, PRODUCTTYPENAME VARCHAR(250) NOT NULL, RANGE1 DOUBLE NOT NULL, RANGE2 DOUBLE NOT NULL, RANGE3 DOUBLE NOT NULL, SPECMODEL VARCHAR(250), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE TAXENTRY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CITY VARCHAR(255), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, RATE DOUBLE, UPDATEDBYID BIGINT, VERSION BIGINT, COUNTRYID BIGINT NOT NULL, STATEID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE MARKETINGTEMPLATE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE TRUCKER (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CODE VARCHAR(20), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, LOGIN VARCHAR(20), NAME VARCHAR(100) NOT NULL, PASSWORD VARCHAR(100), PHONE VARCHAR(20), TRACKINGURL VARCHAR(255), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), LOGOID BIGINT, PRIMARY KEY (ID));
CREATE TABLE COSTSHEET (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE GENERALCONTRACTOR (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(250), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), PHONEOFFICE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE NOTE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE JINVENTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, BILLABLECOST DOUBLE, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, ITEM VARCHAR(255), QUANTITY INTEGER, REORDERQUANTITY INTEGER, SIZE DOUBLE, UNITPRICE DOUBLE, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE AWINVENTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, BILLABLECOST DOUBLE, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, ITEM VARCHAR(255), QUANTITY INTEGER, REORDERQUANTITY INTEGER, SIZE DOUBLE, UNITPRICE DOUBLE, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE ERRORLOG (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, ERRORCODE VARCHAR(255), errorData LONGTEXT, errorText LONGTEXT, serviceUrl LONGTEXT, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE AWFINVENTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, BILLABLECOST DOUBLE, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, ITEM VARCHAR(255), QUANTITY INTEGER, REORDERQUANTITY INTEGER, SIZE DOUBLE, UNITPRICE DOUBLE, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE CLAIMDOCUMENT (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, PRODUCTNAME VARCHAR(100) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE ARCHITECT (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(250), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100), FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE FACEBOOKPROFILE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FACEBOOKID VARCHAR(255), FACEBOOKTOKEN LONGTEXT, UPDATEDBYID BIGINT, USERID BIGINT, USERNAME VARCHAR(255), VERIFIED TINYINT(1) default 0, VERSION BIGINT, PHOTOID BIGINT, PRIMARY KEY (ID));
CREATE TABLE USER (ID BIGINT AUTO_INCREMENT NOT NULL, ABOUTME VARCHAR(255), ARCHIVED TINYINT(1) default 0, AVTARIMAGE VARCHAR(255), CONTACT VARCHAR(20), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FIRSTLOGIN TINYINT(1) default 0, FIRSTNAME VARCHAR(20) NOT NULL, GENDER VARCHAR(10), LASTNAME VARCHAR(20), MIDDLENAME VARCHAR(20), ONLINE TINYINT(1) default 0, PASSWORD VARCHAR(50), ROLE VARCHAR(20), UPDATEDBYID BIGINT, USERCODE VARCHAR(10), USERNAME VARCHAR(25) NOT NULL, VERIFICATIONCODE VARCHAR(255), VERIFIED TINYINT(1) default 0, VERSION BIGINT, PHOTOID BIGINT, PRIMARY KEY (ID));
CREATE TABLE INVOICE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE SPLINVENTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, BILLABLECOST DOUBLE, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, ITEM VARCHAR(255), QUANTITY INTEGER, REORDERQUANTITY INTEGER, SIZE DOUBLE, UNITPRICE DOUBLE, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE JOBORDER (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHITECTUREID BIGINT, ARCHITECTURENAME VARCHAR(255), ARCHIVED TINYINT(1) default 0, BILLINGAMOUNT DOUBLE, COMMENTS VARCHAR(255), CONTRACTORID BIGINT, CONTRACTORNAME VARCHAR(255), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEENTERED DATETIME, DATEUPDATED DATETIME, ENGINEERID BIGINT, ENGINEERNAME VARCHAR(255), JOBADDRESS VARCHAR(255), JOBNAME VARCHAR(255), ORDERID VARCHAR(255), PONAME VARCHAR(255), QUOTEID VARCHAR(255), QUOTEDAMOUNT DOUBLE, SALESPERSONID BIGINT, SALESPERSONNAME VARCHAR(255), UPDATEDBYID BIGINT, VAVCONTROLS VARCHAR(255), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE STATE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, STATECODE VARCHAR(255) NOT NULL, STATENAME VARCHAR(255) NOT NULL, COUNTRYID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE TAKEOFF_BIDDER (TAKEOFF BIGINT NOT NULL, BIDDER BIGINT NOT NULL, PRIMARY KEY (TAKEOFF, BIDDER));
CREATE TABLE TAKEOFF_GC (TAKEOFF BIGINT NOT NULL, GC BIGINT NOT NULL, PRIMARY KEY (TAKEOFF, GC));
CREATE TABLE USER_PERMISSION (USERID BIGINT NOT NULL, PERMISSIONID VARCHAR(30) NOT NULL, PRIMARY KEY (USERID, PERMISSIONID));
ALTER TABLE TWITTERPROFILE ADD CONSTRAINT FK_TWITTERPROFILE_PHOTOID FOREIGN KEY (PHOTOID) REFERENCES FILE (ID);
ALTER TABLE ADDRESS ADD CONSTRAINT FK_ADDRESS_COUNTRYID FOREIGN KEY (COUNTRYID) REFERENCES COUNTRY (ID);
ALTER TABLE ADDRESS ADD CONSTRAINT FK_ADDRESS_STATEID FOREIGN KEY (STATEID) REFERENCES STATE (ID);
ALTER TABLE TAKEOFF ADD CONSTRAINT FK_TAKEOFF_SPECID FOREIGN KEY (SPECID) REFERENCES SPEC (ID);
ALTER TABLE WORKSHEET ADD CONSTRAINT FK_WORKSHEET_STATEID FOREIGN KEY (STATEID) REFERENCES STATE (ID);
ALTER TABLE WORKSHEET ADD CONSTRAINT FK_WORKSHEET_COUNTRYID FOREIGN KEY (COUNTRYID) REFERENCES COUNTRY (ID);
ALTER TABLE LINKEDINPROFILE ADD CONSTRAINT FK_LINKEDINPROFILE_PHOTOID FOREIGN KEY (PHOTOID) REFERENCES FILE (ID);
ALTER TABLE PASSWORDRESETHISTORY ADD CONSTRAINT FK_PASSWORDRESETHISTORY_USERID FOREIGN KEY (USERID) REFERENCES USER (ID);
ALTER TABLE TAXENTRY ADD CONSTRAINT FK_TAXENTRY_COUNTRYID FOREIGN KEY (COUNTRYID) REFERENCES COUNTRY (ID);
ALTER TABLE TAXENTRY ADD CONSTRAINT FK_TAXENTRY_STATEID FOREIGN KEY (STATEID) REFERENCES STATE (ID);
ALTER TABLE TRUCKER ADD CONSTRAINT FK_TRUCKER_LOGOID FOREIGN KEY (LOGOID) REFERENCES FILE (ID);
ALTER TABLE FACEBOOKPROFILE ADD CONSTRAINT FK_FACEBOOKPROFILE_PHOTOID FOREIGN KEY (PHOTOID) REFERENCES FILE (ID);
ALTER TABLE USER ADD CONSTRAINT FK_USER_PHOTOID FOREIGN KEY (PHOTOID) REFERENCES FILE (ID);
ALTER TABLE STATE ADD CONSTRAINT FK_STATE_COUNTRYID FOREIGN KEY (COUNTRYID) REFERENCES COUNTRY (ID);
ALTER TABLE TAKEOFF_BIDDER ADD CONSTRAINT FK_TAKEOFF_BIDDER_TAKEOFF FOREIGN KEY (TAKEOFF) REFERENCES TAKEOFF (ID);
ALTER TABLE TAKEOFF_BIDDER ADD CONSTRAINT FK_TAKEOFF_BIDDER_BIDDER FOREIGN KEY (BIDDER) REFERENCES BIDDER (ID);
ALTER TABLE TAKEOFF_GC ADD CONSTRAINT FK_TAKEOFF_GC_GC FOREIGN KEY (GC) REFERENCES GENERALCONTRACTOR (ID);
ALTER TABLE TAKEOFF_GC ADD CONSTRAINT FK_TAKEOFF_GC_TAKEOFF FOREIGN KEY (TAKEOFF) REFERENCES TAKEOFF (ID);
ALTER TABLE USER_PERMISSION ADD CONSTRAINT FK_USER_PERMISSION_USERID FOREIGN KEY (USERID) REFERENCES USER (ID);
ALTER TABLE USER_PERMISSION ADD CONSTRAINT FK_USER_PERMISSION_PERMISSIONID FOREIGN KEY (PERMISSIONID) REFERENCES PERMISSION (AUTHORITY);

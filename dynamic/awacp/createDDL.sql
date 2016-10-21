CREATE TABLE TWITTERPROFILE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, PHONENUMBER VARCHAR(255), TWITTERID VARCHAR(255), TWITTERTOKEN VARCHAR(255), UPDATEDBYID BIGINT, USERID BIGINT, USERNAME VARCHAR(255), VERIFIED TINYINT(1) default 0, VERSION BIGINT, PHOTOID BIGINT, PRIMARY KEY (ID));
CREATE TABLE CONTRACTOR (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(200), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE ADDRESS (ID BIGINT AUTO_INCREMENT NOT NULL, APPARTMENT VARCHAR(255), ARCHIVED TINYINT(1) default 0, CITY VARCHAR(255), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, LATITUDE DOUBLE, LONGITUDE DOUBLE, PLACEID VARCHAR(255), STREET VARCHAR(255), UPDATEDBYID BIGINT, USERID BIGINT NOT NULL, VERSION BIGINT, ZIPCODE VARCHAR(255), COUNTRYID BIGINT NOT NULL, STATEID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE PRODUCT (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(255), DATECREATED DATETIME, DATEUPDATED DATETIME, PRODUCTNAME VARCHAR(255), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(255), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE COUNTRY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, COUNTRYCODE VARCHAR(2) NOT NULL, COUNTRYNAME VARCHAR(25) NOT NULL, PRIMARY KEY (ID));
CREATE TABLE BIDDER (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(200), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), PHONEOFFICE VARCHAR(255), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE PERMISSION (AUTHORITY VARCHAR(30) NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, DESCRIPTION VARCHAR(35) NOT NULL, LABEL VARCHAR(255), UPDATEDBYID BIGINT, URL VARCHAR(255), VERSION BIGINT, PRIMARY KEY (AUTHORITY));
CREATE TABLE SITECOLOR (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE USERMAILHISTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, content LONGTEXT NOT NULL, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, EVENT VARCHAR(255), UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE INVENTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE TAKEOFF (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHITECTUREID BIGINT, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, DRAWINGDATE DATETIME, DRAWINGRECEIVEDFROM VARCHAR(255), DUEDATE DATETIME, ENGINEERID BIGINT, JOBADDRESS VARCHAR(200) NOT NULL, JOBID VARCHAR(255), JOBNAME VARCHAR(100) NOT NULL, JOBSPECIFICATION VARCHAR(200) NOT NULL, PROJECTNUMBER VARCHAR(255), QUOTEID VARCHAR(255), REVISEDDATE DATETIME, SALESPERSON BIGINT NOT NULL, TAKEOFFCOMMENT VARCHAR(255), TAKEOFFID VARCHAR(255), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), USERCODE VARCHAR(255) NOT NULL, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE ORDERBOOK (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE WORKSHEET (ID BIGINT AUTO_INCREMENT NOT NULL, APPARTMENT VARCHAR(255), ARCHIVED TINYINT(1) default 0, CITY VARCHAR(255), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, LATITUDE DOUBLE, LONGITUDE DOUBLE, PLACEID VARCHAR(255), STREET VARCHAR(255), UPDATEDBYID BIGINT, USERID BIGINT, VERSION BIGINT, ZIPCODE VARCHAR(255), COUNTRYID BIGINT NOT NULL, STATEID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE LINKEDINPROFILE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, LINKEDINID VARCHAR(255), LINKEDINTOKEN VARCHAR(255), PHONENUMBER VARCHAR(255), UPDATEDBYID BIGINT, USERID BIGINT NOT NULL, USERNAME VARCHAR(255), VERIFIED TINYINT(1) default 0, VERSION BIGINT, PHOTOID BIGINT, PRIMARY KEY (ID));
CREATE TABLE ROLE (ROLENAME VARCHAR(20) NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, ROLEDESCRIPTION VARCHAR(40) NOT NULL, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ROLENAME));
CREATE TABLE ENGINEER (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(200), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE SITESETTING (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE SPEC (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, DETAIL VARCHAR(200) NOT NULL, UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE PASSWORDRESETHISTORY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, NEWPASSWORD VARCHAR(255), OLDPASSWORD VARCHAR(255), OTP VARCHAR(255), UPDATEDBYID BIGINT, VERSION BIGINT, USERID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE PRODUCTTYPE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE TAXENTRY (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CITY VARCHAR(255), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, RATE DOUBLE, UPDATEDBYID BIGINT, VERSION BIGINT, COUNTRYID BIGINT NOT NULL, STATEID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE COSTSHEET (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE GENERALCONTRACTOR (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(200), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), PHONEOFFICE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE NOTE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE IMAGE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CONTENTTYPE VARCHAR(50) NOT NULL, CREATEDBYID BIGINT, CREATEDNAME VARCHAR(25) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EXTENSION VARCHAR(4) NOT NULL, ORIGINALNAME VARCHAR(255), OTHER VARCHAR(255), SIZE VARCHAR(255), UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE MARKETINGTEMPLE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE ERRORLOG (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, ERRORCODE VARCHAR(255), errorData LONGTEXT, errorText LONGTEXT, serviceUrl LONGTEXT, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE ARCHITECT (ID BIGINT AUTO_INCREMENT NOT NULL, ADDRESS VARCHAR(150), ARCHIVED TINYINT(1) default 0, BASICSPEC VARCHAR(150), CITY VARCHAR(50), COMMENT VARCHAR(200), CREATEDBYID BIGINT, CREATEDBYUSERCODE VARCHAR(10) NOT NULL, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FAX VARCHAR(20), NAME VARCHAR(100) NOT NULL, PHONE VARCHAR(20), SALESPERSON BIGINT NOT NULL, STATE VARCHAR(50), UPDATEDBYID BIGINT, UPDATEDBYUSERCODE VARCHAR(10), VERSION BIGINT, WEBSITE VARCHAR(150), ZIP VARCHAR(10), PRIMARY KEY (ID));
CREATE TABLE FACEBOOKPROFILE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FACEBOOKID VARCHAR(255), FACEBOOKTOKEN LONGTEXT, UPDATEDBYID BIGINT, USERID BIGINT, USERNAME VARCHAR(255), VERIFIED TINYINT(1) default 0, VERSION BIGINT, PHOTOID BIGINT, PRIMARY KEY (ID));
CREATE TABLE USER (ID BIGINT AUTO_INCREMENT NOT NULL, ABOUTME VARCHAR(255), ARCHIVED TINYINT(1) default 0, AVTARIMAGE VARCHAR(255), CONTACT VARCHAR(255), CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, EMAIL VARCHAR(100) NOT NULL, FIRSTLOGIN TINYINT(1) default 0, FIRSTNAME VARCHAR(20) NOT NULL, GENDER VARCHAR(255), LASTNAME VARCHAR(255), MIDDLENAME VARCHAR(255), ONLINE TINYINT(1) default 0, PASSWORD VARCHAR(255), UPDATEDBYID BIGINT, USERCODE VARCHAR(255), USERNAME VARCHAR(25) NOT NULL, VERIFICATIONCODE VARCHAR(255), VERIFIED TINYINT(1) default 0, VERSION BIGINT, PHOTOID BIGINT, ROLE_NAME VARCHAR(20) NOT NULL, PRIMARY KEY (ID));
CREATE TABLE INVOICE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, CREATEDBYID BIGINT, DATECREATED DATETIME, DATEUPDATED DATETIME, UPDATEDBYID BIGINT, VERSION BIGINT, PRIMARY KEY (ID));
CREATE TABLE STATE (ID BIGINT AUTO_INCREMENT NOT NULL, ARCHIVED TINYINT(1) default 0, STATECODE VARCHAR(255) NOT NULL, STATENAME VARCHAR(255) NOT NULL, COUNTRYID BIGINT NOT NULL, PRIMARY KEY (ID));
CREATE TABLE TAKEOFF_BIDDER (TAKEOFF BIGINT NOT NULL, BIDDER BIGINT NOT NULL, PRIMARY KEY (TAKEOFF, BIDDER));
CREATE TABLE TAKEOFF_GC (TAKEOFF BIGINT NOT NULL, GC BIGINT NOT NULL, PRIMARY KEY (TAKEOFF, GC));
CREATE TABLE ROLE_PERMISSION (ROLEID VARCHAR(20) NOT NULL, PERMISSIONID VARCHAR(30) NOT NULL, PRIMARY KEY (ROLEID, PERMISSIONID));
ALTER TABLE TWITTERPROFILE ADD CONSTRAINT FK_TWITTERPROFILE_PHOTOID FOREIGN KEY (PHOTOID) REFERENCES IMAGE (ID);
ALTER TABLE ADDRESS ADD CONSTRAINT FK_ADDRESS_COUNTRYID FOREIGN KEY (COUNTRYID) REFERENCES COUNTRY (ID);
ALTER TABLE ADDRESS ADD CONSTRAINT FK_ADDRESS_STATEID FOREIGN KEY (STATEID) REFERENCES STATE (ID);
ALTER TABLE WORKSHEET ADD CONSTRAINT FK_WORKSHEET_STATEID FOREIGN KEY (STATEID) REFERENCES STATE (ID);
ALTER TABLE WORKSHEET ADD CONSTRAINT FK_WORKSHEET_COUNTRYID FOREIGN KEY (COUNTRYID) REFERENCES COUNTRY (ID);
ALTER TABLE LINKEDINPROFILE ADD CONSTRAINT FK_LINKEDINPROFILE_PHOTOID FOREIGN KEY (PHOTOID) REFERENCES IMAGE (ID);
ALTER TABLE PASSWORDRESETHISTORY ADD CONSTRAINT FK_PASSWORDRESETHISTORY_USERID FOREIGN KEY (USERID) REFERENCES USER (ID);
ALTER TABLE TAXENTRY ADD CONSTRAINT FK_TAXENTRY_COUNTRYID FOREIGN KEY (COUNTRYID) REFERENCES COUNTRY (ID);
ALTER TABLE TAXENTRY ADD CONSTRAINT FK_TAXENTRY_STATEID FOREIGN KEY (STATEID) REFERENCES STATE (ID);
ALTER TABLE FACEBOOKPROFILE ADD CONSTRAINT FK_FACEBOOKPROFILE_PHOTOID FOREIGN KEY (PHOTOID) REFERENCES IMAGE (ID);
ALTER TABLE USER ADD CONSTRAINT FK_USER_PHOTOID FOREIGN KEY (PHOTOID) REFERENCES IMAGE (ID);
ALTER TABLE USER ADD CONSTRAINT FK_USER_ROLE_NAME FOREIGN KEY (ROLE_NAME) REFERENCES ROLE (ROLENAME);
ALTER TABLE STATE ADD CONSTRAINT FK_STATE_COUNTRYID FOREIGN KEY (COUNTRYID) REFERENCES COUNTRY (ID);
ALTER TABLE TAKEOFF_BIDDER ADD CONSTRAINT FK_TAKEOFF_BIDDER_TAKEOFF FOREIGN KEY (TAKEOFF) REFERENCES TAKEOFF (ID);
ALTER TABLE TAKEOFF_BIDDER ADD CONSTRAINT FK_TAKEOFF_BIDDER_BIDDER FOREIGN KEY (BIDDER) REFERENCES BIDDER (ID);
ALTER TABLE TAKEOFF_GC ADD CONSTRAINT FK_TAKEOFF_GC_GC FOREIGN KEY (GC) REFERENCES GENERALCONTRACTOR (ID);
ALTER TABLE TAKEOFF_GC ADD CONSTRAINT FK_TAKEOFF_GC_TAKEOFF FOREIGN KEY (TAKEOFF) REFERENCES TAKEOFF (ID);
ALTER TABLE ROLE_PERMISSION ADD CONSTRAINT FK_ROLE_PERMISSION_ROLEID FOREIGN KEY (ROLEID) REFERENCES ROLE (ROLENAME);
ALTER TABLE ROLE_PERMISSION ADD CONSTRAINT FK_ROLE_PERMISSION_PERMISSIONID FOREIGN KEY (PERMISSIONID) REFERENCES PERMISSION (AUTHORITY);

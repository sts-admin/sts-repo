package com.sts.core.constant;

public class AwacpMailTemplate {
	public static final StringBuffer NEW_TAKEOFF_MAIL_MESSAGE = new StringBuffer("<div style='font-family:verdana;'>"
	+"<div style='height:25px;overflow:hidden; text-align:center;background-color:gray; max-width: 722px;margin: 0 auto;'>"
	+"<h3 style='color:white; text-align:center; margin:0; padding:0;'>New Takeoff Detail</h3></div>"
	+"<div style='background-color:silver;border: 1px solid #ccc; margin: 0 auto; max-width: 700px; padding: 10px; position: relative; overflow:hidden;'>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Date Created</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Salesman</p>"
	+"<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>User Code</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Takeoff ID</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Project #</p>"
	+"<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p> </div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Engineer</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Architect</p>"
	+"<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"
	

	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Job Name</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Job Address</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Specifications</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Drawing Date</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0; padding-left:50px'>Revised Date</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Due Date</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Received From</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>List Of Bidders</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left;  min-width:110px; margin:0;'>List Of GC</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left;margin:0;'>%s</p></div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left;  min-width:110px; margin:0;'>Comments</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'></p>%s</div></div>"

	+"<div style='background-color:darkgray;margin: 0 auto; max-width: 700px; padding: 10px; position: relative; overflow:hidden;'>"
	+"<p style='font-size:12px;'><span style='font-weight:bold; font-size:14px;'>REGARDS</span>,<br />PAT</p>"
	+"<p style='font-size:12px; text-align:left'>ALBERT WEISS AIR CONDITIONING PRODUCTS INC.<br />"
	+"270 MADISON AVENUE, SUITE 1805, NEW YORK, N.Y. 10016.<br />"
	+"EMAIL: <a href='#'>edwgs@awacp.com</a></p>"
	+"</div></div>");
}

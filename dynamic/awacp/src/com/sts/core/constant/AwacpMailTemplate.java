package com.sts.core.constant;

public class AwacpMailTemplate {
	public static final StringBuffer NEW_TAKEOFF_MAIL_MESSAGE = new StringBuffer("<div style='font-family:verdana;'>"
			+ "<div style='height:25px;overflow:hidden; text-align:center;background-color:gray; max-width: 722px;margin: 0 auto;'>"
			+ "<h3 style='color:white; text-align:center; margin:0; padding:0;'>New Takeoff Detail</h3></div>"
			+ "<div style='background-color:silver;border: 1px solid #ccc; margin: 0 auto; max-width: 700px; padding: 10px; position: relative; overflow:hidden;'>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Date Created</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+ "<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Salesman</p>"
			+ "<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+ "<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>User Code</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Takeoff ID</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+ "<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Project #</p>"
			+ "<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p> </div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Engineer</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+ "<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Architect</p>"
			+ "<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Job Name</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Job Address</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Specifications</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Drawing Date</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+ "<p style='font-weight:bold;font-size:13px; float:left; margin:0; padding-left:50px'>Revised Date</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+ "<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Due Date</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Received From</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>List Of Bidders</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left;  min-width:110px; margin:0;'>List Of GC</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left;margin:0;'>%s</p></div>"

	+ "<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
			+ "<p style='font-weight:bold;font-size:13px; float:left;  min-width:110px; margin:0;'>Comments</p>"
			+ "<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
			+ "<p style='font-size:13px; float:left; margin:0;'></p>%s</div></div>"

	+ "<div style='background-color:darkgray;margin: 0 auto; max-width: 700px; padding: 10px; position: relative; overflow:hidden;'>"
			+ "<p style='font-size:12px;'><span style='font-weight:bold; font-size:14px;'>REGARDS</span>,<br />PAT</p>"
			+ "<p style='font-size:12px; text-align:left'>ALBERT WEISS AIR CONDITIONING PRODUCTS INC.<br />"
			+ "270 MADISON AVENUE, SUITE 1805, NEW YORK, N.Y. 10016.<br />"
			+ "EMAIL: <a href='#'>edwgs@awacp.org</a></p>" + "</div></div>");

	public static final StringBuffer QUOTE_EMAIL_TO_BIDDERS_TEMPLATE = new StringBuffer(
		"<div style='font-family:verdana;'>"
			+"<div style='margin: 0 auto; max-width: 730px; padding: 10px; position: relative; overflow:hidden;'>"
				+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
					+"<p style='font-weight:bold;font-size:12px; float:left; min-width:110px; margin-top:10px;'>Dear valued customer,</p>"
					+"<p style='font-size:13px; float:left; min-width:110px; margin:0;'> MAY WE REQUEST YOU TO PUT OUR EMAIL IN THE SAFE SENDER LIST AND ALSO ALLOW EMAILS FROM OUR DOMAIN. THIS ACTION WILL HELP US TO SEND QUOTES TO YOU WITHOUT ANY INTERUPTION.</p>"		
					+"<p style='font-size:12px; float:left; min-width:110px; margin-top:10px;'>We are pleased to submit our quotation for the project mentioned below for your kind consideration.</p>"
				+"</div>"
			+"</div>"
				
			+"<div style='border: 1px solid #ccc; margin: 0 auto; max-width: 720px; padding: 10px; position: relative; overflow:hidden;'>"
				+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
					+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Project Ref #</p>"
					+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
					+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
		
					+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Quote No #</p>"
					+"<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
					+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
				
					+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>User Code</p>"
					+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
					+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
				+"</div>"
				
				+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
					+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Job Name</p>"
					+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
					+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
				+ "</div>"
				+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
					+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Address</p>"
					+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
					+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
				+"</div>"
				+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
					+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Drawing Date</p>"
					+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
					+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
				+"</div>"
				+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
					+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Engineer</p>"
					+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
					+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
				+"</div>"
				+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
					+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Architecture</p>"
					+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
					+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
				+"</div>"
			+"</div>"
				
			+"<div style='margin: 0 auto; max-width: 730px; padding: 10px; position: relative; overflow:hidden;'>"
				+"<div style='padding: 8px 0;'>"
					+"<p style='font-size:13px;min-width:110px; margin:0;'> Regards,</p>"
					+"<p style='font-size:13px; float:left; min-width:110px; margin:0;'>%s</p>"
				+"</div>"
			+"</div>"
			
			+"<div style='margin: 0 auto; max-width: 730px;'>ALBERT WEISS AIR CONDITIONING PRODUCTS INC., <br />270 MADISON AVENUE, SUITE 1805, NEW YORK, N.Y. 10016. <br />EMAIL: edwgs@awacp.org </div>"			
			+"<div style='margin: 0 auto; max-width: 730px;'> <br /> <pre><b>Note:- </b> Kindly send all bid and quote requests to: edwgs@awacp.org. To view all previously quoted projects to you, please  <a href='http://www.awacp.org/index.php/bidderlogin'>CLICK HERE</a> and login with your exclusive email, your password is:awacp.</pre> </div>"
			
			+"<div style='margin: 0 auto; max-width: 730px;'><pre><b>CONFIDENTIALITY NOTE </b> :- The information contained in this email is intended only for the use of the individual or entity named above and may contain information that is privileged, Confidential and exempt from disclosure under applicable law. </pre> </div>"
			+"<div style='margin: 0 auto; max-width: 730px;'> <pre>If the reader of this message is not the intended recipient, you are hereby notified that any dissemination, distribution or copying of this communication is strictly prohibited. you have received this message in error, please immediately notify the sender and delete the mail. Thank you. </pre> </div>"
		+"</div>"); 
		
	
	public static final StringBuffer QUOTE_FOLLOW_UP = new StringBuffer("<div style='font-family:verdana;'>"
	+"<div style='height:25px;overflow:hidden; text-align:center;background-color:gray; max-width: 722px;margin: 0 auto;'><h3 style='color:white; text-align:center; margin:0; padding:0;'>Quote Follow up</h3></div>"
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
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Takeoff ID</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Project #</p>"
	+"<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Quote ID</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Amount</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Engineer</p>"
	+"<p style='font-weight:bold;padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Architect</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"
	

	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Job Name</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"

	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Job Address</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Specifications</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Drawing Date</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0; padding-left:50px'>Revised Date</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"

	+"<p style='font-weight:bold;font-size:13px; float:left; margin:0;padding-left:50px'>Due Date</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"
	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>Received From</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left;margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>	"
	+"</div>"
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left; min-width:110px; margin:0;'>List Of Bidders</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"

	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left;  min-width:110px; margin:0;'>List Of GC</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left;margin:0;'>%s</p>"
	+"</div>"

	
	+"<div style='clear: both;display: block; float: left; padding: 8px 0;'>"
	+"<p style='font-weight:bold;font-size:13px; float:left;  min-width:110px; margin:0;'>Comments</p>"
	+"<p style='padding:0 15px;font-size:13px;float:left; margin:0;'>:</p>"
	+"<p style='font-size:13px; float:left; margin:0;'>%s</p>"
	+"</div>"


	+"</div>"

	+"<div style='background-color:darkgray;margin: 0 auto; max-width: 700px; padding: 10px; position: relative; overflow:hidden;'>"
		+"<p style='font-size:12px;'><span style='font-weight:bold; font-size:14px;'>REGARDS</span>,<br />%s</p>"
		+"<p style='font-size:12px; text-align:left'>ALBERT WEISS AIR CONDITIONING PRODUCTS INC.<br />"
		+"270 MADISON AVENUE, SUITE 1805, NEW YORK, N.Y. 10016.<br />"
		+"EMAIL: <a href='#'>edwgs@awacp.org</a></p>"
	+"</div>"
+"</div>");
}

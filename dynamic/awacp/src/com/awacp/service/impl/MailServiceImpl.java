/**
 * 
 */
package com.awacp.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.entity.SiteInfo;
import com.awacp.entity.Bidder;
import com.awacp.entity.GeneralContractor;
import com.awacp.entity.JobOrder;
import com.awacp.entity.OrderBook;
import com.awacp.entity.OrderBookInvItem;
import com.awacp.entity.Takeoff;
import com.awacp.entity.Worksheet;
import com.awacp.service.AppSettingService;
import com.awacp.service.ArchitectService;
import com.awacp.service.EngineerService;
import com.awacp.service.JobService;
import com.awacp.service.OrderBookService;
import com.awacp.service.TakeoffService;
import com.sts.core.config.AppPropConfig;
import com.sts.core.constant.AwacpMailTemplate;
import com.sts.core.entity.User;
import com.sts.core.mail.service.MailService;
import com.sts.core.service.UserService;

public class MailServiceImpl implements com.awacp.service.MailService {
	SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

	@Autowired
	MailService mailService;

	@Autowired
	TakeoffService takeoffService;

	@Autowired
	UserService userService;

	@Autowired
	EngineerService engineerService;

	@Autowired
	ArchitectService architectService;

	@Autowired
	OrderBookService orderBookService;

	@Autowired
	JobService jobService;

	@Autowired
	AppSettingService appSettingService;

	@Override
	public String sendNewTakeoffMail(Long takeoffId) throws Exception {
		String[] toAddresses = takeoffService.getNewTakeoffEmails();
		if (toAddresses == null || toAddresses.length == 0) {
			return "NO_TO_TAKEOFF_EMAIL";
		}
		Takeoff takeoff = takeoffService.getTakeoff(takeoffId);
		User user = userService.findUser(takeoff.getSalesPerson());
		takeoff.setSalesPersonName(user.getFirstName() + "	" + user.getLastName());
		if (takeoff.getEngineerId() != null) {
			takeoff.setEngineerName(engineerService.getEngineer(takeoff.getEngineerId()).getName());
		} else {
			takeoff.setEngineerName("");
		}
		if (takeoff.getArchitectureId() != null) {
			takeoff.setArchitectureName(architectService.getArchitect(takeoff.getArchitectureId()).getName());
		} else {
			takeoff.setArchitectureName("");
		}

		String dateCreated = takeoff.getDateCreated() == null ? ""
				: DATE_FORMAT.format(takeoff.getDateCreated().getTime());
		String drawingDate = takeoff.getDrawingDate() == null ? ""
				: DATE_FORMAT.format(takeoff.getDrawingDate().getTime());
		String reviseDate = takeoff.getRevisedDate() == null ? ""
				: DATE_FORMAT.format(takeoff.getRevisedDate().getTime());
		String dueDate = takeoff.getDueDate() == null ? "" : DATE_FORMAT.format(takeoff.getDueDate().getTime());
		StringBuffer biddersList = new StringBuffer("");
		if (takeoff.getBidders() != null) {
			int bidderCounter = 0;
			for (Bidder b : takeoff.getBidders()) {
				biddersList.append(b.getName());
				bidderCounter++;
				if (bidderCounter != takeoff.getBidders().size()) {
					biddersList.append(", ");
				}

			}
		}
		StringBuffer gcs = new StringBuffer("");
		if (takeoff.getGeneralContractors() != null) {
			int contractorCounter = 0;
			for (GeneralContractor gc : takeoff.getGeneralContractors()) {
				gcs.append(gc.getName());
				contractorCounter++;
				if (contractorCounter != takeoff.getGeneralContractors().size()) {
					gcs.append(", ");
				}

			}
		}
		String projectNo = takeoff.getProjectNumber() == null ? "" : takeoff.getProjectNumber();
		String drawingReceivedFrom = takeoff.getDrawingReceivedFrom() == null ? "" : takeoff.getDrawingReceivedFrom();
		String comment = takeoff.getTakeOffComment() == null ? "" : takeoff.getTakeOffComment();

		String content = String.format(AwacpMailTemplate.NEW_TAKEOFF_MAIL_MESSAGE.toString(), dateCreated,
				takeoff.getSalesPersonName(), takeoff.getUserCode(), takeoff.getTakeoffId(), projectNo,
				takeoff.getEngineerName(), takeoff.getArchitectureName(), takeoff.getJobName(), takeoff.getJobAddress(),
				takeoff.getSpec().getDetail(), drawingDate, reviseDate, dueDate, drawingReceivedFrom,
				biddersList.toString(), gcs, comment, takeoff.getSalesPersonName());
		boolean status = mailService.sendMail(toAddresses, AppPropConfig.emailNewTakeoff, "AWACP :: New Takeoff Detail",
				content, "NEW_TAKEOFF", AppPropConfig.emailNewTakeoff, AppPropConfig.emailCommonPassword, null, null);
		return status == true ? "TAKEOFF_MAIL_SUCCESS" : "TAKEOFF_MAIL_FAILED";
	}

	@Override
	public boolean sendTakeoffMail() {
		return false;
	}

	@Override
	public boolean sendQuoteFollowupMail(Long takeoffId) throws Exception {

		String[] toAddresses = takeoffService.getNewTakeoffEmails();
		if (toAddresses == null || toAddresses.length == 0) {
			return false;
		}
		Takeoff takeoff = takeoffService.getTakeoff(takeoffId);
		User user = userService.findUser(takeoff.getSalesPerson());
		takeoff.setSalesPersonName(user.getFirstName() + "	" + user.getLastName());
		if (takeoff.getEngineerId() != null) {
			takeoff.setEngineerName(engineerService.getEngineer(takeoff.getEngineerId()).getName());
		} else {
			takeoff.setEngineerName("");
		}
		if (takeoff.getArchitectureId() != null) {
			takeoff.setArchitectureName(architectService.getArchitect(takeoff.getArchitectureId()).getName());
		} else {
			takeoff.setArchitectureName("");
		}

		String dateCreated = takeoff.getDateCreated() == null ? ""
				: DATE_FORMAT.format(takeoff.getDateCreated().getTime());
		String drawingDate = takeoff.getDrawingDate() == null ? ""
				: DATE_FORMAT.format(takeoff.getDrawingDate().getTime());
		String reviseDate = takeoff.getRevisedDate() == null ? ""
				: DATE_FORMAT.format(takeoff.getRevisedDate().getTime());
		String dueDate = takeoff.getDueDate() == null ? "" : DATE_FORMAT.format(takeoff.getDueDate().getTime());
		StringBuffer biddersList = new StringBuffer("");
		if (takeoff.getBidders() != null) {
			int bidderCounter = 0;
			for (Bidder b : takeoff.getBidders()) {
				biddersList.append(b.getName());
				bidderCounter++;
				if (bidderCounter != takeoff.getBidders().size()) {
					biddersList.append(", ");
				}

			}
		}
		StringBuffer gcs = new StringBuffer("");
		if (takeoff.getGeneralContractors() != null) {
			int contractorCounter = 0;
			for (GeneralContractor gc : takeoff.getGeneralContractors()) {
				gcs.append(gc.getName());
				contractorCounter++;
				if (contractorCounter != takeoff.getGeneralContractors().size()) {
					gcs.append(", ");
				}

			}
		}
		String projectNo = takeoff.getProjectNumber() == null ? "" : takeoff.getProjectNumber();
		String drawingReceivedFrom = takeoff.getDrawingReceivedFrom() == null ? "" : takeoff.getDrawingReceivedFrom();
		String comment = takeoff.getTakeOffComment() == null ? "" : takeoff.getTakeOffComment();

		String content = String.format(AwacpMailTemplate.QUOTE_FOLLOW_UP.toString(), dateCreated,
				takeoff.getSalesPersonName(), takeoff.getUserCode(), takeoff.getTakeoffId(), projectNo,
				takeoff.getQuoteId(), takeoff.getAmount(), takeoff.getEngineerName(), takeoff.getArchitectureName(),
				takeoff.getJobName(), takeoff.getJobAddress(), takeoff.getSpec().getDetail(), drawingDate, reviseDate,
				dueDate, drawingReceivedFrom, biddersList.toString(), gcs, comment, takeoff.getSalesPersonName());
		return mailService.sendMail(toAddresses, AppPropConfig.emailQuoteFollowup, "AWACP :: Quote Follow up", content,
				"NEW_TAKEOFF", AppPropConfig.emailQuoteFollowup, AppPropConfig.emailQuoteFollowupPassword, null, null);

	}

	@Override
	public boolean sendFinalUpdateMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendMondayReminderMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendEstimateReminderMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendLowInventoryMail(String toAddress, String inv, String owner, String userCode, int orderQty,
			int availQty, int reorderQty, String itemSize, String itemDesc, String orderNum) throws Exception {
		String content = String.format(AwacpMailTemplate.LOW_INV_REMIND_MESSAGE.toString(), inv, owner, userCode,
				itemSize, itemDesc, orderNum, orderQty, availQty, reorderQty);
		return mailService.sendMail(new String[] { toAddress }, AppPropConfig.emailNoReply,
				"AWACP :: Low Inventory Reminder", content, "LOW_INVENTORY_REMINDER", AppPropConfig.emailQuoteFollowup,
				AppPropConfig.emailQuoteFollowupPassword, null, null);
	}

	@Override
	public boolean sendOrderConfirmationMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendNoReplyMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendQuoteMailToBidders(Worksheet worksheet, String fileName, String quotePdfFilePath)
			throws Exception {
		Takeoff takeoff = worksheet.getTakeoff();
		String[] bidderEmails = new String[takeoff.getBidders().size()];
		int index = 0;

		for (Bidder bidder : takeoff.getBidders()) {
			bidderEmails[index++] = bidder.getEmail();
		}
		File file = new File(quotePdfFilePath);
		String drawingDate = takeoff.getDrawingDate() == null ? ""
				: DATE_FORMAT.format(takeoff.getDrawingDate().getTime());

		String content = String.format(AwacpMailTemplate.QUOTE_EMAIL_TO_BIDDERS_TEMPLATE.toString(),
				takeoff.getProjectNumber() == null ? "" : takeoff.getProjectNumber(), takeoff.getQuoteId(),
				takeoff.getUserCode(), takeoff.getJobName(), takeoff.getJobAddress(), drawingDate,
				takeoff.getEngineerName() == null ? "" : takeoff.getEngineerName(),
				takeoff.getArchitectureName() == null ? "" : takeoff.getArchitectureName(),
				takeoff.getSalesPersonName());
		boolean status = mailService.sendMail(bidderEmails, AppPropConfig.quoteMailToBidder,
				"AWACP QUOTE :" + takeoff.getQuoteId(), content, "QUOTE_MAIL_TO_BIDDER",
				AppPropConfig.quoteMailToBidder, AppPropConfig.quoteMailToBidderPassword, fileName, file);
		return status;
	}

	@Override
	public boolean sendPremiumOrderEmail(String emailOrId, String source, Long orderBookId) throws Exception {
		OrderBook ob = orderBookService.fetchPremiumOrder(orderBookId);
		JobOrder jo = jobService.getJobOrder(ob.getJobId());
		if (source.equalsIgnoreCase("id")) {
			emailOrId = userService.findUser(Long.valueOf(emailOrId)).getEmail();
		}
		SiteInfo setting = appSettingService.getSiteInfo(1L);
		String logoPath = AppPropConfig.acBaseUrl + "/" + AppPropConfig.acImageDir + "/" + "big_logo.png";
		String orderDate = jo.getDateCreated() == null ? "" : DATE_FORMAT.format(ob.getDateCreated().getTime());
		String estDate = ob.getEstDate() == null ? "" : DATE_FORMAT.format(ob.getEstDate().getTime());
		String orderItems = "";
		if (ob.getInvItems() != null && !ob.getInvItems().isEmpty()) {
			int index = 0;
			for (OrderBookInvItem obii : ob.getInvItems()) {
				orderItems += "<tr><th scope='row'>" + ++index + "</th><td>" + obii.getSize() + "</td> <td>"
						+ obii.getItemDescription() + "</td></tr>";
			}
		}

		String shippingStatus = "";

		String content = String.format(AwacpMailTemplate.PREMIUM_ORDER_EMAIL.toString(), logoPath,
				setting.getPhoneNumber(), setting.getOfficeAddress(), setting.getFaxNumber(), setting.getFrontWebsite(),
				setting.getEmailAddress(), ob.getFactoryType(), jo.getOrderNumber(), orderDate,
				jo.getPoName() == null ? "" : jo.getPoName(),
				ob.getContractorName() == null ? "" : ob.getContractorName(),
				ob.getShipToName() == null ? "" : ob.getShipToName(), ob.getAttn() == null ? "" : ob.getAttn(),
				jo.getJobName(), jo.getJobAddress(), ob.getCreatedByUserCode(), ob.getSalesPersonName(), estDate,
				orderItems, shippingStatus);
		String toAddress[] = new String[] { emailOrId };
		String subject = "AWACP ORDER CONFIRMATION :" + ob.getJobOrderNumber() + " : " + ob.getContractorName() == null
				? "" : ob.getContractorName() + " : " + jo.getJobName();
		return mailService.sendMail(toAddress, AppPropConfig.emailPremiumOrder, subject, content, "PREMIUM_ORDER",
				AppPropConfig.emailPremiumOrder, AppPropConfig.emailPremiumOrderPassword, null, null);
	}

}

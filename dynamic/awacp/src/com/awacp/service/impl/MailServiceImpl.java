/**
 * 
 */
package com.awacp.service.impl;

import java.io.File;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;

import com.awacp.entity.Bidder;
import com.awacp.entity.GeneralContractor;
import com.awacp.entity.Takeoff;
import com.awacp.service.ArchitectService;
import com.awacp.service.EngineerService;
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

	@Override
	public String sendNewTakeoffMail(Long takeoffId) throws Exception {
		String[] toAddresses = takeoffService.getNewTakeoffEmails(takeoffId);
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
				biddersList.toString(), gcs, comment);
		boolean status = mailService.sendMail(toAddresses, AppPropConfig.emailNewTakeoff, "AWACP :: New Takeoff Detail",
				content, "NEW_TAKEOFF", AppPropConfig.emailNewTakeoff, AppPropConfig.emailCommonPassword, null, null);
		return status == true ? "TAKEOFF_MAIL_SUCCESS" : "TAKEOFF_MAIL_FAILED";
	}

	@Override
	public boolean sendTakeoffMail() {
		return false;
	}

	@Override
	public boolean sendQuoteFollowupMail() {
		// TODO Auto-generated method stub
		return false;
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
	public boolean sendAwLowInventoryMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendAwfLowInventoryMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendSbcLowInventoryMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendSplLowInventorySplMail() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendJobLowInventoryMail() {
		// TODO Auto-generated method stub
		return false;
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
	public boolean sendQuoteMailToBidders(Long takeoffId, String fileName, String quotePdfFilePath) throws Exception {
		Takeoff takeoff = takeoffService.getTakeoff(takeoffId);
		String[] bidderEmails = new String[takeoff.getBidders().size()];
		int index = 0;
		for (Bidder bidder : takeoff.getBidders()) {
			bidderEmails[index++] = bidder.getEmail();
		}
		File file = new File(quotePdfFilePath);
		String drawingDate = takeoff.getDrawingDate() == null ? ""
				: DATE_FORMAT.format(takeoff.getDrawingDate().getTime());

		String content = String.format(AwacpMailTemplate.QUOTE_EMAIL_TO_BIDDERS_TEMPLATE.toString(),
				takeoff.getProjectNumber(), takeoff.getQuoteId(), takeoff.getUserCode(), takeoff.getJobName(),
				takeoff.getJobAddress(), drawingDate, takeoff.getEngineerName(), takeoff.getArchitectureName());
		boolean status = mailService.sendMail(bidderEmails, AppPropConfig.quoteMailToBidder, "AWACP QUOTE :"+takeoff.getQuoteId(), content,
				"QUOTE_MAIL_TO_BIDDER", AppPropConfig.quoteMailToBidder, AppPropConfig.quoteMailToBidderPassword,
				fileName, file);
		return status;
	}

}

package com.awacp.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: SiteEmailAccount
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "SiteEmailAccount.findAll", query = "SELECT mi FROM SiteEmailAccount mi WHERE mi.archived = 'false' ORDER BY mi.dateCreated DESC")

})
public class SiteEmailAccount extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String bidderRequestEmail;
	private String bidderRequestPassword;
	private String edwgsEmail;
	private String edwgsEmailPassword;
	private String voiceEmail;
	private String voiceEmailPassword;
	private String finalUpdateEmail;
	private String finalUpdateEmailPassword;
	private String adminEmail;
	private String adminEmailPassword;
	private String estOrbfReminderEmail;
	private String estOrbfReminderEmailPassword;
	private String inventoryAlertEmail;
	private String inventoryAlertEmailPassword;
	private String followupEmail;
	private String followupEmailPassword;
	private String infoEmail;
	private String infoEmailPassword;
	private String inventoryEmail;
	private String inventoryEmailPassword;
	private String mondayMorningEmail;
	private String mondayMorningEmailPassword;
	private String noReplyEmail;
	private String noReplyEmailPassword;

	public SiteEmailAccount() {
		super();
	}

	public String getBidderRequestEmail() {
		return bidderRequestEmail;
	}

	public String getBidderRequestPassword() {
		return bidderRequestPassword;
	}

	public String getEdwgsEmail() {
		return edwgsEmail;
	}

	public String getEdwgsEmailPassword() {
		return edwgsEmailPassword;
	}

	public String getVoiceEmail() {
		return voiceEmail;
	}

	public String getVoiceEmailPassword() {
		return voiceEmailPassword;
	}

	public String getFinalUpdateEmail() {
		return finalUpdateEmail;
	}

	public String getFinalUpdateEmailPassword() {
		return finalUpdateEmailPassword;
	}

	public String getAdminEmail() {
		return adminEmail;
	}

	public String getAdminEmailPassword() {
		return adminEmailPassword;
	}

	public String getEstOrbfReminderEmail() {
		return estOrbfReminderEmail;
	}

	public String getEstOrbfReminderEmailPassword() {
		return estOrbfReminderEmailPassword;
	}

	public String getInventoryAlertEmail() {
		return inventoryAlertEmail;
	}

	public String getInventoryAlertEmailPassword() {
		return inventoryAlertEmailPassword;
	}

	public String getFollowupEmail() {
		return followupEmail;
	}

	public String getFollowupEmailPassword() {
		return followupEmailPassword;
	}

	public String getInfoEmail() {
		return infoEmail;
	}

	public String getInfoEmailPassword() {
		return infoEmailPassword;
	}

	public String getInventoryEmail() {
		return inventoryEmail;
	}

	public String getInventoryEmailPassword() {
		return inventoryEmailPassword;
	}

	public String getMondayMorningEmail() {
		return mondayMorningEmail;
	}

	public String getMondayMorningEmailPassword() {
		return mondayMorningEmailPassword;
	}

	public String getNoReplyEmail() {
		return noReplyEmail;
	}

	public String getNoReplyEmailPassword() {
		return noReplyEmailPassword;
	}

	public void setBidderRequestEmail(String bidderRequestEmail) {
		this.bidderRequestEmail = bidderRequestEmail;
	}

	public void setBidderRequestPassword(String bidderRequestPassword) {
		this.bidderRequestPassword = bidderRequestPassword;
	}

	public void setEdwgsEmail(String edwgsEmail) {
		this.edwgsEmail = edwgsEmail;
	}

	public void setEdwgsEmailPassword(String edwgsEmailPassword) {
		this.edwgsEmailPassword = edwgsEmailPassword;
	}

	public void setVoiceEmail(String voiceEmail) {
		this.voiceEmail = voiceEmail;
	}

	public void setVoiceEmailPassword(String voiceEmailPassword) {
		this.voiceEmailPassword = voiceEmailPassword;
	}

	public void setFinalUpdateEmail(String finalUpdateEmail) {
		this.finalUpdateEmail = finalUpdateEmail;
	}

	public void setFinalUpdateEmailPassword(String finalUpdateEmailPassword) {
		this.finalUpdateEmailPassword = finalUpdateEmailPassword;
	}

	public void setAdminEmail(String adminEmail) {
		this.adminEmail = adminEmail;
	}

	public void setAdminEmailPassword(String adminEmailPassword) {
		this.adminEmailPassword = adminEmailPassword;
	}

	public void setEstOrbfReminderEmail(String estOrbfReminderEmail) {
		this.estOrbfReminderEmail = estOrbfReminderEmail;
	}

	public void setEstOrbfReminderEmailPassword(String estOrbfReminderEmailPassword) {
		this.estOrbfReminderEmailPassword = estOrbfReminderEmailPassword;
	}

	public void setInventoryAlertEmail(String inventoryAlertEmail) {
		this.inventoryAlertEmail = inventoryAlertEmail;
	}

	public void setInventoryAlertEmailPassword(String inventoryAlertEmailPassword) {
		this.inventoryAlertEmailPassword = inventoryAlertEmailPassword;
	}

	public void setFollowupEmail(String followupEmail) {
		this.followupEmail = followupEmail;
	}

	public void setFollowupEmailPassword(String followupEmailPassword) {
		this.followupEmailPassword = followupEmailPassword;
	}

	public void setInfoEmail(String infoEmail) {
		this.infoEmail = infoEmail;
	}

	public void setInfoEmailPassword(String infoEmailPassword) {
		this.infoEmailPassword = infoEmailPassword;
	}

	public void setInventoryEmail(String inventoryEmail) {
		this.inventoryEmail = inventoryEmail;
	}

	public void setInventoryEmailPassword(String inventoryEmailPassword) {
		this.inventoryEmailPassword = inventoryEmailPassword;
	}

	public void setMondayMorningEmail(String mondayMorningEmail) {
		this.mondayMorningEmail = mondayMorningEmail;
	}

	public void setMondayMorningEmailPassword(String mondayMorningEmailPassword) {
		this.mondayMorningEmailPassword = mondayMorningEmailPassword;
	}

	public void setNoReplyEmail(String noReplyEmail) {
		this.noReplyEmail = noReplyEmail;
	}

	public void setNoReplyEmailPassword(String noReplyEmailPassword) {
		this.noReplyEmailPassword = noReplyEmailPassword;
	}

}

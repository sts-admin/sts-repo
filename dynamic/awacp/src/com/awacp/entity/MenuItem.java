package com.awacp.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: MenuItem
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "MenuItem.findAll", query = "SELECT mi FROM MenuItem mi WHERE mi.archived = 'false' ORDER BY mi.dateCreated DESC")

})
public class MenuItem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String takeoffPrefix;
	private String quotePrefix;
	private String jobOrderPrefix;
	private String awPrefix;
	private String awfPrefix;
	private String sbcPrefix;
	private String splPrefix;
	private String jPrefix;
	private String qPrefix;

	public MenuItem() {
		super();
	}

	public String getTakeoffPrefix() {
		return takeoffPrefix == null ? "T" : takeoffPrefix;
	}

	public String getQuotePrefix() {
		return quotePrefix == null ? "Q" : quotePrefix;
	}

	public String getJobOrderPrefix() {
		return jobOrderPrefix == null ? "J" : jobOrderPrefix;
	}

	public String getAwPrefix() {
		return awPrefix == null ? "AW" : awPrefix;
	}

	public String getAwfPrefix() {
		return awfPrefix == null ? "AWF" : awfPrefix;
	}

	public String getSbcPrefix() {
		return sbcPrefix == null ? "SBC" : sbcPrefix;
	}

	public String getSplPrefix() {
		return splPrefix;
	}

	public String getjPrefix() {
		return jPrefix == null ? "J" : jPrefix;
	}

	public String getqPrefix() {
		return qPrefix;
	}

	public void setTakeoffPrefix(String takeoffPrefix) {
		this.takeoffPrefix = takeoffPrefix;
	}

	public void setQuotePrefix(String quotePrefix) {
		this.quotePrefix = quotePrefix;
	}

	public void setJobOrderPrefix(String jobOrderPrefix) {
		this.jobOrderPrefix = jobOrderPrefix;
	}

	public void setAwPrefix(String awPrefix) {
		this.awPrefix = awPrefix;
	}

	public void setAwfPrefix(String awfPrefix) {
		this.awfPrefix = awfPrefix;
	}

	public void setSbcPrefix(String sbcPrefix) {
		this.sbcPrefix = sbcPrefix;
	}

	public void setSplPrefix(String splPrefix) {
		this.splPrefix = splPrefix;
	}

	public void setjPrefix(String jPrefix) {
		this.jPrefix = jPrefix;
	}

	public void setqPrefix(String qPrefix) {
		this.qPrefix = qPrefix;
	}

}

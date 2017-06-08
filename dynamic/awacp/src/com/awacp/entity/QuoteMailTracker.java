package com.awacp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: QuoteMailTracker
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "QuoteMailTracker.listAll", query = "SELECT qmt FROM QuoteMailTracker qmt WHERE qmt.archived = 'false'"),
		@NamedQuery(name = "QuoteMailTracker.getByWsBidderAndTakeoffId", query = "SELECT qmt FROM QuoteMailTracker qmt WHERE qmt.archived = 'false' AND qmt.takeoffId =:takeoffId AND qmt.worksheetId =:worksheetId AND qmt.bidder.id =:bidderId"),
		@NamedQuery(name = "QuoteMailTracker.listByWorksheetAndTakeoffId", query = "SELECT qmt FROM QuoteMailTracker qmt WHERE qmt.archived = 'false' AND qmt.takeoffId =:takeoffId AND qmt.worksheetId =:worksheetId")
})
public class QuoteMailTracker extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long takeoffId;
	private String quote;
	private Bidder bidder;
	private Long worksheetId;

	private Integer mailSentCount; // Count by take off and bidder

	public Long getTakeoffId() {
		return takeoffId;
	}

	public void setTakeoffId(Long takeoffId) {
		this.takeoffId = takeoffId;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	@XmlElement(name = "bidder")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "BIDDERID", unique = false, nullable = false, updatable = true)
	@NotNull
	public Bidder getBidder() {
		return bidder;
	}

	public void setBidder(Bidder bidder) {
		this.bidder = bidder;
	}

	public Integer getMailSentCount() {
		return mailSentCount;
	}

	public void setMailSentCount(Integer mailSentCount) {
		this.mailSentCount = mailSentCount;
	}

	public Long getWorksheetId() {
		return worksheetId;
	}

	public void setWorksheetId(Long worksheetId) {
		this.worksheetId = worksheetId;
	}

}

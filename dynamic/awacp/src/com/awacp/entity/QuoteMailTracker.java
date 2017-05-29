package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: PageSetting
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "QuoteMailTracker.listAll", query = "SELECT qmt FROM QuoteMailTracker qmt WHERE qmt.archived = 'false'"),
		@NamedQuery(name = "QuoteMailTracker.listByTakeoffId", query = "SELECT qmt FROM QuoteMailTracker qmt WHERE qmt.archived = 'false' AND qmt.takeoffId =:takeoffId") })
public class QuoteMailTracker extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long takeoffId;
	private String quote;
	private Bidder bidder;

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

	public Bidder getBidder() {
		return bidder;
	}

	public void setBidder(Bidder bidder) {
		this.bidder = bidder;
	}
}

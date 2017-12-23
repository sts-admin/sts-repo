package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Orbf
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "Orbf.findByIdAndByOrderBookId", query = "SELECT o FROM Orbf o WHERE o.archived = 'false' AND o.id =:id AND o.orderBookId =:orderBookId ORDER BY o.dateCreated DESC"),
		@NamedQuery(name = "Orbf.findByOrderBookId", query = "SELECT o FROM Orbf o WHERE o.archived = 'false' AND o.orderBookId =:orderBookId ORDER BY o.dateCreated DESC")

})
public class Orbf extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String orbf;
	private Long orderBookId;
	private Long truckerOne;
	private Long truckerTwo;
	private Long truckerThree;
	private String trakingLinkOne;
	private String trackingLinkTwo;
	private String trackingLinkThree;
	private Calendar estDate;
	private String comment;

	private String statusOne;
	private String statusTwo;
	private String statusThree;

	public Orbf() {
		super();
	}

	public String getOrbf() {
		return orbf;
	}

	public void setOrbf(String orbf) {
		this.orbf = orbf;
	}

	public Long getTruckerOne() {
		return truckerOne;
	}

	public void setTruckerOne(Long truckerOne) {
		this.truckerOne = truckerOne;
	}

	public Long getTruckerTwo() {
		return truckerTwo;
	}

	public void setTruckerTwo(Long truckerTwo) {
		this.truckerTwo = truckerTwo;
	}

	public Long getTruckerThree() {
		return truckerThree;
	}

	public void setTruckerThree(Long truckerThree) {
		this.truckerThree = truckerThree;
	}

	public String getTrakingLinkOne() {
		return trakingLinkOne;
	}

	public void setTrakingLinkOne(String trakingLinkOne) {
		this.trakingLinkOne = trakingLinkOne;
	}

	public String getTrackingLinkTwo() {
		return trackingLinkTwo;
	}

	public void setTrackingLinkTwo(String trackingLinkTwo) {
		this.trackingLinkTwo = trackingLinkTwo;
	}

	public String getTrackingLinkThree() {
		return trackingLinkThree;
	}

	public void setTrackingLinkThree(String trackingLinkThree) {
		this.trackingLinkThree = trackingLinkThree;
	}

	public Calendar getEstDate() {
		return estDate;
	}

	public void setEstDate(Calendar estDate) {
		this.estDate = estDate;
	}

	@Lob
	@Column(length = 1024)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

	public String getStatusOne() {
		return statusOne;
	}

	public String getStatusTwo() {
		return statusTwo;
	}

	public String getStatusThree() {
		return statusThree;
	}

	public void setStatusOne(String statusOne) {
		this.statusOne = statusOne;
	}

	public void setStatusTwo(String statusTwo) {
		this.statusTwo = statusTwo;
	}

	public void setStatusThree(String statusThree) {
		this.statusThree = statusThree;
	}

}

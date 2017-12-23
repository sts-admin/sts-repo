package com.awacp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Tracking
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "Tracking.findByOrder", query = "SELECT t FROM Tracking t WHERE t.archived = 'false' AND t.orderBookId = :orderBookId ORDER BY t.dateCreated DESC")
})
public class Tracking extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Trucker truckerOne;
	private String trackingNumberOne;
	private Double freightOne;
	private Trucker truckerTwo;
	private String trackingNumberTwo;
	private Double freightTwo;
	private Trucker truckerThree;
	private String trackingNumberThree;
	private Double freightThree;
	private String orderBookNumber;
	private Long orderBookInvItemId;
	private String orderInvType;
	private String statusTextOne;
	private String statusTextTwo;
	private String statusTextThree;

	private boolean truckerOneDelivered;
	private boolean truckerTwoDelivered;
	private boolean truckerThreeDelivered;

	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private Long orderBookId;

	public Tracking() {
		super();
	}

	@XmlElement(name = "truckerOne")
	@OneToOne(optional = true, cascade = CascadeType.DETACH)
	@JoinColumn(name = "TRUCKERONEID", unique = false, nullable = false, updatable = true)
	public Trucker getTruckerOne() {
		return truckerOne;
	}

	public String getTrackingNumberOne() {
		return trackingNumberOne;
	}

	public Double getFreightOne() {
		return freightOne;
	}

	@XmlElement(name = "truckerTwo")
	@OneToOne(optional = true, cascade = CascadeType.DETACH)
	@JoinColumn(name = "TRUCKERTWOID", unique = false, nullable = true, updatable = true)
	public Trucker getTruckerTwo() {
		return truckerTwo;
	}

	public String getTrackingNumberTwo() {
		return trackingNumberTwo;
	}

	public Double getFreightTwo() {
		return freightTwo;
	}

	@XmlElement(name = "truckerThree")
	@OneToOne(optional = true, cascade = CascadeType.DETACH)
	@JoinColumn(name = "TRUCKERTHREEID", unique = false, nullable = true, updatable = true)
	public Trucker getTruckerThree() {
		return truckerThree;
	}

	public String getTrackingNumberThree() {
		return trackingNumberThree;
	}

	public Double getFreightThree() {
		return freightThree;
	}

	@Transient
	public String getOrderBookNumber() {
		return orderBookNumber;
	}

	public void setTruckerOne(Trucker truckerOne) {
		this.truckerOne = truckerOne;
	}

	public void setTrackingNumberOne(String trackingNumberOne) {
		this.trackingNumberOne = trackingNumberOne;
	}

	public void setFreightOne(Double freightOne) {
		this.freightOne = freightOne;
	}

	public void setTruckerTwo(Trucker truckerTwo) {
		this.truckerTwo = truckerTwo;
	}

	public void setTrackingNumberTwo(String trackingNumberTwo) {
		this.trackingNumberTwo = trackingNumberTwo;
	}

	public void setFreightTwo(Double freightTwo) {
		this.freightTwo = freightTwo;
	}

	public void setTruckerThree(Trucker truckerThree) {
		this.truckerThree = truckerThree;
	}

	public void setTrackingNumberThree(String trackingNumberThree) {
		this.trackingNumberThree = trackingNumberThree;
	}

	public void setFreightThree(Double freightThree) {
		this.freightThree = freightThree;
	}

	public void setOrderBookNumber(String orderBookNumber) {
		this.orderBookNumber = orderBookNumber;
	}

	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}

	public Long getOrderBookInvItemId() {
		return orderBookInvItemId;
	}

	public void setOrderBookInvItemId(Long orderBookInvItemId) {
		this.orderBookInvItemId = orderBookInvItemId;
	}

	public String getOrderInvType() {
		return orderInvType;
	}

	public void setOrderInvType(String orderInvType) {
		this.orderInvType = orderInvType;
	}

	public String getStatusTextOne() {
		return statusTextOne;
	}

	public String getStatusTextThree() {
		return statusTextThree;
	}

	public void setStatusTextOne(String statusTextOne) {
		this.statusTextOne = statusTextOne;
	}

	public void setStatusTextThree(String statusTextThree) {
		this.statusTextThree = statusTextThree;
	}

	public String getStatusTextTwo() {
		return statusTextTwo;
	}

	public void setStatusTextTwo(String statusTextTwo) {
		this.statusTextTwo = statusTextTwo;
	}

	public boolean isTruckerOneDelivered() {
		return truckerOneDelivered;
	}

	public boolean isTruckerTwoDelivered() {
		return truckerTwoDelivered;
	}

	public boolean isTruckerThreeDelivered() {
		return truckerThreeDelivered;
	}

	public void setTruckerOneDelivered(boolean truckerOneDelivered) {
		this.truckerOneDelivered = truckerOneDelivered;
	}

	public void setTruckerTwoDelivered(boolean truckerTwoDelivered) {
		this.truckerTwoDelivered = truckerTwoDelivered;
	}

	public void setTruckerThreeDelivered(boolean truckerThreeDelivered) {
		this.truckerThreeDelivered = truckerThreeDelivered;
	}

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

}

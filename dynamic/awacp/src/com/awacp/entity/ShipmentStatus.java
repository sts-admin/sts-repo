package com.awacp.entity;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "ShipmentStatus.findAll", query = "SELECT ss FROM ShipmentStatus ss WHERE ss.archived = 'false' ORDER BY ss.dateCreated DESC"),
		@NamedQuery(name = "ShipmentStatus.findByOrderBook", query = "SELECT ss FROM ShipmentStatus ss WHERE ss.archived = 'false' AND ss.orderBookId = :orderBookId ORDER BY ss.dateCreated DESC"),
		@NamedQuery(name = "ShipmentStatus.findByTruckerIdAndTrackingNumber", query = "SELECT ss FROM ShipmentStatus ss WHERE ss.archived = 'false' AND ss.truckerId = :truckerId AND ss.trackingNumber = :trackingNumber ORDER BY ss.dateCreated DESC")

})
public class ShipmentStatus extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long orderBookId;
	private Long truckerId;
	private Long trackingId;

	private String prevStatusText;
	private String currStatusText;
	private String deliveredOn;
	private String billedOn;
	private String shipmentCategory;
	private String weight;

	private String trackingNumber;

	private boolean delivered;
	private String serviceProvider;
	private String deliveredTo;
	private String receivedBy;
	private String leftAt;
	private String lastLine;
	private String trackingUrl;

	// Transient
	private String connectionStatus;
	private String providerLogo;

	private boolean podGenerated;

	private String podUrl;

	public Long getOrderBookId() {
		return orderBookId;
	}

	public String getPrevStatusText() {
		return prevStatusText;
	}

	public String getCurrStatusText() {
		return currStatusText;
	}

	public String getDeliveredOn() {
		return deliveredOn;
	}

	public String getBilledOn() {
		return billedOn;
	}

	public String getShipmentCategory() {
		return shipmentCategory;
	}

	public String getWeight() {
		return weight;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

	public void setPrevStatusText(String prevStatusText) {
		this.prevStatusText = prevStatusText;
	}

	public void setCurrStatusText(String currStatusText) {
		this.currStatusText = currStatusText;
	}

	public void setDeliveredOn(String deliverOn) {
		this.deliveredOn = deliverOn;
	}

	public void setBilledOn(String billedOn) {
		this.billedOn = billedOn;
	}

	public void setShipmentCategory(String shipmentCategory) {
		this.shipmentCategory = shipmentCategory;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Long getTruckerId() {
		return truckerId;
	}

	public void setTruckerId(Long truckerId) {
		this.truckerId = truckerId;
	}

	@Transient
	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public Long getTrackingId() {
		return trackingId;
	}

	public boolean isDelivered() {
		return delivered;
	}

	public void setTrackingId(Long trackingId) {
		this.trackingId = trackingId;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public String getTrackingNumber() {
		return trackingNumber;
	}

	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}

	public String getServiceProvider() {
		return serviceProvider;
	}

	public void setServiceProvider(String serviceProvider) {
		this.serviceProvider = serviceProvider;
	}

	public String getDeliveredTo() {
		return deliveredTo;
	}

	public String getLeftAt() {
		return leftAt;
	}

	public void setDeliveredTo(String deliveredTo) {
		this.deliveredTo = deliveredTo;
	}

	public void setLeftAt(String leftAt) {
		this.leftAt = leftAt;
	}

	public String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(String receivedBy) {
		this.receivedBy = receivedBy;
	}

	@Transient
	public String getProviderLogo() {
		return providerLogo;
	}

	public void setProviderLogo(String providerLogo) {
		this.providerLogo = providerLogo;
	}

	@Transient
	public String getLastLine() {
		return lastLine;
	}

	public void setLastLine(String lastLine) {
		this.lastLine = lastLine;
	}

	@Transient
	public boolean isPodGenerated() {
		return podGenerated;
	}

	public void setPodGenerated(boolean podGenerated) {
		this.podGenerated = podGenerated;
	}

	public String getTrackingUrl() {
		return trackingUrl;
	}

	public void setTrackingUrl(String trackingUrl) {
		this.trackingUrl = trackingUrl;
	}

	@Transient
	public String getPodUrl() {
		return podUrl;
	}

	public void setPodUrl(String podUrl) {
		this.podUrl = podUrl;
	}

}

package com.awacp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: FactoryClaim
 *
 */
@Entity
@XmlRootElement
public class TruckerClaim extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String orderBookNumber; // Unique order book number
	private String orbf;
	private Contractor contractor;
	private String tracking;
	private Trucker trucker;
	private String claim;
	private Double claimAmount;
	private String reasonForClaim;
	private String userCode; // Code of the User created this record.
	private Long salesmanId;

	private Long contractorId;

	private Long truckerId;
	
	private String status;
	
	private String salesPersonName;

	public TruckerClaim() {
		super();
	}

	public String getOrbf() {
		return orbf;
	}

	@XmlElement(name = "contractor")
	@OneToOne(optional = true, cascade = CascadeType.DETACH)
	@JoinColumn(name = "CONTRACTORID", unique = false, nullable = true, updatable = true)
	public Contractor getContractor() {
		return contractor;
	}

	@XmlElement(name = "trucker")
	@OneToOne(optional = true, cascade = CascadeType.DETACH)
	@JoinColumn(name = "TRUCKERID", unique = false, nullable = true, updatable = true)
	public Trucker getTrucker() {
		return trucker;
	}

	public String getClaim() {
		return claim;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public String getReasonForClaim() {
		return reasonForClaim;
	}

	public String getUserCode() {
		return userCode;
	}

	public Long getSalesmanId() {
		return salesmanId;
	}

	public void setOrbf(String orbf) {
		this.orbf = orbf;
	}

	public void setContractor(Contractor contractor) {
		this.contractor = contractor;
	}

	public void setTrucker(Trucker trucker) {
		this.trucker = trucker;
	}

	public void setClaim(String claim) {
		this.claim = claim;
	}

	public String getOrderBookNumber() {
		return orderBookNumber;
	}

	public void setOrderBookNumber(String orderBookNumber) {
		this.orderBookNumber = orderBookNumber;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public void setReasonForClaim(String reasonForClaim) {
		this.reasonForClaim = reasonForClaim;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public void setSalesmanId(Long salesmanId) {
		this.salesmanId = salesmanId;
	}

	public String getTracking() {
		return tracking;
	}

	public void setTracking(String tracking) {
		this.tracking = tracking;
	}

	@Transient
	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	@Transient
	public Long getTruckerId() {
		return truckerId;
	}

	public void setTruckerId(Long truckerId) {
		this.truckerId = truckerId;
	}

	
	public String getStatus() {
		return status;
	}

	@Transient
	public String getSalesPersonName() {
		return salesPersonName;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}
	
	

}

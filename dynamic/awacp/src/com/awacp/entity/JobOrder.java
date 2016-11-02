package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: JobOrder
 *
 */
@Entity
@XmlRootElement
public class JobOrder extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String orderId;
	private String poName;
	private String jobName;
	private Long salesPersonId;
	private String jobAddress;
	private Long contractorId;
	private Long engineerId;
	private Long architectureId;
	private Double quotedAmount;
	private Double billingAmount;
	private String vavControls;
	private String comments;
	private Calendar dateEntered;
	private String quoteId;

	// Transient
	private String salesPersonName;
	private String contractorName;
	private String engineerName;
	private String architectureName;

	public JobOrder() {
		super();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPoName() {
		return poName;
	}

	public void setPoName(String poName) {
		this.poName = poName;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public Long getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(Long salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	public String getJobAddress() {
		return jobAddress;
	}

	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public Long getEngineerId() {
		return engineerId;
	}

	public void setEngineerId(Long engineerId) {
		this.engineerId = engineerId;
	}

	public Long getArchitectureId() {
		return architectureId;
	}

	public void setArchitectureId(Long architectureId) {
		this.architectureId = architectureId;
	}

	public Double getQuotedAmount() {
		return quotedAmount;
	}

	public void setQuotedAmount(Double quotedAmount) {
		this.quotedAmount = quotedAmount;
	}

	public Double getBillingAmount() {
		return billingAmount;
	}

	public void setBillingAmount(Double billingAmount) {
		this.billingAmount = billingAmount;
	}

	public String getVavControls() {
		return vavControls;
	}

	public void setVavControls(String vavControls) {
		this.vavControls = vavControls;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Calendar getDateEntered() {
		return dateEntered;
	}

	public void setDateEntered(Calendar dateEntered) {
		this.dateEntered = dateEntered;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getSalesPersonName() {
		return salesPersonName;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}

	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	public String getEngineerName() {
		return engineerName;
	}

	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}

	public String getArchitectureName() {
		return architectureName;
	}

	public void setArchitectureName(String architectureName) {
		this.architectureName = architectureName;
	}

}

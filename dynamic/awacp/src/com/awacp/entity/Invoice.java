package com.awacp.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Invoice
 *
 */
@Entity
@XmlRootElement

@NamedQueries({
		@NamedQuery(name = "Invoice.getByOrderId", query = "SELECT inv FROM Invoice inv WHERE inv.archived = 'false' AND inv.jobOrderId = :jobOrderId"),
		@NamedQuery(name = "Invoice.getArchivedInvoiceByOrderId", query = "SELECT inv FROM Invoice inv WHERE inv.archived = 'true' AND inv.jobOrderId = :jobOrderId")
})
public class Invoice extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long jobOrderId; // Job order primary key

	private String awOrderNumber; // A unique job order number
	private Long shippedViaId;

	private Long taxRateId;
	private Calendar shipDate;
	private String shipment;
	private Long item1Id;
	private Long item2Id;
	private Long item3Id;
	private Long item4Id;
	private Long item5Id;
	private String taxExemptNumber;
	private String prePayCheckNumber;
	private Double prePayAmount = 0.0D;
	private Double partialPayment = 0.0D;

	private String salesPersonCode;

	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private Double totalCost = 0.0D;
	private Double totalProfit = 0.0D;
	private Double profitPercent = 0.0D;

	private Double billableAmount = 0.0D;
	private Double totalPayment = 0.0D;
	private String shipTo;

	// Transient
	private JobOrder jobOrder;
	private String profitOrLossLabel;

	private Set<ProfitSheetItem> profitSheetItems;

	public Invoice() {
		super();
	}

	@Transient
	public JobOrder getJobOrder() {
		return jobOrder;
	}

	public void setJobOrder(JobOrder jobOrder) {
		this.jobOrder = jobOrder;
	}

	public String getAwOrderNumber() {
		return awOrderNumber;
	}

	public void setAwOrderNumber(String awOrderNumber) {
		this.awOrderNumber = awOrderNumber;
	}

	public Calendar getShipDate() {
		return shipDate;
	}

	public void setShipDate(Calendar shipDate) {
		this.shipDate = shipDate;
	}

	public String getShipment() {
		return shipment;
	}

	public void setShipment(String shipment) {
		this.shipment = shipment;
	}

	public String getTaxExemptNumber() {
		return taxExemptNumber;
	}

	public void setTaxExemptNumber(String taxExemptNumber) {
		this.taxExemptNumber = taxExemptNumber;
	}

	public String getPrePayCheckNumber() {
		return prePayCheckNumber;
	}

	public void setPrePayCheckNumber(String prePayCheckNumber) {
		this.prePayCheckNumber = prePayCheckNumber;
	}

	public Double getPrePayAmount() {
		return prePayAmount;
	}

	public void setPrePayAmount(Double prePayAmount) {
		this.prePayAmount = prePayAmount;
	}

	public String getSalesPersonCode() {
		return salesPersonCode;
	}

	public void setSalesPersonCode(String salesPersonCode) {
		this.salesPersonCode = salesPersonCode;
	}

	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}

	@XmlElement(name = "profitSheetItems")
	@Transient
	public Set<ProfitSheetItem> getProfitSheetItems() {
		return profitSheetItems;
	}

	public void setProfitSheetItems(Set<ProfitSheetItem> profitSheetItems) {
		this.profitSheetItems = profitSheetItems;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public Double getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(Double totalProfit) {
		this.totalProfit = totalProfit;
	}

	public Double getProfitPercent() {
		return profitPercent;
	}

	public void setProfitPercent(Double profitPercent) {
		this.profitPercent = profitPercent;
	}

	@NotNull
	public Long getJobOrderId() {
		return jobOrderId;
	}

	public void setJobOrderId(Long jobOrderId) {
		this.jobOrderId = jobOrderId;
	}

	public Double getPartialPayment() {
		return partialPayment;
	}

	public void setPartialPayment(Double partialPayment) {
		this.partialPayment = partialPayment;
	}

	@Transient
	public String getProfitOrLossLabel() {
		return profitOrLossLabel;
	}

	public void setProfitOrLossLabel(String profitOrLossLabel) {
		this.profitOrLossLabel = profitOrLossLabel;
	}

	public Double getBillableAmount() {
		return billableAmount;
	}

	public void setBillableAmount(Double billableAmount) {
		this.billableAmount = billableAmount;
	}

	public Double getTotalPayment() {
		return totalPayment;
	}

	public void setTotalPayment(Double totalPayment) {
		this.totalPayment = totalPayment;
	}

	@NotNull
	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

	public Long getShippedViaId() {
		return shippedViaId;
	}

	public void setShippedViaId(Long shippedViaId) {
		this.shippedViaId = shippedViaId;
	}

	@NotNull
	public Long getTaxRateId() {
		return taxRateId;
	}

	public void setTaxRateId(Long taxRateId) {
		this.taxRateId = taxRateId;
	}

	public Long getItem1Id() {
		return item1Id;
	}

	public void setItem1Id(Long item1Id) {
		this.item1Id = item1Id;
	}

	public Long getItem2Id() {
		return item2Id;
	}

	public void setItem2Id(Long item2Id) {
		this.item2Id = item2Id;
	}

	public Long getItem3Id() {
		return item3Id;
	}

	public void setItem3Id(Long item3Id) {
		this.item3Id = item3Id;
	}

	public Long getItem4Id() {
		return item4Id;
	}

	public void setItem4Id(Long item4Id) {
		this.item4Id = item4Id;
	}

	public Long getItem5Id() {
		return item5Id;
	}

	public void setItem5Id(Long item5Id) {
		this.item5Id = item5Id;
	}
}

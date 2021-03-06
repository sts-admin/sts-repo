package com.awacp.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
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
		@NamedQuery(name = "Invoice.getArchivedInvoiceByOrderId", query = "SELECT inv FROM Invoice inv WHERE inv.archived = 'true' AND inv.jobOrderId = :jobOrderId") })
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

	private String shippedVia;
	private Double taxRate = 0.0D;
	private String taxRateString;

	private String item1Name;
	private String item2Name;
	private String item3Name;
	private String item4Name;
	private String item5Name;

	private String invoiceSource;

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

	@Transient
	public String getShippedVia() {
		return shippedVia;
	}

	public void setShippedVia(String shippedVia) {
		this.shippedVia = shippedVia;
	}

	@Transient
	public Double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	@Transient
	public String getItem1Name() {
		return item1Name;
	}

	public void setItem1Name(String item1Name) {
		this.item1Name = item1Name;
	}

	@Transient
	public String getItem2Name() {
		return item2Name;
	}

	public void setItem2Name(String item2Name) {
		this.item2Name = item2Name;
	}

	@Transient
	public String getItem3Name() {
		return item3Name;
	}

	public void setItem3Name(String item3Name) {
		this.item3Name = item3Name;
	}

	@Transient
	public String getItem4Name() {
		return item4Name;
	}

	public void setItem4Name(String item4Name) {
		this.item4Name = item4Name;
	}

	@Transient
	public String getItem5Name() {
		return item5Name;
	}

	public void setItem5Name(String item5Name) {
		this.item5Name = item5Name;
	}

	@Transient
	public String getTaxRateString() {
		return taxRateString;
	}

	public void setTaxRateString(String taxRateString) {
		this.taxRateString = taxRateString;
	}

	@Transient
	public String getInvoiceSource() {
		return invoiceSource;
	}

	public void setInvoiceSource(String invoiceSource) {
		this.invoiceSource = invoiceSource;
	}

	@PrePersist
	@PreUpdate
	public void prePersist() {
		if (this.getPrePayAmount() == null || this.getPrePayAmount() == Double.POSITIVE_INFINITY
				|| this.getPrePayAmount() == Double.NEGATIVE_INFINITY) {
			this.setPrePayAmount(0.0D);
		}
		if (this.getPartialPayment() == null || this.getPartialPayment() == Double.POSITIVE_INFINITY
				|| this.getPartialPayment() == Double.NEGATIVE_INFINITY) {
			this.setPartialPayment(0.0D);
		}
		if (this.getTotalCost() == null || this.getTotalCost() == Double.POSITIVE_INFINITY
				|| this.getTotalCost() == Double.NEGATIVE_INFINITY) {
			this.setTotalCost(0.0D);
		}
		if (this.getTotalProfit() == null || this.getTotalProfit() == Double.POSITIVE_INFINITY
				|| this.getTotalProfit() == Double.NEGATIVE_INFINITY) {
			this.setTotalProfit(0.0D);
		}
		if (this.getProfitPercent() == null || this.getProfitPercent() == Double.POSITIVE_INFINITY
				|| this.getProfitPercent() == Double.NEGATIVE_INFINITY) {
			this.setProfitPercent(0.0D);
		}
		if (this.getBillableAmount() == null || this.getBillableAmount() == Double.POSITIVE_INFINITY
				|| this.getBillableAmount() == Double.NEGATIVE_INFINITY) {
			this.setBillableAmount(0.0D);
		}
		if (this.getTotalPayment() == null || this.getTotalPayment() == Double.POSITIVE_INFINITY
				|| this.getTotalPayment() == Double.NEGATIVE_INFINITY) {
			this.setTotalPayment(0.0D);
		}
	}

}

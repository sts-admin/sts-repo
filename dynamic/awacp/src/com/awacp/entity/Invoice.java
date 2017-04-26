package com.awacp.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
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
		@NamedQuery(name = "Invoice.getByOrderId", query = "SELECT inv FROM Invoice inv WHERE inv.archived = 'false' AND inv.jobOrderId = :jobOrderId") })
public class Invoice extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long jobOrderId;

	private String awOrderNumber;
	private ShippedVia shippedVia;

	private TaxEntry taxRate;
	private Calendar shipDate;
	private String shipment;
	private ItemShipped item1;
	private ItemShipped item2;
	private ItemShipped item3;
	private ItemShipped item4;
	private ItemShipped item5;
	private String taxExemptNumber;
	private String prePayCheckNumber;
	private Double prePayAmount;
	private Double partialPayment;

	private String salesPersonCode;

	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private Set<ProfitSheetItem> profitSheetItems;

	private Double totalCost;
	private Double totalProfit;
	private Double profitPercent;

	private Double billableAmount;
	private Double balancePayable;

	// Transient
	private JobOrder jobOrder;
	private String profitOrLossLabel;

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

	@XmlElement(name = "taxRate")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "TAX_RATE", unique = false, nullable = true, updatable = true)
	public TaxEntry getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(TaxEntry taxRate) {
		this.taxRate = taxRate;
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

	@XmlElement(name = "item1")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "ITEM1", unique = false, nullable = true, updatable = true)
	public ItemShipped getItem1() {
		return item1;
	}

	public void setItem1(ItemShipped item1) {
		this.item1 = item1;
	}

	@XmlElement(name = "item2")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "ITEM2", unique = false, nullable = true, updatable = true)
	public ItemShipped getItem2() {
		return item2;
	}

	public void setItem2(ItemShipped item2) {
		this.item2 = item2;
	}

	@XmlElement(name = "item3")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "ITEM3", unique = false, nullable = true, updatable = true)
	public ItemShipped getItem3() {
		return item3;
	}

	public void setItem3(ItemShipped item3) {
		this.item3 = item3;
	}

	@XmlElement(name = "item4")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "ITEM4", unique = false, nullable = true, updatable = true)
	public ItemShipped getItem4() {
		return item4;
	}

	public void setItem4(ItemShipped item4) {
		this.item4 = item4;
	}

	@XmlElement(name = "item5")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "ITEM5", unique = false, nullable = true, updatable = true)
	public ItemShipped getItem5() {
		return item5;
	}

	public void setItem5(ItemShipped item5) {
		this.item5 = item5;
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
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "JO_INV_PS", joinColumns = @JoinColumn(name = "INVID"), inverseJoinColumns = @JoinColumn(name = "PSITEMID"))
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

	@XmlElement(name = "shippedVia")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "SHIPPED_VIA", unique = false, nullable = true, updatable = true)
	public ShippedVia getShippedVia() {
		return shippedVia;
	}

	public void setShippedVia(ShippedVia shippedVia) {
		this.shippedVia = shippedVia;
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

	public Double getBalancePayable() {
		return balancePayable;
	}

	public void setBalancePayable(Double balancePayable) {
		this.balancePayable = balancePayable;
	}

}

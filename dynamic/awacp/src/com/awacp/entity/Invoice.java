package com.awacp.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: Invoice
 *
 */
@Entity
@XmlRootElement
public class Invoice extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private JobOrder jobOrder;
	private String awOrderNumber;
	private String shipTo;

	private TaxEntry taxRate;
	private Calendar shipDate;
	private String shipment;
	private ItemShipped item1;
	private ItemShipped item2;
	private ItemShipped item3;
	private String item4;
	private String item5;
	private String item6;
	private String item7;
	private String item8;
	private String taxExemptNumber;
	private String prePayCheckNumber;
	private Double prePayAmount;

	private String salesPersonCode;

	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private Set<ProfitSheetItem> profitSheetItems;

	public Invoice() {
		super();
	}

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

	public String getShipTo() {
		return shipTo;
	}

	public void setShipTo(String shipTo) {
		this.shipTo = shipTo;
	}

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

	public ItemShipped getItem1() {
		return item1;
	}

	public void setItem1(ItemShipped item1) {
		this.item1 = item1;
	}

	public ItemShipped getItem2() {
		return item2;
	}

	public void setItem2(ItemShipped item2) {
		this.item2 = item2;
	}

	public ItemShipped getItem3() {
		return item3;
	}

	public void setItem3(ItemShipped item3) {
		this.item3 = item3;
	}

	public String getItem4() {
		return item4;
	}

	public void setItem4(String item4) {
		this.item4 = item4;
	}

	public String getItem5() {
		return item5;
	}

	public void setItem5(String item5) {
		this.item5 = item5;
	}

	public String getItem6() {
		return item6;
	}

	public void setItem6(String item6) {
		this.item6 = item6;
	}

	public String getItem7() {
		return item7;
	}

	public void setItem7(String item7) {
		this.item7 = item7;
	}

	public String getItem8() {
		return item8;
	}

	public void setItem8(String item8) {
		this.item8 = item8;
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
	@ManyToMany(cascade = { CascadeType.DETACH })
	@JoinTable(name = "WS_MND_PDNI", joinColumns = @JoinColumn(name = "MND") , inverseJoinColumns = @JoinColumn(name = "PDNI") )
	public Set<ProfitSheetItem> getProfitSheetItems() {
		return profitSheetItems;
	}

	public void setProfitSheetItems(Set<ProfitSheetItem> profitSheetItems) {
		this.profitSheetItems = profitSheetItems;
	}

}

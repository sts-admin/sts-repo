package com.awacp.entity;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: ProfitSheetItem
 *
 */
@Entity
@XmlRootElement
public class ProfitSheetItem extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Long invoiceId;
	private String awacpPoNumber;
	private String orbf;
	private String facInv;
	private Double invAmount;
	private Double freight;

	private Double totalCost;
	private Double totalProfit;
	private Double profitPercent;

	public ProfitSheetItem() {
		super();
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getAwacpPoNumber() {
		return awacpPoNumber;
	}

	public void setAwacpPoNumber(String awacpPoNumber) {
		this.awacpPoNumber = awacpPoNumber;
	}

	public String getOrbf() {
		return orbf;
	}

	public void setOrbf(String orbf) {
		this.orbf = orbf;
	}

	public String getFacInv() {
		return facInv;
	}

	public void setFacInv(String facInv) {
		this.facInv = facInv;
	}

	public Double getInvAmount() {
		return invAmount;
	}

	public void setInvAmount(Double invAmount) {
		this.invAmount = invAmount;
	}

	public Double getFreight() {
		return freight;
	}

	public void setFreight(Double freight) {
		this.freight = freight;
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

}

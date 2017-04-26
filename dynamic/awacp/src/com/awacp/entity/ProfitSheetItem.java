package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
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
	private String awacpPoNumber;
	private String orbf;
	private String facInv;
	private Double invAmount;
	private Double freight;
	private String comment;

	private boolean manual;

	public ProfitSheetItem() {
		super();
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

	@Lob
	@Column(length = 1024)
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isManual() {
		return manual;
	}

	public void setManual(boolean manual) {
		this.manual = manual;
	}

}

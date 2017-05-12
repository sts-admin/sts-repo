package com.awacp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: ProfitSheetItem
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
	@NamedQuery(name = "ProfitSheetItem.getAllByInvoiceId", query = "SELECT psi FROM ProfitSheetItem psi WHERE psi.archived = 'false' AND psi.invoiceId = :invoiceId"),
	@NamedQuery(name = "ProfitSheetItem.getManualItemByInvoiceId", query = "SELECT psi FROM ProfitSheetItem psi WHERE psi.archived = 'false' AND psi.invoiceId = :invoiceId AND psi.manual = 'true' "),
	@NamedQuery(name = "ProfitSheetItem.getNonManualItemByInvoiceId", query = "SELECT psi FROM ProfitSheetItem psi WHERE psi.archived = 'false' AND psi.invoiceId = :invoiceId AND psi.manual = 'false' "),	
	@NamedQuery(name = "ProfitSheetItem.getByOrderBookId", query = "SELECT psi FROM ProfitSheetItem psi WHERE psi.archived = 'false' AND psi.orderBookId = :orderBookId"),
	@NamedQuery(name = "ProfitSheetItem.getNonManualItemByInvoiceAndOrderId", query = "SELECT psi FROM ProfitSheetItem psi WHERE psi.archived = 'false' AND psi.orderBookId = :orderBookId AND psi.invoiceId = :invoiceId AND psi.manual = 'false'"),
	@NamedQuery(name = "ProfitSheetItem.getAllByOrderBookId", query = "SELECT psi FROM ProfitSheetItem psi WHERE psi.orderBookId = :orderBookId")
})
public class ProfitSheetItem extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String awacpPoNumber;
	private String orbf;
	private String facInv;
	private Double invAmount;
	private Double freight;
	private String comment;
	private Long invoiceId;
	private String obCategory;
	private Long orderBookId;

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

	@NotNull
	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	@NotNull
	public String getObCategory() {
		return obCategory;
	}

	public void setObCategory(String obCategory) {
		this.obCategory = obCategory;
	}

	public Long getOrderBookId() {
		return orderBookId;
	}

	public void setOrderBookId(Long orderBookId) {
		this.orderBookId = orderBookId;
	}

}

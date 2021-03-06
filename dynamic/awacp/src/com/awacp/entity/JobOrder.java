package com.awacp.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.constant.StsCoreConstant;
import com.sts.core.entity.BaseEntity;

/**
 * Entity implementation class for Entity: JobOrder
 *
 */
@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "JobOrder.getByOrderId", query = "SELECT jo FROM JobOrder jo WHERE jo.archived = 'false' AND LOWER(jo.orderNumber) = :orderNumber"),
		@NamedQuery(name = "JobOrder.getByInvoiceStatus", query = "SELECT jo FROM JobOrder jo WHERE jo.archived = 'false' AND LOWER(jo.invoiceMode) = :invoiceStatus ORDER BY jo.dateCreated DESC"),
		@NamedQuery(name = "JobOrder.getCountByInvoiceStatus", query = "SELECT COUNT(jo.id) FROM JobOrder jo WHERE jo.archived = 'false' AND LOWER(jo.invoiceMode) = :invoiceStatus") })
public class JobOrder extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String orderNumber;
	private String poName;
	private String jobName;
	private Long salesPersonId;
	private Long recepientSpId;
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
	private Long takeoffId;
	private Long invoiceId;

	private String contractorName;
	private String engineerName;
	private String architectureName;
	private boolean billGenerated;

	private String invoiceMode;

	private boolean finalUpdate;

	private String finalUpdateStatus;

	// Transient
	private String salesPersonName;
	private String recepientSpName;

	private String userNameOrEmail;

	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private String[] jobOrderBookNumbers;

	private int joOneDocCount;
	private int joTwoDocCount;
	private int joThreeDocCount;
	private int joFourDocCount;
	private int joFiveDocCount;
	private int joSixDocCount;

	private int joDocCount;
	private int joXlsCount;
	private int joTaxDocCount;
	private int joPoDocCount;
	private int joUiDocCount;

	private boolean cancelled = false;

	// Transient
	private int year;
	private Calendar fromDate;
	private Calendar toDate;

	private int pageNumber;
	private int pageSize;
	private String status;
	private String jobStatus;

	private Double totalCost = 0.0D;
	private Double totalProfit = 0.0D;
	private Double profitPercent = 0.0D;
	private Double totalBillableAmount = 0.0D;

	public JobOrder() {
		super();
	}

	@NotNull
	@Column(length = 25)
	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
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

	@Transient
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

	@Transient
	public String getUserNameOrEmail() {
		return userNameOrEmail;
	}

	public void setUserNameOrEmail(String userNameOrEmail) {
		this.userNameOrEmail = userNameOrEmail;
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

	public Long getTakeoffId() {
		return takeoffId;
	}

	public void setTakeoffId(Long takeoffId) {
		this.takeoffId = takeoffId;
	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

	@Transient
	public String[] getJobOrderBookNumbers() {
		return jobOrderBookNumbers;
	}

	public void setJobOrderBookNumbers(String[] jobOrderBookNumbers) {
		this.jobOrderBookNumbers = jobOrderBookNumbers;
	}

	public boolean isBillGenerated() {
		return billGenerated;
	}

	public void setBillGenerated(boolean billGenerated) {
		this.billGenerated = billGenerated;
	}

	@NotNull
	@Column(length = 4)
	public String getInvoiceMode() {
		return invoiceMode;
	}

	public void setInvoiceMode(String invoiceMode) {
		this.invoiceMode = invoiceMode;
	}

	@PrePersist
	public void prePersist() {
		if (getInvoiceMode() == null || getInvoiceMode().trim().length() <= 0) {
			setInvoiceMode(StsCoreConstant.INV_MODE_BILL);
		}
	}

	@Transient
	public int getJoOneDocCount() {
		return joOneDocCount;
	}

	public void setJoOneDocCount(int joOneDocCount) {
		this.joOneDocCount = joOneDocCount;
	}

	@Transient
	public int getJoTwoDocCount() {
		return joTwoDocCount;
	}

	public void setJoTwoDocCount(int joTwoDocCount) {
		this.joTwoDocCount = joTwoDocCount;
	}

	@Transient
	public int getJoThreeDocCount() {
		return joThreeDocCount;
	}

	public void setJoThreeDocCount(int joThreeDocCount) {
		this.joThreeDocCount = joThreeDocCount;
	}

	@Transient
	public int getJoFourDocCount() {
		return joFourDocCount;
	}

	public void setJoFourDocCount(int joFourDocCount) {
		this.joFourDocCount = joFourDocCount;
	}

	@Transient
	public int getJoFiveDocCount() {
		return joFiveDocCount;
	}

	public void setJoFiveDocCount(int joFiveDocCount) {
		this.joFiveDocCount = joFiveDocCount;
	}

	@Transient
	public int getJoSixDocCount() {
		return joSixDocCount;
	}

	public void setJoSixDocCount(int joSixDocCount) {
		this.joSixDocCount = joSixDocCount;
	}

	@Transient
	public int getJoDocCount() {
		return joDocCount;
	}

	public void setJoDocCount(int joDocCount) {
		this.joDocCount = joDocCount;
	}

	@Transient
	public int getJoXlsCount() {
		return joXlsCount;
	}

	public void setJoXlsCount(int joXlsCount) {
		this.joXlsCount = joXlsCount;
	}

	@Transient
	public int getJoTaxDocCount() {
		return joTaxDocCount;
	}

	public void setJoTaxDocCount(int joTaxDocCount) {
		this.joTaxDocCount = joTaxDocCount;
	}

	@Transient
	public int getJoPoDocCount() {
		return joPoDocCount;
	}

	public void setJoPoDocCount(int joPoDocCount) {
		this.joPoDocCount = joPoDocCount;
	}

	@Transient
	public int getJoUiDocCount() {
		return joUiDocCount;
	}

	public void setJoUiDocCount(int joUiDocCount) {
		this.joUiDocCount = joUiDocCount;
	}

	public boolean isFinalUpdate() {
		return finalUpdate;
	}

	public void setFinalUpdate(boolean finalUpdate) {
		this.finalUpdate = finalUpdate;
	}

	public Long getRecepientSpId() {
		return recepientSpId;
	}

	public void setRecepientSpId(Long recepientSpId) {
		this.recepientSpId = recepientSpId;
	}

	@Transient
	public String getRecepientSpName() {
		return recepientSpName;
	}

	public void setRecepientSpName(String recepientSpName) {
		this.recepientSpName = recepientSpName;
	}

	@Transient
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	@Transient
	public Calendar getFromDate() {
		return fromDate;
	}

	public void setFromDate(Calendar fromDate) {
		this.fromDate = fromDate;
	}

	@Transient
	public Calendar getToDate() {
		return toDate;
	}

	public void setToDate(Calendar toDate) {
		this.toDate = toDate;
	}

	@Transient
	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	@Transient
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@Transient
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	@Transient
	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	@Transient
	public Double getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(Double totalProfit) {
		this.totalProfit = totalProfit;
	}

	@Transient
	public Double getProfitPercent() {
		return profitPercent;
	}

	public void setProfitPercent(Double profitPercent) {
		this.profitPercent = profitPercent;
	}

	@Transient
	public Double getTotalBillableAmount() {
		return totalBillableAmount;
	}

	public void setTotalBillableAmount(Double totalBillableAmount) {
		this.totalBillableAmount = totalBillableAmount;
	}

	@Transient
	public String getFinalUpdateStatus() {
		return finalUpdateStatus;
	}

	public void setFinalUpdateStatus(String finalUpdateStatus) {
		this.finalUpdateStatus = finalUpdateStatus;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}

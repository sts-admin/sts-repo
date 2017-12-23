package com.awacp.entity;

import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.sts.core.entity.BaseEntity;

@Entity
@XmlRootElement
@NamedQueries({
		@NamedQuery(name = "OrderBook.getByJobOrderId", query = "SELECT ob FROM OrderBook ob WHERE ob.archived = 'false' AND ob.jobId = :jobId ORDER BY ob.dateCreated DESC"),
		@NamedQuery(name = "OrderBook.getByNumber", query = "SELECT ob FROM OrderBook ob WHERE ob.archived = 'false' AND ob.orderBookNumber = :orderBookNumber ORDER BY ob.dateCreated DESC"),
		@NamedQuery(name = "OrderBook.getCountByJobOrderId", query = "SELECT COUNT(ob.id) FROM OrderBook ob WHERE ob.archived = 'false' AND ob.jobId = :jobId"),
		@NamedQuery(name = "OrderBook.getCancelledCountByJobOrderId", query = "SELECT COUNT(ob.id) FROM OrderBook ob WHERE ob.archived = 'false' AND ob.jobId = :jobId AND ob.cancelled = 'true'"),
		@NamedQuery(name = "OrderBook.getOrderBookNumbersByOrderId", query = "SELECT new com.awacp.entity.OrderBook(ob.orderBookNumber) FROM OrderBook ob WHERE ob.archived = 'false' AND ob.jobId = :jobId") })
public class OrderBook extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long jobId; // Job Order primary key
	private String jobOrderNumber; // Unique job order number
	private String orderBookNumber; // Unique order book number
	private String obCategory;
	private Long salesPersonId;

	private String specialInstruction;
	private Long factoryId; // If regular factory
	private String factoryType;
	private String orbf;
	private Calendar estDate;
	private Long contractorId;
	private Long shipToId;

	private String attn;
	private String jobName;
	private String jobAddress;
	private String comment;

	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private String userNameOrEmail;

	private Long takeoffId;
	private String quoteId;
	private boolean cancelled = false;

	// Transient fields
	private String contractorName;
	private String shipToName;
	private String factoryName; // None regular factory like AW, AWF, SBC, SPL,
								// Q, J
	private String salesPersonName;

	private Set<OrderBookInvItem> invItems;

	private int obADocCount;
	private int obYXlsDocCount;
	private int obAckPdfDocCount;
	private int obFrtPdfDocCount;

	private int year;
	private Calendar fromDate;
	private Calendar toDate;

	private int pageNumber;
	private int pageSize;

	private Double netCost;

	private JobOrder jobOrder;

	private String po;

	private boolean delivered;

	private List<ShipmentStatus> deliveryStatuses;

	public OrderBook() {
		super();
	}

	public OrderBook(String orderBookNumber) {
		this.orderBookNumber = orderBookNumber;
	}

	@NotNull
	@Column(nullable = false)
	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Transient
	public String getSalesPersonName() {
		return salesPersonName;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}

	public String getSpecialInstruction() {
		return specialInstruction;
	}

	public void setSpecialInstruction(String specialInstruction) {
		this.specialInstruction = specialInstruction;
	}

	public Long getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(Long factoryId) {
		this.factoryId = factoryId;
	}

	@Transient
	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getOrbf() {
		return orbf;
	}

	public void setOrbf(String orbf) {
		this.orbf = orbf;
	}

	public Calendar getEstDate() {
		return estDate;
	}

	public void setEstDate(Calendar estDate) {
		this.estDate = estDate;
	}

	public Long getContractorId() {
		return contractorId;
	}

	public void setContractorId(Long contractorId) {
		this.contractorId = contractorId;
	}

	public Long getShipToId() {
		return shipToId;
	}

	public void setShipToId(Long shipToId) {
		this.shipToId = shipToId;
	}

	public String getAttn() {
		return attn;
	}

	public void setAttn(String attn) {
		this.attn = attn;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobAddress() {
		return jobAddress;
	}

	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Transient
	public String getContractorName() {
		return contractorName;
	}

	public void setContractorName(String contractorName) {
		this.contractorName = contractorName;
	}

	@Transient
	public String getShipToName() {
		return shipToName;
	}

	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
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

	@Transient
	public String getUserNameOrEmail() {
		return userNameOrEmail;
	}

	public void setUserNameOrEmail(String userNameOrEmail) {
		this.userNameOrEmail = userNameOrEmail;
	}

	public Long getSalesPersonId() {
		return salesPersonId;
	}

	public void setSalesPersonId(Long salesPersonId) {
		this.salesPersonId = salesPersonId;
	}

	@XmlElement(name = "invItems")
	@Transient
	public Set<OrderBookInvItem> getInvItems() {
		return invItems;
	}

	public void setInvItems(Set<OrderBookInvItem> invItems) {
		this.invItems = invItems;
	}

	@NotNull
	@Column(nullable = false, length = 25)
	public String getJobOrderNumber() {
		return jobOrderNumber;
	}

	public void setJobOrderNumber(String jobOrderNumber) {
		this.jobOrderNumber = jobOrderNumber;
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public String getObCategory() {
		return obCategory;
	}

	public void setObCategory(String obCategory) {
		this.obCategory = obCategory;
	}

	@NotNull
	public Long getTakeoffId() {
		return takeoffId;
	}

	public void setTakeoffId(Long takeoffId) {
		this.takeoffId = takeoffId;
	}

	@NotNull
	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public String getOrderBookNumber() {
		return orderBookNumber;
	}

	public void setOrderBookNumber(String orderBookNumber) {
		this.orderBookNumber = orderBookNumber;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Transient
	public int getObADocCount() {
		return obADocCount;
	}

	public void setObADocCount(int obADocCount) {
		this.obADocCount = obADocCount;
	}

	@Transient
	public int getObYXlsDocCount() {
		return obYXlsDocCount;
	}

	public void setObYXlsDocCount(int obYXlsDocCount) {
		this.obYXlsDocCount = obYXlsDocCount;
	}

	@Transient
	public int getObAckPdfDocCount() {
		return obAckPdfDocCount;
	}

	public void setObAckPdfDocCount(int obAckPdfDocCount) {
		this.obAckPdfDocCount = obAckPdfDocCount;
	}

	@Transient
	public int getObFrtPdfDocCount() {
		return obFrtPdfDocCount;
	}

	public void setObFrtPdfDocCount(int obFrtPdfDocCount) {
		this.obFrtPdfDocCount = obFrtPdfDocCount;
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

	public String getFactoryType() {
		return factoryType;
	}

	public void setFactoryType(String factoryType) {
		this.factoryType = factoryType;
	}

	@Transient
	public Double getNetCost() {
		return netCost;
	}

	public void setNetCost(Double netCost) {
		this.netCost = netCost;
	}

	@Transient
	public JobOrder getJobOrder() {
		return jobOrder;
	}

	public void setJobOrder(JobOrder jobOrder) {
		this.jobOrder = jobOrder;
	}

	@Transient
	public String getPo() {
		return po;
	}

	public void setPo(String po) {
		this.po = po;
	}

	public boolean isDelivered() {
		return delivered;
	}

	@Transient
	public List<ShipmentStatus> getDeliveryStatuses() {
		return deliveryStatuses;
	}

	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}

	public void setDeliveryStatuses(List<ShipmentStatus> deliveryStatuses) {
		this.deliveryStatuses = deliveryStatuses;
	}

}

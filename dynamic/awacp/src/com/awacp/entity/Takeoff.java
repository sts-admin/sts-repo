package com.awacp.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
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
 * Entity implementation class for Entity: Takeoff
 *
 */
@Entity
@XmlRootElement

@NamedQueries({ @NamedQuery(name = "Takeoff.listAll", query = "SELECT t FROM Takeoff t WHERE t.archived = 'false' ORDER BY t.dateCreated DESC"),
		@NamedQuery(name = "Takeoff.countAll", query = "SELECT COUNT(t.id) FROM Takeoff t WHERE t.archived = 'false'"),
		@NamedQuery(name = "Takeoff.listNewTakeoffsForQuote", query = "SELECT t FROM Takeoff t WHERE t.archived = 'false' AND FUNC('ISNULL', t.quoteId)  ORDER BY t.dateCreated DESC"),
		@NamedQuery(name = "Takeoff.listTakeoffsForView", query = "SELECT t FROM Takeoff t WHERE t.archived = 'false' AND NOT FUNC('ISNULL', t.quoteId)  ORDER BY t.dateCreated DESC"),
		@NamedQuery(name = "Takeoff.getTakeoffByQuoteId", query = "SELECT t FROM Takeoff t WHERE t.archived = 'false' AND t.quoteId =:quoteId") })
public class Takeoff extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String takeoffId;
	private String quoteId;
	private Calendar quoteDate;
	private String quoteUserCode;
	private String quoteComment;
	private Long jobOrderId;
	private String jobOrderNumber;

	private Long salesPerson; // User id //required
	private String userCode;
	private Long architectureId; // User id
	private Long engineerId; // User id

	private String jobName; // required
	private String jobAddress; // required
	private String projectNumber;

	private Calendar drawingDate;
	private Calendar revisedDate;
	private Calendar dueDate;
	private String drawingReceivedFrom;
	private String takeOffComment;

	private String createdByUserCode; // Code of the User created this record.
	private String updatedByUserCode; // Code of the user update this record.

	private boolean wsCreated; // Work sheet created or not
	private Long worksheetId;
	private Calendar wsDate; // Date of work sheet creation

	private Set<Bidder> bidders;
	private Set<GeneralContractor> generalContractors;
	private Spec spec;

	private boolean worksheetDeleted;

	private String vibrolayin;
	private Double amount;

	private String quoteRevision;
	private boolean mailSentToBidder;
	private boolean pdfGenerated;
	private String pdfFilePath;
	// VIBRO LAY IN

	// Transient
	private String specId;
	private String[] biddersIds;
	private String[] contractorsIds;
	private String userNameOrEmail;
	private String salesPersonName;
	private String architectureName;
	private String engineerName;
	private String specName;
	private String idStyle;
	private String statusStyle;

	private boolean hasBidders;

	private String status;

	private int drawingDocCount;
	private int takeoffDocCount;
	private int vibroDocCount;
	private int quoteDocCount;
	private int quotePdfDocCount;
	private int quoteXlsDocCount;
	private int quoteVibroDocCount;

	private AppSetting appSetting;

	// Report variables
	private int year;
	private Calendar fromDate;
	private Calendar toDate;
	private Calendar fromDueDate;
	private Calendar toDueDate;

	private int pageNumber;
	private int pageSize;

	public Takeoff() {
		super();
		appSetting = new AppSetting();
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public Long getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(Long salesPerson) {
		this.salesPerson = salesPerson;
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	@Column(length = 20)
	public Long getArchitectureId() {
		return architectureId;
	}

	public void setArchitectureId(Long architectureId) {
		this.architectureId = architectureId;
	}

	@Column(length = 20)
	public Long getEngineerId() {
		return engineerId;
	}

	public void setEngineerId(Long engineerId) {
		this.engineerId = engineerId;
	}

	@NotNull
	@Column(nullable = false, length = 100)
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@NotNull
	@Column(nullable = false, length = 250)
	public String getJobAddress() {
		return jobAddress;
	}

	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}

	@Column(length = 25)
	public String getProjectNumber() {
		return projectNumber;
	}

	public void setProjectNumber(String projectNumber) {
		this.projectNumber = projectNumber;
	}

	public Calendar getDrawingDate() {
		return drawingDate;
	}

	public void setDrawingDate(Calendar drawingDate) {
		this.drawingDate = drawingDate;
	}

	public Calendar getRevisedDate() {
		return revisedDate;
	}

	public void setRevisedDate(Calendar revisedDate) {
		this.revisedDate = revisedDate;
	}

	public Calendar getDueDate() {
		return dueDate;
	}

	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}

	@Column(length = 100)
	public String getDrawingReceivedFrom() {
		return drawingReceivedFrom;
	}

	public void setDrawingReceivedFrom(String drawingReceivedFrom) {
		this.drawingReceivedFrom = drawingReceivedFrom;
	}

	@Lob
	@Column(length = 1024)
	public String getTakeOffComment() {
		return takeOffComment;
	}

	public void setTakeOffComment(String takeOffComment) {
		this.takeOffComment = takeOffComment;
	}

	@XmlElement(name = "bidders")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TAKEOFF_BIDDER", joinColumns = @JoinColumn(name = "TAKEOFF"), inverseJoinColumns = @JoinColumn(name = "BIDDER"))
	public Set<Bidder> getBidders() {
		return bidders;
	}

	public void setBidders(Set<Bidder> bidders) {
		this.bidders = bidders;
	}

	@XmlElement(name = "spec")
	@OneToOne(optional = false, cascade = CascadeType.DETACH)
	@JoinColumn(name = "SPECID", unique = false, nullable = false, updatable = true)
	@NotNull
	public Spec getSpec() {
		return spec;
	}

	public void setSpec(Spec spec) {
		this.spec = spec;
	}

	@XmlElement(name = "generalContractors")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TAKEOFF_GC", joinColumns = @JoinColumn(name = "TAKEOFF"), inverseJoinColumns = @JoinColumn(name = "GC"))
	public Set<GeneralContractor> getGeneralContractors() {
		return generalContractors;
	}

	public void setGeneralContractors(Set<GeneralContractor> generalContractors) {
		this.generalContractors = generalContractors;
	}

	@Transient
	public String[] getBiddersIds() {
		return biddersIds;
	}

	public void setBiddersIds(String[] biddersIds) {
		this.biddersIds = biddersIds;
	}

	@Transient
	public String[] getContractorsIds() {
		return contractorsIds;
	}

	public void setContractorsIds(String[] contractorsIds) {
		this.contractorsIds = contractorsIds;
	}

	@Transient
	public String getUserNameOrEmail() {
		return userNameOrEmail;
	}

	public void setUserNameOrEmail(String userNameOrEmail) {
		this.userNameOrEmail = userNameOrEmail;
	}

	@Transient
	public String getSalesPersonName() {
		return salesPersonName;
	}

	public void setSalesPersonName(String salesPersonName) {
		this.salesPersonName = salesPersonName;
	}

	@Transient
	public String getArchitectureName() {
		return architectureName;
	}

	public void setArchitectureName(String architectureName) {
		this.architectureName = architectureName;
	}

	@Transient
	public String getEngineerName() {
		return engineerName;
	}

	public void setEngineerName(String engineerName) {
		this.engineerName = engineerName;
	}

	public String getTakeoffId() {
		return takeoffId;
	}

	public void setTakeoffId(String takeoffId) {
		this.takeoffId = takeoffId;
	}

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	@Transient
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@NotNull
	@Column(nullable = false, length = 10)
	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	@Column(nullable = true, length = 10)
	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}

	@Transient
	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	@Column(length = 100)
	public String getVibrolayin() {

		return vibrolayin;
	}

	public void setVibrolayin(String vibrolayin) {
		this.vibrolayin = vibrolayin;
	}

	@Transient
	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	@Transient
	public String getIdStyle() {
		return idStyle;
	}

	public void setIdStyle(String idStyle) {
		this.idStyle = idStyle;
	}

	@Transient
	public String getStatusStyle() {
		return statusStyle;
	}

	public void setStatusStyle(String statusStyle) {
		this.statusStyle = statusStyle;
	}

	@Transient
	public int getDrawingDocCount() {
		return drawingDocCount;
	}

	public void setDrawingDocCount(int drawingDocCount) {
		this.drawingDocCount = drawingDocCount;
	}

	@Transient
	public int getTakeoffDocCount() {
		return takeoffDocCount;
	}

	public void setTakeoffDocCount(int takeoffDocCount) {
		this.takeoffDocCount = takeoffDocCount;
	}

	@Transient
	public int getVibroDocCount() {
		return vibroDocCount;
	}

	public void setVibroDocCount(int vibroDocCount) {
		this.vibroDocCount = vibroDocCount;
	}

	public Calendar getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(Calendar quoteDate) {
		this.quoteDate = quoteDate;
	}

	public String getQuoteUserCode() {
		return quoteUserCode;
	}

	public void setQuoteUserCode(String quoteUserCode) {
		this.quoteUserCode = quoteUserCode;
	}

	@Lob
	@Column(length = 1024)
	public String getQuoteComment() {
		return quoteComment;
	}

	public void setQuoteComment(String quoteComment) {
		this.quoteComment = quoteComment;
	}

	public boolean isWsCreated() {
		return wsCreated;
	}

	public void setWsCreated(boolean wsCreated) {
		this.wsCreated = wsCreated;
	}

	public Calendar getWsDate() {
		return wsDate;
	}

	public void setWsDate(Calendar wsDate) {
		this.wsDate = wsDate;
	}

	public Long getWorksheetId() {
		return worksheetId;
	}

	public void setWorksheetId(Long worksheetId) {
		this.worksheetId = worksheetId;
	}

	@Transient
	public boolean isHasBidders() {
		return hasBidders;
	}

	public void setHasBidders(boolean hasBidders) {
		this.hasBidders = hasBidders;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getJobOrderId() {
		return jobOrderId;
	}

	public void setJobOrderId(Long jobOrderId) {
		this.jobOrderId = jobOrderId;
	}

	public String getJobOrderNumber() {
		return jobOrderNumber;
	}

	public void setJobOrderNumber(String jobOrderNumber) {
		this.jobOrderNumber = jobOrderNumber;
	}

	@Transient
	public int getQuoteDocCount() {
		return quoteDocCount;
	}

	public void setQuoteDocCount(int quoteDocCount) {
		this.quoteDocCount = quoteDocCount;
	}

	@Transient
	public int getQuotePdfDocCount() {
		return quotePdfDocCount;
	}

	public void setQuotePdfDocCount(int quotePdfDocCount) {
		this.quotePdfDocCount = quotePdfDocCount;
	}

	@Transient
	public int getQuoteXlsDocCount() {
		return quoteXlsDocCount;
	}

	public void setQuoteXlsDocCount(int quoteXlsDocCount) {
		this.quoteXlsDocCount = quoteXlsDocCount;
	}

	@Transient
	public int getQuoteVibroDocCount() {
		return quoteVibroDocCount;
	}

	public void setQuoteVibroDocCount(int quoteVibroDocCount) {
		this.quoteVibroDocCount = quoteVibroDocCount;
	}

	@Transient
	public AppSetting getAppSetting() {
		return appSetting;
	}

	public void setAppSetting(AppSetting appSetting) {
		this.appSetting = appSetting;
	}

	public boolean isWorksheetDeleted() {
		return worksheetDeleted;
	}

	public void setWorksheetDeleted(boolean worksheetDeleted) {
		this.worksheetDeleted = worksheetDeleted;
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
	public Calendar getFromDueDate() {
		return fromDueDate;
	}

	public void setFromDueDate(Calendar fromDueDate) {
		this.fromDueDate = fromDueDate;
	}

	@Transient
	public Calendar getToDueDate() {
		return toDueDate;
	}

	public void setToDueDate(Calendar toDueDate) {
		this.toDueDate = toDueDate;
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

	@Column(nullable = true, length = 25)
	public String getQuoteRevision() {
		return quoteRevision;
	}

	public void setQuoteRevision(String quoteRevision) {
		this.quoteRevision = quoteRevision;
	}

	public boolean isMailSentToBidder() {
		return mailSentToBidder;
	}

	public void setMailSentToBidder(boolean mailSentToBidder) {
		this.mailSentToBidder = mailSentToBidder;
	}

	public boolean isPdfGenerated() {
		return pdfGenerated;
	}

	public void setPdfGenerated(boolean pdfGenerated) {
		this.pdfGenerated = pdfGenerated;
	}

	public String getPdfFilePath() {
		return pdfFilePath;
	}

	public void setPdfFilePath(String pdfFilePath) {
		this.pdfFilePath = pdfFilePath;
	}

}

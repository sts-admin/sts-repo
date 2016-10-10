package com.awacp.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
@NamedQueries({ @NamedQuery(name = "Takeoff.listAll", query = "SELECT t FROM Takeoff t WHERE t.archived = 'false'"),
		@NamedQuery(name = "Takeoff.countAll", query = "SELECT COUNT(t.id) FROM Takeoff t WHERE t.archived = 'false'") 
})
public class Takeoff extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String takeoffId;
	private String quoteId;
	private String jobId;

	private Long salesPerson; // User id //required
	private String userCode;
	private Long architectureId; // User id
	private Long engineerId; // User id

	private String jobName; // required
	private String jobAddress; // required
	private String jobSpecification; // required
	private String projectNumber;

	private Calendar drawingDate;
	private Calendar revisedDate;
	private Calendar dueDate;
	private String drawingReceivedFrom;
	private String takeOffComment;

	private Set<Bidder> bidders;
	private Set<GeneralContractor> generalContractors;

	// Transient
	private String[] biddersIds;
	private String[] contractorsIds;
	private String userNameOrEmail;
	private String salesPersonName;
	private String architectureName;
	private String engineerName;

	public Takeoff() {
		super();
	}

	@NotNull
	@Column(nullable = false)
	public Long getSalesPerson() {
		return salesPerson;
	}

	public void setSalesPerson(Long salesPerson) {
		this.salesPerson = salesPerson;
	}

	@NotNull
	@Column(nullable = false)
	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Long getArchitectureId() {
		return architectureId;
	}

	public void setArchitectureId(Long architectureId) {
		this.architectureId = architectureId;
	}

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
	@Column(nullable = false, length = 200)
	public String getJobAddress() {
		return jobAddress;
	}

	public void setJobAddress(String jobAddress) {
		this.jobAddress = jobAddress;
	}

	@NotNull
	@Column(nullable = false, length = 200)
	public String getJobSpecification() {
		return jobSpecification;
	}

	public void setJobSpecification(String jobSpecification) {
		this.jobSpecification = jobSpecification;
	}

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

	public String getDrawingReceivedFrom() {
		return drawingReceivedFrom;
	}

	public void setDrawingReceivedFrom(String drawingReceivedFrom) {
		this.drawingReceivedFrom = drawingReceivedFrom;
	}

	public String getTakeOffComment() {
		return takeOffComment;
	}

	public void setTakeOffComment(String takeOffComment) {
		this.takeOffComment = takeOffComment;
	}

	@XmlElement(name = "bidders")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TAKEOFF_BIDDER", joinColumns = @JoinColumn(name = "TAKEOFF") , inverseJoinColumns = @JoinColumn(name = "BIDDER") )
	public Set<Bidder> getBidders() {
		return bidders;
	}

	public void setBidders(Set<Bidder> bidders) {
		this.bidders = bidders;
	}

	@XmlElement(name = "generalContractors")
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "TAKEOFF_GC", joinColumns = @JoinColumn(name = "TAKEOFF") , inverseJoinColumns = @JoinColumn(name = "GC") )
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

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

}

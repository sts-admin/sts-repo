package com.awacp.entity;

import java.util.Calendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
public class Takeoff extends BaseEntity {

	private static final long serialVersionUID = 1L;

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

	@Transient
	private String biddersString;
	private String contractorsString;

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
	public String getBiddersString() {
		return biddersString;
	}

	public void setBiddersString(String biddersString) {
		this.biddersString = biddersString;
	}

	@Transient
	public String getContractorsString() {
		return contractorsString;
	}

	public void setContractorsString(String contractorsString) {
		this.contractorsString = contractorsString;
	}

}

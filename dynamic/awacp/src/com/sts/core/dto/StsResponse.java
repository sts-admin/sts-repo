package com.sts.core.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.awacp.entity.Architect;
import com.awacp.entity.Bidder;
import com.awacp.entity.Takeoff;

@XmlRootElement
@XmlSeeAlso({Bidder.class, Takeoff.class, Architect.class})
public class StsResponse<T> {
	private String status;
	private String message;
	private int totalCount;
	private List<T> results;

	public StsResponse() {
		super();
	}

	public StsResponse(int totalCount, List<T> results) {
		super();
		this.totalCount = totalCount;
		this.results = results;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<T> getResults() {
		return results;
	}

	public StsResponse<T> setResults(List<T> results) {
		this.results = results;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

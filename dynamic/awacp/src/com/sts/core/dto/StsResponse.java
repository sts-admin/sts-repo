package com.sts.core.dto;

import java.util.List;

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

	public void setResults(List<T> results) {
		this.results = results;
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

package com.sts.core.dto;

import java.util.List;

public class StsResponse {
	private int totalCount;
	private List<Object> results;

	public StsResponse() {
		super();
	}

	public StsResponse(int totalCount, List<Object> results) {
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

	public List<Object> getResults() {
		return results;
	}

	public void setResults(List<Object> results) {
		this.results = results;
	}

}

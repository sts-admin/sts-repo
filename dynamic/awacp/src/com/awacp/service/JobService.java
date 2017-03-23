package com.awacp.service;

import com.awacp.entity.JobOrder;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.exception.StsResourceNotFoundException;

public interface JobService {

	public StsResponse<JobOrder> listJobOrders(int pageNumber, int pageSize);

	public JobOrder getJobOrder(Long jobOrderId);

	public JobOrder saveJobOrder(JobOrder jobOrder) throws StsDuplicateException;

	public JobOrder updateJobOrder(JobOrder jobOrder) throws StsDuplicateException;

	public String delete(Long id);

	public JobOrder searchQuoteForJobOrder(String quoteId) throws StsResourceNotFoundException;

}
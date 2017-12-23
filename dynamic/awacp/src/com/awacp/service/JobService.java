package com.awacp.service;

import com.awacp.entity.JobOrder;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;
import com.sts.core.exception.StsResourceNotFoundException;

public interface JobService {

	public StsResponse<JobOrder> listJobOrders(int pageNumber, int pageSize);

	public StsResponse<JobOrder> listJobOrdersByInvoiceStatus(int pageNumber, int pageSize, String invoiceStatus);

	public JobOrder getJobOrder(Long jobOrderId);

	public JobOrder saveJobOrder(JobOrder jobOrder) throws StsDuplicateException;

	public JobOrder updateJobOrder(JobOrder jobOrder) throws StsDuplicateException;

	public String delete(Long id);

	public JobOrder searchQuoteForJobOrder(String quoteId) throws StsResourceNotFoundException;

	public JobOrder getJobOrderByOrderId(String orderId);

	public String jobFinalUpdate(Long jobId, Long userId);

	public StsResponse<JobOrder> generateJobOrderReport(JobOrder jobOrder);

	public String cancelJobOrder(Long jobId);

	public String uncancellJobOrder(Long jobId);

}

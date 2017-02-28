package com.awacp.service;

import java.util.List;

import com.awacp.entity.JobOrder;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.StsDuplicateException;

public interface JobService {

	public StsResponse<JobOrder> listJobOrders(int pageNumber, int pageSize);

	public JobOrder getJobOrder(Long jobOrderId);

	public JobOrder saveJobOrder(JobOrder jobOrder) throws StsDuplicateException;

	public JobOrder updateJobOrder(JobOrder jobOrder) throws StsDuplicateException;

	public List<JobOrder> filter(String keyword); // match name

	public String delete(Long id);

}

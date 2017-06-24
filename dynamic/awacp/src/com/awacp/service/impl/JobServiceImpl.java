package com.awacp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.entity.Contractor;
import com.awacp.entity.Engineer;
import com.awacp.entity.Invoice;
import com.awacp.entity.JobOrder;
import com.awacp.entity.OrderBook;
import com.awacp.entity.Takeoff;
import com.awacp.service.InvoiceService;
import com.awacp.service.JobService;
import com.awacp.service.TakeoffService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.exception.StsResourceNotFoundException;
import com.sts.core.service.FileService;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class JobServiceImpl extends CommonServiceImpl<JobOrder> implements JobService {
	private EntityManager entityManager;

	@Autowired
	private UserService userService;

	@Autowired
	private TakeoffService takeoffService;

	@Autowired
	private FileService fileService;

	@Autowired
	private InvoiceService invoiceService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<JobOrder> listJobOrdersByInvoiceStatus(int pageNumber, int pageSize, String invoiceStatus) {
		if (invoiceStatus == null || invoiceStatus.trim().isEmpty() || invoiceStatus.equalsIgnoreCase("all")) {
			return listJobOrders(pageNumber, pageSize);
		}
		StsResponse<JobOrder> response = new StsResponse<JobOrder>();
		if (pageNumber <= 1) { // Set total record count on first page request
								// and re-use that

			Object object = getEntityManager().createNamedQuery("JobOrder.getCountByInvoiceStatus")
					.setParameter("invoiceStatus", invoiceStatus.toLowerCase()).getSingleResult();
			response.setTotalCount(object == null ? 0 : ((Long) object).intValue());
		}
		Query query = getEntityManager().createNamedQuery("JobOrder.getByInvoiceStatus").setParameter("invoiceStatus",
				invoiceStatus.toLowerCase());
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<JobOrder> results = query.getResultList();
		return results == null || results.isEmpty() ? response : response.setResults(initWithDetail(results));
	}

	@Override
	public StsResponse<JobOrder> listJobOrders(int pageNumber, int pageSize) {
		StsResponse<JobOrder> response = listAll(pageNumber, pageSize, JobOrder.class.getSimpleName(),
				getEntityManager());
		return response.getResults() == null || response.getResults().isEmpty() ? response
				: response.setResults(initWithDetail(response.getResults()));
	}

	private List<JobOrder> initWithDetail(List<JobOrder> jobOrders) {
		if (jobOrders == null || jobOrders.isEmpty())
			return null;
		for (JobOrder jo : jobOrders) {
			jo.setJoOneDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_ONE, jo.getId()));
			jo.setJoTwoDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_TWO, jo.getId()));
			jo.setJoThreeDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_THREE, jo.getId()));
			jo.setJoFourDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_FOUR, jo.getId()));
			jo.setJoFiveDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_FIVE, jo.getId()));
			jo.setJoSixDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_SIX, jo.getId()));

			jo.setJoDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_DOC, jo.getId()));
			jo.setJoXlsCount(fileService.getFileCount(StsCoreConstant.DOC_JO_XLS, jo.getId()));
			jo.setJoTaxDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_TAX, jo.getId()));
			jo.setJoPoDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_PO, jo.getId()));
			jo.setJoUiDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_IU, jo.getId()));
		}
		return jobOrders;
	}

	@SuppressWarnings("unchecked")
	private void initWithDetail(JobOrder jobOrder) {
		if (jobOrder != null) {
			List<OrderBook> partialOrderBooks = getEntityManager()
					.createNamedQuery("OrderBook.getOrderBookNumbersByOrderId").setParameter("jobId", jobOrder.getId())
					.getResultList();
			if (partialOrderBooks != null && !partialOrderBooks.isEmpty()) {
				String OBN[] = new String[partialOrderBooks.size()];
				for (int i = 0; i < partialOrderBooks.size(); i++) {
					OBN[i] = partialOrderBooks.get(i).getOrderBookNumber();
				}
				jobOrder.setJobOrderBookNumbers(OBN);
			}
			if (jobOrder.getContractorName() == null || jobOrder.getContractorName().trim().isEmpty()) {
				if (jobOrder.getContractorId() != null) {
					Contractor contractor = getEntityManager().find(Contractor.class, jobOrder.getContractorId());
					if (contractor != null) {
						jobOrder.setContractorName(contractor.getName());
					}
				}
			}
			if (jobOrder.getEngineerName() == null || jobOrder.getEngineerName().trim().isEmpty()) {
				if (jobOrder.getEngineerId() != null) {
					Engineer engineer = getEntityManager().find(Engineer.class, jobOrder.getEngineerId());
					if (engineer != null) {
						jobOrder.setEngineerName(engineer.getName());
					}
				}
			}
			if (jobOrder.getArchitectureName() == null || jobOrder.getArchitectureName().trim().isEmpty()) {
				if (jobOrder.getArchitectureId() != null) {
					Architect architect = getEntityManager().find(Architect.class, jobOrder.getArchitectureId());
					if (architect != null) {
						jobOrder.setArchitectureName(architect.getName());
					}
				}
			}
		}
	}

	@Override
	public JobOrder getJobOrder(Long jobOrderId) {
		JobOrder jobOrder = getEntityManager().find(JobOrder.class, jobOrderId);
		initWithDetail(jobOrder);
		return jobOrder;
	}

	@Override
	@Transactional
	public JobOrder saveJobOrder(JobOrder jobOrder) {
		User user = userService.getUserByUserNameOrEmail(jobOrder.getUserNameOrEmail());
		if (StringUtils.isNotEmpty(jobOrder.getArchitectureName())) { // NEW
			Architect architect = new Architect();
			architect.setCreatedByUserCode(jobOrder.getCreatedByUserCode());
			architect.setName(jobOrder.getArchitectureName());
			architect.setSalesPerson(user.getId());
			getEntityManager().persist(architect);
			getEntityManager().flush();
			jobOrder.setArchitectureId(architect.getId());
		}
		if (StringUtils.isNotEmpty(jobOrder.getEngineerName())) { // NEW
			Engineer engineer = new Engineer();
			engineer.setCreatedByUserCode(jobOrder.getCreatedByUserCode());
			engineer.setName(jobOrder.getEngineerName());
			engineer.setSalesPerson(user.getId());
			getEntityManager().persist(engineer);
			getEntityManager().flush();
			jobOrder.setEngineerId(engineer.getId());
		}
		if (StringUtils.isNotEmpty(jobOrder.getContractorName())) { // NEW
			Contractor contractor = new Contractor();
			contractor.setCreatedByUserCode(jobOrder.getCreatedByUserCode());
			contractor.setName(jobOrder.getContractorName());
			contractor.setSalesPerson(user.getId());
			getEntityManager().persist(contractor);
			getEntityManager().flush();
			jobOrder.setContractorId(contractor.getId());
		}
		if (jobOrder.getId() != null && jobOrder.getId() > 0) { // update
			getEntityManager().merge(jobOrder);
			getEntityManager().flush();
		} else {

			DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2
														// digits
			String jobOrderId = new StringBuffer("JO").append(df.format(Calendar.getInstance().getTime())).append("-")
					.toString();
			jobOrder.setOrderNumber(jobOrderId);
			getEntityManager().persist(jobOrder);
			getEntityManager().flush();
			jobOrder.setOrderNumber(jobOrder.getOrderNumber().concat("" + jobOrder.getId()));
			getEntityManager().merge(jobOrder);
			getEntityManager().flush();
			
			Invoice invoice = new Invoice();
			invoice.setAwOrderNumber(jobOrder.getOrderNumber());
			invoice.setSalesPersonCode(jobOrder.getSalesPersonName());
			invoice.setCreatedById(jobOrder.getCreatedById());
			invoice.setCreatedByUserCode(jobOrder.getCreatedByUserCode());
			invoice.setProfitPercent(0.0D);
			invoice.setJobOrderId(jobOrder.getId());

			invoiceService.saveInvoice(invoice);

			/* Update job order info in referenced takeoff */
			if (jobOrder.getTakeoffId() != null) {
				Takeoff takeoff = takeoffService.getTakeoff(jobOrder.getTakeoffId());
				takeoff.setJobOrderId(jobOrder.getId());
				takeoff.setJobOrderNumber(jobOrder.getOrderNumber());

				takeoffService.updateTakeoff(takeoff);
			}

		}

		return jobOrder;
	}

	@Override
	@Transactional
	public JobOrder updateJobOrder(JobOrder JobOrder) {
		JobOrder = getEntityManager().merge(JobOrder);
		getEntityManager().flush();
		return JobOrder;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		JobOrder entity = getJobOrder(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@Override
	public JobOrder searchQuoteForJobOrder(String quoteId) throws StsResourceNotFoundException {
		Takeoff takeoff = takeoffService.getTakeoffByQuoteId(quoteId);
		JobOrder jobOrder = null;
		if (takeoff == null) {
			throw new StsResourceNotFoundException("resource_not_found");
		}
		jobOrder = new JobOrder();
		jobOrder.setTakeoffId(takeoff.getId());
		jobOrder.setQuoteId(takeoff.getQuoteId());
		jobOrder.setArchitectureId(takeoff.getArchitectureId());
		jobOrder.setArchitectureName(takeoff.getArchitectureName());
		jobOrder.setEngineerId(takeoff.getEngineerId());
		jobOrder.setEngineerName(takeoff.getEngineerName());
		jobOrder.setJobName(takeoff.getJobName());
		jobOrder.setJobAddress(takeoff.getJobAddress());
		jobOrder.setQuotedAmount(takeoff.getAmount());
		jobOrder.setBillingAmount(takeoff.getAmount());
		jobOrder.setId(takeoff.getJobOrderId());
		jobOrder.setOrderNumber(takeoff.getJobOrderNumber());

		return jobOrder;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JobOrder getJobOrderByOrderId(String orderNumber) {
		List<JobOrder> orders = getEntityManager().createNamedQuery("JobOrder.getByOrderId")
				.setParameter("orderNumber", orderNumber.toLowerCase()).getResultList();
		return orders == null || orders.isEmpty() ? null : orders.get(0);
	}

}

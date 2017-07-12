package com.awacp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.entity.Contractor;
import com.awacp.entity.Engineer;
import com.awacp.entity.Invoice;
import com.awacp.entity.JobOrder;
import com.awacp.entity.OrderBook;
import com.awacp.entity.ProfitSheetItem;
import com.awacp.entity.Takeoff;
import com.awacp.service.ArchitectService;
import com.awacp.service.EngineerService;
import com.awacp.service.InvoiceService;
import com.awacp.service.JobService;
import com.awacp.service.OrderBookService;
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

	@Autowired
	private EngineerService engineerService;

	@Autowired
	private ArchitectService architectService;

	@Autowired
	private OrderBookService orderBookService;

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
			initWithDetail(jo);
		}
		return jobOrders;
	}

	@SuppressWarnings("unchecked")
	private void initWithDetail(JobOrder jobOrder) {
		if (jobOrder != null) {
			jobOrder.setJoOneDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_ONE, jobOrder.getId()));
			jobOrder.setJoTwoDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_TWO, jobOrder.getId()));
			jobOrder.setJoThreeDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_THREE, jobOrder.getId()));
			jobOrder.setJoFourDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_FOUR, jobOrder.getId()));
			jobOrder.setJoFiveDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_FIVE, jobOrder.getId()));
			jobOrder.setJoSixDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_SIX, jobOrder.getId()));

			jobOrder.setJoDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_DOC, jobOrder.getId()));
			jobOrder.setJoXlsCount(fileService.getFileCount(StsCoreConstant.DOC_JO_XLS, jobOrder.getId()));
			jobOrder.setJoTaxDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_TAX, jobOrder.getId()));
			jobOrder.setJoPoDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_PO, jobOrder.getId()));
			jobOrder.setJoUiDocCount(fileService.getFileCount(StsCoreConstant.DOC_JO_IU, jobOrder.getId()));
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
			if (jobOrder.getSalesPersonName() != null && !jobOrder.getSalesPersonName().isEmpty()) {
				invoice.setSalesPersonCode(jobOrder.getSalesPersonName());
			}

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

	@Override
	@Transactional
	public String jobFinalUpdate(Long jobId) {
		JobOrder jo = getJobOrder(jobId);
		if (jo != null) {
			jo.setFinalUpdate(true);
			getEntityManager().merge(jo);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@Override
	public StsResponse<JobOrder> generateJobOrderReport(JobOrder jobOrder) {
		StringBuffer sb = new StringBuffer("SELECT t FROM ").append(JobOrder.class.getSimpleName())
				.append(" t WHERE t.archived =:archived");

		StringBuffer countQuery = new StringBuffer("SELECT COUNT(t.id) FROM ").append(JobOrder.class.getSimpleName())
				.append(" t WHERE t.archived =:archived");

		if (jobOrder.getFromDate() != null && jobOrder.getToDate() != null) {
			sb.append(" AND FUNC('DATE', t.dateCreated) >= :fromDate AND FUNC('DATE', t.dateCreated) <= :toDate");
			countQuery
					.append(" AND FUNC('DATE', t.dateCreated) >= :fromDate AND FUNC('DATE', t.dateCreated) <= :toDate");
		}

		if (jobOrder.getYear() > 0) {
			sb.append(" AND FUNC('YEAR', t.dateCreated) =:year");
			countQuery.append(" AND FUNC('YEAR', t.dateCreated) =:year");
		}
		if (jobOrder.getCreatedByUserCode() != null && !jobOrder.getCreatedByUserCode().isEmpty()) {
			sb.append(" AND t.createdByUserCode =:createdByUserCode");
			countQuery.append(" AND t.createdByUserCode =:createdByUserCode");
		}
		if (jobOrder.getSalesPersonId() != null && jobOrder.getSalesPersonId() > 0) {
			sb.append(" AND t.salesPersonId =:salesPersonId");
			countQuery.append(" AND t.salesPersonId =:salesPersonId");
		}
		/**
		 * Search parameters
		 */
		if (jobOrder.getRecepientSpId() != null && jobOrder.getRecepientSpId() > 0) {
			sb.append(" AND t.recepientSpId =:recepientSpId");
			countQuery.append(" AND t.salesPersonId =:recepientSpId");
		}
		if (jobOrder.getQuotedAmount() != null && jobOrder.getQuotedAmount().intValue() > 0) {
			sb.append(" AND t.quotedAmount =:quotedAmount");
			countQuery.append(" AND t.quotedAmount =:quotedAmount");
		}
		if (jobOrder.getBillingAmount() != null && jobOrder.getBillingAmount().intValue() > 0) {
			sb.append(" AND t.billingAmount =:billingAmount");
			countQuery.append(" AND t.billingAmount =:billingAmount");
		}
		if (jobOrder.getOrderNumber() != null && !jobOrder.getOrderNumber().isEmpty()) {
			sb.append(" AND t.orderNumber =:orderNumber");
			countQuery.append(" AND t.orderNumber =:orderNumber");
		}

		if (jobOrder.getPoName() != null && !jobOrder.getPoName().isEmpty()) {
			sb.append(" AND t.poName =:poName");
			countQuery.append(" AND t.poName =:poName");
		}

		if (jobOrder.getJobName() != null && jobOrder.getJobName().trim().length() > 0) {
			sb.append(" AND LOWER(t.jobName) LIKE :jobName");
			countQuery.append(" AND LOWER(t.jobName) LIKE :jobName");

		}
		if (jobOrder.getJobAddress() != null && jobOrder.getJobAddress().trim().length() > 0) {
			sb.append(" AND LOWER(t.jobAddress) LIKE :jobAddress");
			countQuery.append(" AND LOWER(t.jobAddress) LIKE :jobAddress");
		}

		if (jobOrder.getVavControls() != null && !jobOrder.getVavControls().isEmpty()) {
			sb.append(" AND LOWER(t.vavControls) LIKE :vavControls");
			countQuery.append(" AND LOWER(t.vavControls) LIKE :vavControls");
		}
		/**
		 * Search parameters
		 */

		if (jobOrder.getEngineerId() != null) {
			sb.append(" AND t.engineerId =:engineerId");
			countQuery.append(" AND t.engineerId =:engineerId");
		}
		if (jobOrder.getArchitectureId() != null) {
			sb.append(" AND t.architectureId =:architectureId");
			countQuery.append(" AND t.architectureId =:architectureId");
		}
		if (jobOrder.getInvoiceMode() != null && !jobOrder.getInvoiceMode().isEmpty()) {
			sb.append(" AND LOWER(t.invoiceMode) =:invoiceMode");
			countQuery.append(" AND LOWER(t.invoiceMode) =:invoiceMode");
		}
		String status = jobOrder.getStatus();
		if (status == null) {
			status = "";
		}
		status = status.trim();
		if (status.equalsIgnoreCase("released")) {
			sb.append(" AND t.salesPersonId > 0");
			countQuery.append(" AND t.salesPersonId > 0");
		} else if (status.equalsIgnoreCase("unreleased")) {
			sb.append(" AND t.salesPersonId IS NULL");
			countQuery.append(" AND t.salesPersonId IS NULL");
		}
		sb.append(" AND t.finalUpdate =:finalUpdate");
		countQuery.append(" AND t.finalUpdate =:finalUpdate");

		Query query = getEntityManager().createQuery(sb.toString());
		Query query2 = getEntityManager().createQuery(countQuery.toString());

		query.setParameter("finalUpdate", jobOrder.isFinalUpdate());
		query2.setParameter("finalUpdate", jobOrder.isFinalUpdate());

		query.setParameter("archived", jobOrder.isArchived());
		query2.setParameter("archived", jobOrder.isArchived());

		if (jobOrder.getFromDate() != null && jobOrder.getToDate() != null) {
			query.setParameter("fromDate", jobOrder.getFromDate().getTime(), TemporalType.DATE);
			query.setParameter("toDate", jobOrder.getToDate().getTime(), TemporalType.DATE);

			query2.setParameter("fromDate", jobOrder.getFromDate().getTime(), TemporalType.DATE);
			query2.setParameter("toDate", jobOrder.getToDate().getTime(), TemporalType.DATE);
		}

		if (jobOrder.getYear() > 0) {
			query.setParameter("year", jobOrder.getYear());
			query2.setParameter("year", jobOrder.getYear());
		}
		if (jobOrder.getCreatedByUserCode() != null && !jobOrder.getCreatedByUserCode().isEmpty()) {
			query.setParameter("createdByUserCode", jobOrder.getCreatedByUserCode());
			query2.setParameter("createdByUserCode", jobOrder.getCreatedByUserCode());
		}
		if (jobOrder.getSalesPersonId() != null && jobOrder.getSalesPersonId() > 0) {
			query.setParameter("salesPersonId", jobOrder.getSalesPersonId());
			query2.setParameter("salesPersonId", jobOrder.getSalesPersonId());
		}
		if (jobOrder.getEngineerId() != null) {
			query.setParameter("engineerId", jobOrder.getEngineerId());
			query2.setParameter("engineerId", jobOrder.getEngineerId());
		}
		if (jobOrder.getArchitectureId() != null) {
			query.setParameter("architectureId", jobOrder.getArchitectureId());
			query2.setParameter("architectureId", jobOrder.getArchitectureId());
		}
		if (jobOrder.getInvoiceMode() != null && !jobOrder.getInvoiceMode().isEmpty()) {
			query.setParameter("invoiceMode", jobOrder.getInvoiceMode());
			query2.setParameter("invoiceMode", jobOrder.getInvoiceMode());
		}

		/**
		 * Search parameters
		 */
		if (jobOrder.getRecepientSpId() != null && jobOrder.getRecepientSpId() > 0) {
			query.setParameter("recepientSpId", jobOrder.getRecepientSpId());
			query2.setParameter("recepientSpId", jobOrder.getRecepientSpId());
		}
		if (jobOrder.getQuotedAmount() != null && jobOrder.getQuotedAmount().intValue() > 0) {
			query.setParameter("quotedAmount", jobOrder.getQuotedAmount());
			query2.setParameter("quotedAmount", jobOrder.getQuotedAmount());
		}
		if (jobOrder.getBillingAmount() != null && jobOrder.getBillingAmount().intValue() > 0) {
			query.setParameter("billingAmount", jobOrder.getBillingAmount());
			query2.setParameter("billingAmount", jobOrder.getBillingAmount());
		}
		if (jobOrder.getOrderNumber() != null && !jobOrder.getOrderNumber().isEmpty()) {
			query.setParameter("orderNumber", jobOrder.getOrderNumber());
			query2.setParameter("orderNumber", jobOrder.getOrderNumber());
		}

		if (jobOrder.getPoName() != null && !jobOrder.getPoName().isEmpty()) {
			query.setParameter("poName", jobOrder.getPoName());
			query2.setParameter("poName", jobOrder.getPoName());
		}

		if (jobOrder.getJobName() != null && !jobOrder.getJobName().isEmpty()) {
			query.setParameter("jobName", "%" + jobOrder.getJobName().toLowerCase() + "%");
			query2.setParameter("jobName", "%" + jobOrder.getJobName().toLowerCase() + "%");
		}
		if (jobOrder.getJobAddress() != null && !jobOrder.getJobAddress().isEmpty()) {
			query.setParameter("jobAddress", "%" + jobOrder.getJobAddress().toLowerCase() + "%");
			query2.setParameter("jobAddress", "%" + jobOrder.getJobAddress().toLowerCase() + "%");
		}

		if (jobOrder.getVavControls() != null && !jobOrder.getVavControls().isEmpty()) {
			query.setParameter("vavControls", "%" + jobOrder.getVavControls().toLowerCase() + "%");
			query2.setParameter("vavControls", "%" + jobOrder.getVavControls().toLowerCase() + "%");
		}
		/**
		 * Search parameters
		 */

		if (jobOrder.getPageNumber() > 0 && jobOrder.getPageSize() > 0) {
			query.setFirstResult(((jobOrder.getPageNumber() - 1) * jobOrder.getPageSize()))
					.setMaxResults(jobOrder.getPageSize());
		}
		StsResponse<JobOrder> response = new StsResponse<JobOrder>();
		if (jobOrder.getPageNumber() <= 1) {
			Object object = query2.getSingleResult();
			int count = 0;
			if (object != null) {
				count = (((Long) object).intValue());
			}
			response.setTotalCount(count);
		}

		@SuppressWarnings("unchecked")
		List<JobOrder> results = query.getResultList();
		if (results != null && !results.isEmpty()) {
			Double totalCost = 0.0D;
			for (JobOrder result : results) {
				totalCost = 0.0D;
				enrichJobOrderForReport(result);
				List<OrderBook> oBooks = orderBookService.listByJobOrder(result.getId());
				if (oBooks != null && !oBooks.isEmpty()) {
					for (OrderBook ob : oBooks) {
						Invoice invoice = invoiceService.getInvoiceByJobOrder(ob.getJobId());
						if (invoice != null) {
							Set<ProfitSheetItem> psItems = invoice.getProfitSheetItems();
							if (psItems != null && !psItems.isEmpty()) {
								totalCost += calculateTotalCost(psItems);
							}
						}
					}
				}
				result.setTotalCost(totalCost);
			}
			response.setResults(results);
		}
		return response;
	}

	private JobOrder enrichJobOrderForReport(JobOrder jobOrder) {
		if (jobOrder.getSalesPersonId() != null && jobOrder.getSalesPersonId() > 0) {
			User salesUser = userService.findUser(jobOrder.getSalesPersonId());
			if (salesUser != null) {
				jobOrder.setSalesPersonName(salesUser.getFirstName() + "	" + salesUser.getLastName());
			}
		}

		User user = userService.findUser(jobOrder.getCreatedById());
		if (user != null) {
			jobOrder.setCreatedByUserCode(user.getUserCode());
		}
		if (jobOrder.getEngineerId() != null && jobOrder.getEngineerId() > 0) {
			Engineer eng = engineerService.getEngineer(jobOrder.getEngineerId());
			if (eng != null) {
				jobOrder.setEngineerName(eng.getName());
			}
		}
		if (jobOrder.getArchitectureId() != null && jobOrder.getArchitectureId() > 0) {
			Architect arc = architectService.getArchitect(jobOrder.getArchitectureId());
			if (arc != null) {
				jobOrder.setArchitectureName(arc.getName());
			}
		}

		return jobOrder;
	}

	private Double calculateTotalCost(Set<ProfitSheetItem> psItems) {
		Double totalCost = 0.0D;

		if (psItems != null && !psItems.isEmpty()) {
			for (ProfitSheetItem psi : psItems) {
				totalCost = totalCost + (psi.getInvAmount() == null ? 0.0D : psi.getInvAmount())
						+ (psi.getFreight() == null ? 0.0D : psi.getFreight());
			}
		}
		return totalCost;
	}
}

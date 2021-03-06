package com.awacp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.entity.Bidder;
import com.awacp.entity.Engineer;
import com.awacp.entity.GeneralContractor;
import com.awacp.entity.QuoteFollowup;
import com.awacp.entity.SiteInfo;
import com.awacp.entity.Spec;
import com.awacp.entity.Takeoff;
import com.awacp.service.ArchitectService;
import com.awacp.service.BidderService;
import com.awacp.service.EngineerService;
import com.awacp.service.GeneralContractorService;
import com.awacp.service.MailService;
import com.awacp.service.SpecService;
import com.awacp.service.TakeoffService;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.AutoComplete;
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.service.FileService;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class TakeoffServiceImpl extends CommonServiceImpl<Takeoff> implements TakeoffService {

	private EntityManager entityManager;

	@Autowired
	private BidderService bidderService;

	@Autowired
	private GeneralContractorService generalContractorService;

	@Autowired
	private EngineerService engineerService;

	@Autowired
	private ArchitectService architectService;

	@Autowired
	private UserService userService;

	@Autowired
	private MailService awacpMailService;

	@Autowired
	private SpecService specService;

	@Autowired
	private FileService fileService;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public StsResponse<Takeoff> listTakeoffs(int pageNumber, int pageSize, String redOrGreenOrAll) {
		StsResponse<Takeoff> response = null;
		StringBuffer searchQuery = new StringBuffer("SELECT entity FROM ").append(Takeoff.class.getSimpleName())
				.append(" entity WHERE entity.archived = 'false' ORDER BY entity.dateCreated DESC");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(entity.id) FROM ")
				.append(Takeoff.class.getSimpleName()).append(" entity WHERE entity.archived = 'false'");
		if ("not_quoted".equalsIgnoreCase(redOrGreenOrAll)) {
			searchQuery = new StringBuffer("SELECT entity FROM ").append(Takeoff.class.getSimpleName()).append(
					" entity WHERE entity.archived = 'false' AND FUNC('ISNULL', entity.quoteId) ORDER BY entity.dateCreated DESC");
			countQuery = new StringBuffer("SELECT COUNT(entity.id) FROM ").append(Takeoff.class.getSimpleName())
					.append(" entity WHERE entity.archived = 'false' AND FUNC('ISNULL', entity.quoteId)");

		} else if ("quoted".equalsIgnoreCase(redOrGreenOrAll)) {
			searchQuery = new StringBuffer("SELECT entity FROM ").append(Takeoff.class.getSimpleName()).append(
					" entity WHERE entity.archived = 'false' AND NOT FUNC('ISNULL', entity.quoteId) ORDER BY entity.dateCreated DESC");
			countQuery = new StringBuffer("SELECT COUNT(entity.id) FROM ").append(Takeoff.class.getSimpleName())
					.append(" entity WHERE entity.archived = 'false' AND NOT FUNC('ISNULL', entity.quoteId)");

		}
		response = listAll(pageNumber, pageSize, searchQuery.toString(), countQuery.toString(),
				Takeoff.class.getSimpleName(), "id", getEntityManager());
		return response.getResults() == null || response.getResults().isEmpty() ? response
				: response.setResults(initWithDetail(response.getResults()));
	}

	private List<Takeoff> initWithDetail(List<Takeoff> takeoffs) {
		if (takeoffs == null || takeoffs.isEmpty())
			return null;

		for (Takeoff takeoff : takeoffs) {
			if (takeoff.getView() == null || takeoff.getView().length() <= 0) {
				takeoff.setView("takeoff");
			}

			enrichTakeoff(takeoff);
		}
		return takeoffs;
	}

	@Override
	public Takeoff getTakeoff(Long takeoffId) {
		Takeoff takeoff = getEntityManager().find(Takeoff.class, takeoffId);
		if (takeoff != null && takeoff.getSpec() != null) {
			takeoff.setSpecId(String.valueOf(takeoff.getSpec().getId()));
		}
		enrichTakeoff(takeoff);
		return takeoff;
	}

	@Override
	@Transactional
	public Takeoff saveTakeoff(Takeoff takeoff) throws Exception {
		String[] biddersIds = takeoff.getBiddersIds();
		User user = userService.getUserByUserNameOrEmail(takeoff.getUserNameOrEmail());
		if (StringUtils.isNotEmpty(takeoff.getArchitectureName())) { // NEW
			if (!isExistsByName(takeoff.getArchitectureName(), "name", "Architect", getEntityManager())) {
				Architect architect = new Architect();
				architect.setCreatedByUserCode(takeoff.getCreatedByUserCode());
				architect.setName(takeoff.getArchitectureName());
				architect.setSalesPerson(user.getId());
				getEntityManager().persist(architect);
				getEntityManager().flush();
				takeoff.setArchitectureId(architect.getId());
			}
		}
		if (StringUtils.isNotEmpty(takeoff.getEngineerName())) { // NEW
			if (!isExistsByName(takeoff.getEngineerName(), "name", "Engineer", getEntityManager())) {
				Engineer engineer = new Engineer();
				engineer.setCreatedByUserCode(takeoff.getCreatedByUserCode());
				engineer.setName(takeoff.getEngineerName());
				engineer.setSalesPerson(user.getId());
				getEntityManager().persist(engineer);
				getEntityManager().flush();
				takeoff.setEngineerId(engineer.getId());
			}

		}
		if (StringUtils.isNotEmpty(takeoff.getSpecName())) { // NEW
			Spec spec = new Spec();
			spec.setCreatedByUserCode(takeoff.getCreatedByUserCode());
			spec.setDetail(takeoff.getSpecName());
			spec.setCreatedById(user.getId());
			spec.setCreatedByUserCode(user.getUserCode());
			getEntityManager().persist(spec);
			getEntityManager().flush();
			takeoff.setSpec(spec);
		}
		if (biddersIds != null && biddersIds.length > 0) {
			Set<Bidder> bidders = new HashSet<Bidder>();
			for (String id : biddersIds) {
				Bidder bidder = bidderService.getBidder(Long.valueOf(id));
				if (bidder != null) {
					bidders.add(bidder);
				}
			}
			takeoff.setBidders(bidders);
		}
		String specId = takeoff.getSpecId();
		if (specId != null && !specId.isEmpty()) {
			takeoff.setSpec(specService.getSpec(Long.valueOf(specId)));
		}
		String[] contractorIds = takeoff.getContractorsIds();
		if (contractorIds != null && contractorIds.length > 0) {
			Set<GeneralContractor> contractors = new HashSet<GeneralContractor>();
			for (String id : contractorIds) {
				GeneralContractor contractor = generalContractorService.getContractor(Long.valueOf(id));
				if (contractor != null) {
					contractors.add(contractor);
				}
			}
			takeoff.setGeneralContractors(contractors);
		}
		if (takeoff.getUserNameOrEmail() != null && !takeoff.getUserNameOrEmail().isEmpty()) {
			String code = userService.getUserCode(takeoff.getUserNameOrEmail());
			takeoff.setUserCode(code);
		}
		takeoff.setUserCode(userService.getUserCode(takeoff.getUserNameOrEmail()));
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2
													// digits
		if (takeoff.getId() != null && takeoff.getId() > 0) { // update
			if (takeoff.getQuoteRevision() != null && !takeoff.getQuoteRevision().isEmpty()) {
				String quoteId = new StringBuffer("Q").append(df.format(Calendar.getInstance().getTime())).append("-")
						.append(takeoff.getId()).append("-").append(takeoff.getQuoteRevision()).toString();

				takeoff.setQuoteId(quoteId);

			}

			getEntityManager().merge(takeoff);
		} else {
			getEntityManager().persist(takeoff);
			getEntityManager().flush();

			String takeoffId = new StringBuffer("T").append(df.format(Calendar.getInstance().getTime())).append("-")
					.append(takeoff.getId()).toString();
			takeoff.setTakeoffId(takeoffId);
			getEntityManager().merge(takeoff);

			// Takeoff mail
			String status = awacpMailService.sendNewTakeoffMail(takeoff.getId());
			takeoff.setStatus(status);
		}

		return takeoff;
	}

	@Override
	@Transactional
	public Takeoff updateTakeoff(Takeoff takeoff) {
		System.err.println("TAKEOFF DATE = " + takeoff.getDateCreated() + " DUE DATE: " + takeoff.getDueDate());
		takeoff = getEntityManager().merge(takeoff);
		getEntityManager().flush();
		return takeoff;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getNewTakeoffEmails() {
		String[] emails = null;
		String queryString = "SELECT u.email FROM USER AS u INNER JOIN USER_PERMISSION AS p ON u.ID = p.USERID AND u.ARCHIVED = 'false' AND p.PERMISSIONID = 'takeoff_new_email'";
		List<String> results = getEntityManager().createNativeQuery(queryString).getResultList();
		if (results != null && !results.isEmpty()) {
			emails = new String[results.size()];
			int index = 0;
			for (String email : results) {
				emails[index++] = email;
			}
		}
		return emails;
	}

	@Override
	public List<Takeoff> filter(String filters, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Takeoff entity = getTakeoff(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<Takeoff> listNewTakeoffsForQuote(int pageNumber, int pageSize) {
		int totalRecordsCount = getTotalRecords(
				"SELECT COUNT(entity.id) FROM Takeoff entity WHERE entity.archived = 'false' AND FUNC('ISNULL', entity.quoteId)",
				getEntityManager());
		Query query = getEntityManager().createNamedQuery("Takeoff.listNewTakeoffsForQuote");
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<Takeoff> takeoffs = query.getResultList();
		StsResponse<Takeoff> responses = null;
		if (totalRecordsCount > 0) {
			responses = new StsResponse<>();
			responses.setTotalCount(totalRecordsCount);
			responses.setResults(initWithDetailForNewQuote(takeoffs, false));
		}
		return responses;
	}

	private List<Takeoff> initWithDetailForNewQuote(List<Takeoff> takeoffs, boolean viewQuote) {
		if (takeoffs == null || takeoffs.isEmpty()) {
			return null;
		}
		for (Takeoff takeoff : takeoffs) {
			if (viewQuote) {
				takeoff.setView("quote");
			}
			enrichTakeoff(takeoff);
		}
		return takeoffs;
	}

	private Takeoff enrichTakeoff(Takeoff takeoff) {
		if (takeoff.getSiteInfo() == null) {
			takeoff.setSiteInfo(new SiteInfo());
		}
		User user = userService.findUser(takeoff.getSalesPerson());
		if (user != null) {
			takeoff.setSalesPersonName(user.getFirstName() + "	" + user.getLastName());
		}
		if (takeoff.getEngineerId() != null && takeoff.getEngineerId() > 0) {
			Engineer eng = engineerService.getEngineer(takeoff.getEngineerId());
			if (eng != null) {
				takeoff.setEngineerName(eng.getName());
			}
		}
		if (takeoff.getArchitectureId() != null && takeoff.getArchitectureId() > 0) {
			Architect arc = architectService.getArchitect(takeoff.getArchitectureId());
			if (arc != null) {
				takeoff.setArchitectureName(arc.getName());
			}
		}
		if (takeoff.getView() != null && "quote".equalsIgnoreCase(takeoff.getView())) {
			takeoff.setQuoteDocCount(fileService.getFileCount(StsCoreConstant.DOC_QUOTE_DOC, takeoff.getId()));
			takeoff.setQuotePdfDocCount(fileService.getFileCount(StsCoreConstant.DOC_QUOTE_PDF, takeoff.getId()));
			takeoff.setQuoteVibroDocCount(fileService.getFileCount(StsCoreConstant.DOC_QUOTE_VIBRO, takeoff.getId()));
			takeoff.setQuoteXlsDocCount(fileService.getFileCount(StsCoreConstant.DOC_QUOTE_XLS, takeoff.getId()));
		} else {
			takeoff.setDrawingDocCount(fileService.getFileCount(StsCoreConstant.DOC_TAKEOFF_DRAWING, takeoff.getId()));
			takeoff.setTakeoffDocCount(fileService.getFileCount(StsCoreConstant.DOC_TAKEOFF, takeoff.getId()));
			takeoff.setVibroDocCount(fileService.getFileCount(StsCoreConstant.DOC_TAKEOFF_VIBRO, takeoff.getId()));
			System.err.println("takeoff.getQuoteId = " + takeoff.getQuoteId());
			takeoff.setIdStyle(StringUtils.isNotEmpty(takeoff.getQuoteId()) ? "{'color':'green'}" : "{'color':'red'}");
			takeoff.setStatusStyle(takeoff.isArchived() ? "{'background':'#FFCC33'}" : "");
		}

		return takeoff;
	}

	private Takeoff enrichTakeoffForReport(Takeoff takeoff) {
		User salesUser = userService.findUser(takeoff.getSalesPerson());
		if (salesUser != null) {
			takeoff.setSalesPersonName(salesUser.getFirstName() + "	" + salesUser.getLastName());
		}
		User user = userService.getByUserCode(takeoff.getUserCode());
		if (user != null) {
			takeoff.setUserCode(user.getUserCode());
		}
		if (takeoff.getEngineerId() != null && takeoff.getEngineerId() > 0) {
			Engineer eng = engineerService.getEngineer(takeoff.getEngineerId());
			if (eng != null) {
				takeoff.setEngineerName(eng.getName());
			}
		}
		if (takeoff.getArchitectureId() != null && takeoff.getArchitectureId() > 0) {
			Architect arc = architectService.getArchitect(takeoff.getArchitectureId());
			if (arc != null) {
				takeoff.setArchitectureName(arc.getName());
			}
		}

		return takeoff;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<Takeoff> listTakeoffsForView(int pageNumber, int pageSize, boolean quoteView) {
		int totalRecordsCount = getTotalRecords(
				"SELECT COUNT(entity.id) FROM Takeoff entity WHERE entity.archived = 'false' AND NOT FUNC('ISNULL', entity.quoteId)",
				getEntityManager());
		String queryString = "Takeoff.listTakeoffsForView";
		Query query = getEntityManager().createNamedQuery(queryString);
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<Takeoff> takeoffs = query.getResultList();
		StsResponse<Takeoff> responses = null;
		if (totalRecordsCount > 0) {
			responses = new StsResponse<>();
			responses.setTotalCount(totalRecordsCount);
			responses.setResults(initWithDetailForNewQuote(takeoffs, quoteView));
		}
		return responses;

	}

	@Override
	@Transactional
	public String makeQuote(Long takeoffId) {
		Takeoff takeoff = getTakeoff(takeoffId);
		if (takeoff != null) {
			if (takeoff.getQuoteId() != null && takeoff.getQuoteId().trim().length() > 0) {
				return "quote_already_created";
			} else {
				DateFormat df = new SimpleDateFormat("yy");

				String id = new StringBuffer("Q").append(df.format(Calendar.getInstance().getTime())).append("-")
						.append(takeoff.getId()).toString();
				takeoff.setQuoteId(id);
				takeoff.setQuoteDate(Calendar.getInstance());
				getEntityManager().merge(takeoff);
				return "quote_create_success";
			}
		}

		return "quote_create_fail";
	}

	@Override
	public Takeoff getTakeoffWithDetail(Long id) {
		Takeoff takeoff = getTakeoff(id);
		if (takeoff != null) {
			if (takeoff.getView() == null || takeoff.getView().length() <= 0) {
				takeoff.setView("takeoff");
			}
			enrichTakeoff(takeoff);
		}
		return takeoff;
	}

	@Override
	@Transactional
	public void updateWorksheetInfo(Long takeoffId, Long worksheetId, Double amount) {
		Takeoff takeoff = getTakeoff(takeoffId);
		if (!takeoff.isWsCreated()) {
			takeoff.setWsCreated(true);
		}
		if (takeoff.getWorksheetId() == null || takeoff.getWorksheetId() <= 0) {
			takeoff.setWorksheetId(worksheetId);
			takeoff.setWsDate(Calendar.getInstance());
		}

		takeoff.setAmount(amount);
		getEntityManager().merge(takeoff);
		getEntityManager().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getQuoteFollowupEmails() {
		String[] emails = null;
		String queryString = "SELECT u.email FROM USER AS u INNER JOIN USER_PERMISSION AS p ON u.ID = p.USERID AND u.ARCHIVED = 'false' AND p.PERMISSIONID = 'quote_follow'";
		List<String> results = getEntityManager().createNativeQuery(queryString).getResultList();
		if (results != null && !results.isEmpty()) {
			emails = new String[results.size()];
			int index = 0;
			for (String email : results) {
				emails[index++] = email;
			}
		}
		return emails;
	}

	@Override
	@Transactional
	public Long saveQuoteFollowup(QuoteFollowup quoteFollowup) throws Exception {
		getEntityManager().persist(quoteFollowup);
		getEntityManager().flush();
		boolean status = awacpMailService.sendQuoteFollowupMail(quoteFollowup.getTakeoffId());
		quoteFollowup.setMailSent(status);
		return quoteFollowup.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QuoteFollowup> getAllQuoteFollowups(Long takeoffId) {
		return getEntityManager().createNamedQuery("QuoteFollowup.findAll").setParameter("takeoffId", takeoffId)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Takeoff getTakeoffByQuoteId(String quoteId) {
		List<Takeoff> takeoffs = getEntityManager().createNamedQuery("Takeoff.getTakeoffByQuoteId")
				.setParameter("quoteId", quoteId).getResultList();
		takeoffs.get(0).setView("takeoff");
		return takeoffs == null || takeoffs.isEmpty() ? null : enrichTakeoff(takeoffs.get(0));
	}

	@Override
	public StsResponse<Takeoff> searchTakeoffs(Takeoff takeoff) {
		String view = takeoff.getView() != null && !takeoff.getView().isEmpty() ? takeoff.getView() : "takeoff";
		StringBuffer searchQuery = new StringBuffer("SELECT t FROM ").append(Takeoff.class.getSimpleName())
				.append(" t WHERE t.archived = 'false'");
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(t.id) FROM ").append(Takeoff.class.getSimpleName())
				.append(" t WHERE t.archived = 'false'");

		if (takeoff.getDateCreated() != null) {
			searchQuery.append(" AND FUNC('DATE', t.dateCreated) = :dateCreated ");
			countQuery.append(" AND FUNC('DATE', t.dateCreated) = :dateCreated ");
		}

		if (takeoff.getDueDate() != null) {
			searchQuery.append(" AND FUNC('DATE', t.dueDate) = :dueDate ");
			countQuery.append(" AND FUNC('DATE', t.dueDate) = :dueDate ");
		}

		if (takeoff.getTakeoffId() != null && !takeoff.getTakeoffId().isEmpty()) {
			searchQuery.append(" AND t.id =:takeoffId");
			countQuery.append(" AND t.id =:takeoffId");
		}

		if (takeoff.getQuoteId() != null && !takeoff.getQuoteId().isEmpty()) {
			searchQuery.append(" AND t.id =:quoteId");
			countQuery.append(" AND t.id =:quoteId");
		}
		if (takeoff.getJobOrderNumber() != null && !takeoff.getJobOrderNumber().isEmpty()) {
			searchQuery.append(" AND t.id =:jobOrderNumber");
			countQuery.append(" AND t.id =:jobOrderNumber");
		}

		if (takeoff.getJobName() != null && takeoff.getJobName().trim().length() > 0) {
			searchQuery.append(" AND t.id =:jobName");
			countQuery.append(" AND t.id =:jobName");

		}
		if (takeoff.getJobAddress() != null && takeoff.getJobAddress().trim().length() > 0) {
			searchQuery.append(" AND t.id =:jobAddress");
			countQuery.append(" AND t.id =:jobAddress");
		}
		if (takeoff.getEngineerId() != null) {
			searchQuery.append(" AND t.engineerId =:engineerId");
			countQuery.append(" AND t.engineerId =:engineerId");
		}
		if (takeoff.getArchitectureId() != null) {
			searchQuery.append(" AND t.architectureId =:architectureId");
			countQuery.append(" AND t.architectureId =:architectureId");
		}

		if (takeoff.getSalesPerson() != null) {
			searchQuery.append(" AND t.salesPerson =:salesPerson");
			countQuery.append(" AND t.salesPerson =:salesPerson");
		}
		if (takeoff.getUserCode() != null && !takeoff.getUserCode().isEmpty()) {
			searchQuery.append(" AND (t.createdById =:userCode OR t.updatedById =:userCode)");
			countQuery.append(" AND (t.createdById =:userCode OR t.updatedById =:userCode)");
		}
		if (takeoff.getSpecId() != null) {
			searchQuery.append(" AND t.spec.id =:specId");
			countQuery.append(" AND t.spec.id =:specId");
		}
		if (takeoff.getProjectNumber() != null && takeoff.getProjectNumber().trim().length() > 0) {
			searchQuery.append(" AND t.id =:projectNumber");
			countQuery.append(" AND t.id =:projectNumber");
		}
		if (takeoff.getVibrolayin() != null && takeoff.getVibrolayin().trim().length() > 0) {
			searchQuery.append(" AND t.id =:vibrolayin");
			countQuery.append(" AND t.id =:vibrolayin");
		}

		if (takeoff.getAmt() != null) {
			searchQuery.append(" AND t.id =:amount");
			countQuery.append(" AND t.id =:amount");
		}

		Query query = getEntityManager().createQuery(searchQuery.toString());
		Query query2 = getEntityManager().createQuery(countQuery.toString());
		if (takeoff.getDateCreated() != null) {
			query.setParameter("dateCreated", takeoff.getDateCreated().getTime(), TemporalType.DATE);
			query2.setParameter("dateCreated", takeoff.getDateCreated().getTime(), TemporalType.DATE);
		}

		if (takeoff.getDueDate() != null) {
			query.setParameter("dueDate", takeoff.getDueDate(), TemporalType.DATE);
			query2.setParameter("dueDate", takeoff.getDueDate(), TemporalType.DATE);
		}
		if (takeoff.getTakeoffId() != null && !takeoff.getTakeoffId().isEmpty()) {
			query.setParameter("takeoffId", Long.valueOf(takeoff.getTakeoffId()));
			query2.setParameter("takeoffId", Long.valueOf(takeoff.getTakeoffId()));
		}
		if (takeoff.getQuoteId() != null && !takeoff.getQuoteId().isEmpty()) {
			query.setParameter("quoteId", Long.valueOf(takeoff.getQuoteId()));
			query2.setParameter("quoteId", Long.valueOf(takeoff.getQuoteId()));
		}
		if (takeoff.getJobOrderNumber() != null && !takeoff.getJobOrderNumber().isEmpty()) {
			query.setParameter("jobOrderNumber", Long.valueOf(takeoff.getJobOrderNumber()));
			query2.setParameter("jobOrderNumber", Long.valueOf(takeoff.getJobOrderNumber()));
		}

		if (takeoff.getJobName() != null && !takeoff.getJobName().isEmpty()) {
			query.setParameter("jobName", Long.valueOf(takeoff.getJobName()));
			query2.setParameter("jobName", Long.valueOf(takeoff.getJobName()));
		}
		if (takeoff.getJobAddress() != null && !takeoff.getJobAddress().isEmpty()) {
			query.setParameter("jobAddress", Long.valueOf(takeoff.getJobAddress()));
			query2.setParameter("jobAddress", Long.valueOf(takeoff.getJobAddress()));
		}

		if (takeoff.getEngineerId() != null) {
			query.setParameter("engineerId", Long.valueOf(takeoff.getEngineerId()));
			query2.setParameter("engineerId", Long.valueOf(takeoff.getEngineerId()));
		}
		if (takeoff.getArchitectureId() != null) {
			query.setParameter("architectureId", Long.valueOf(takeoff.getArchitectureId()));
			query2.setParameter("architectureId", Long.valueOf(takeoff.getArchitectureId()));
		}

		if (takeoff.getSalesPerson() != null) {
			query.setParameter("salesPerson", Long.valueOf(takeoff.getSalesPerson()));
			query2.setParameter("salesPerson", Long.valueOf(takeoff.getSalesPerson()));
		}
		if (takeoff.getUserCode() != null && !takeoff.getUserCode().isEmpty()) {
			query.setParameter("userCode", Long.valueOf(takeoff.getUserCode()));
			query2.setParameter("userCode", Long.valueOf(takeoff.getUserCode()));
		}
		if (takeoff.getSpecId() != null) {
			query.setParameter("specId", Long.valueOf(Long.valueOf(takeoff.getSpecId())));
			query2.setParameter("specId", Long.valueOf(Long.valueOf(takeoff.getSpecId())));
		}

		if (takeoff.getProjectNumber() != null && !takeoff.getProjectNumber().isEmpty()) {
			query.setParameter("projectNumber", Long.valueOf(takeoff.getProjectNumber()));
			query2.setParameter("projectNumber", Long.valueOf(takeoff.getProjectNumber()));
		}

		if (takeoff.getVibrolayin() != null && !takeoff.getVibrolayin().isEmpty()) {
			query.setParameter("vibrolayin", Long.valueOf(takeoff.getVibrolayin()));
			query2.setParameter("vibrolayin", Long.valueOf(takeoff.getVibrolayin()));
		}

		if (takeoff.getAmt() != null) {
			query.setParameter("amount", Long.valueOf(takeoff.getAmt()));
			query2.setParameter("amount", Long.valueOf(takeoff.getAmt()));
		}

		StsResponse<Takeoff> response = listAll(takeoff.getPageNumber(), takeoff.getPageSize(), query, query2,
				Takeoff.class.getSimpleName(), "id", getEntityManager());
		if (response.getResults() != null && !response.getResults().isEmpty()) {
			for (Takeoff to : response.getResults()) {
				if ("quote".equals(view)) {
					takeoff.setView("quote");
				}
				enrichTakeoff(to);
			}
		}

		return response.getResults() == null || response.getResults().isEmpty() ? response
				: response.setResults(response.getResults());
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<Takeoff> generateTakeoffReport(Takeoff takeoff) {
		StringBuffer sb = new StringBuffer("SELECT t FROM ").append(Takeoff.class.getSimpleName())
				.append(" t WHERE t.archived = 'false'");

		StringBuffer countQuery = new StringBuffer("SELECT COUNT(t.id) FROM ").append(Takeoff.class.getSimpleName())
				.append(" t WHERE t.archived = 'false'");

		if (takeoff.getFromDate() != null && takeoff.getToDate() != null) {
			sb.append(" AND FUNC('DATE', t.dateCreated) >= :fromDate AND FUNC('DATE', t.dateCreated) <= :toDate");
			countQuery
					.append(" AND FUNC('DATE', t.dateCreated) >= :fromDate AND FUNC('DATE', t.dateCreated) <= :toDate");
		}

		if (takeoff.getFromDueDate() != null && takeoff.getToDueDate() != null) {
			sb.append(" AND FUNC('DATE', t.dueDate) >= :fromDueDate AND FUNC('DATE', t.dueDate) <= :toDueDate");
			countQuery.append(" AND FUNC('DATE', t.dueDate) >= :fromDueDate AND FUNC('DATE', t.dueDate) <= :toDueDate");
		}

		/** search form input */
		if (takeoff.getQuoteRevision() != null && !takeoff.getQuoteRevision().isEmpty()) {
			sb.append(" AND t.quoteRevision =:quoteRevision");
			countQuery.append(" AND t.quoteRevision =:quoteRevision");
		}
		if (takeoff.getDrawingDate() != null && takeoff.getDrawingDate() != null) {
			sb.append(" AND FUNC('DATE', t.drawingDate) = :drawingDate");
			countQuery.append(" AND FUNC('DATE', t.drawingDate) = :drawingDate");
		}

		if (takeoff.getRevisedDate() != null && takeoff.getRevisedDate() != null) {
			sb.append(" AND FUNC('DATE', t.revisedDate) = :revisedDate");
			countQuery.append(" AND FUNC('DATE', t.revisedDate) = :revisedDate");
		}

		if (takeoff.getDrawingReceivedFrom() != null && !takeoff.getDrawingReceivedFrom().isEmpty()) {
			sb.append(" AND LOWER(t.drawingReceivedFrom) =:drawingReceivedFrom");
			countQuery.append(" AND LOWER(t.drawingReceivedFrom) =:drawingReceivedFrom");
		}
		if (takeoff.getJobName() != null && takeoff.getJobName().trim().length() > 0) {
			sb.append(" AND LOWER(t.jobName) LIKE :jobName");
			countQuery.append(" AND LOWER(t.jobName) LIKE :jobName");

		}
		if (takeoff.getJobAddress() != null && takeoff.getJobAddress().trim().length() > 0) {
			sb.append(" AND LOWER(t.jobAddress) LIKE :jobAddress");
			countQuery.append(" AND LOWER(t.jobAddress) LIKE :jobAddress");
		}
		if (takeoff.getProjectNumber() != null && takeoff.getProjectNumber().trim().length() > 0) {
			sb.append(" AND t.projectNumber =:projectNumber");
			countQuery.append(" AND t.projectNumber =:projectNumber");
		}

		if (takeoff.getAmount() != null && takeoff.getAmount().intValue() > 0) {
			sb.append(" AND t.amount =:amount");
			countQuery.append(" AND t.amount =:amount");
		}
		if (takeoff.getJobOrderNumber() != null && !takeoff.getJobOrderNumber().isEmpty()) {
			sb.append(" AND t.jobOrderNumber =:jobOrderNumber");
			countQuery.append(" AND t.jobOrderNumber =:jobOrderNumber");
		}
		if (takeoff.getQuoteId() != null && !takeoff.getQuoteId().isEmpty()) {
			sb.append(" AND t.quoteId =:quoteId");
			countQuery.append(" AND t.quoteId =:quoteId");
		}
		/** search form input */

		if (takeoff.getYear() > 0) {
			sb.append(" AND FUNC('YEAR', t.dateCreated) =:year");
			countQuery.append(" AND FUNC('YEAR', t.dateCreated) =:year");
		}
		if (takeoff.getUserCode() != null && !takeoff.getUserCode().isEmpty()) {
			sb.append(" AND LOWER(t.userCode) =:userCode");
			countQuery.append(" AND LOWER(t.userCode) =:userCode");
		}
		if (takeoff.getSalesPerson() != null) {
			sb.append(" AND t.salesPerson =:salesPerson");
			countQuery.append(" AND t.salesPerson =:salesPerson");
		}
		if (takeoff.getEngineerId() != null) {
			sb.append(" AND t.engineerId =:engineerId");
			countQuery.append(" AND t.engineerId =:engineerId");
		}
		if (takeoff.getArchitectureId() != null) {
			sb.append(" AND t.architectureId =:architectureId");
			countQuery.append(" AND t.architectureId =:architectureId");
		}

		if (takeoff.getSpecId() != null) {
			sb.append(" AND t.spec.id =:specId");
			countQuery.append(" AND t.spec.id =:specId");
		}
		if (takeoff.getQuoteRevision() != null && !takeoff.getQuoteRevision().isEmpty()) {
			sb.append(" AND t.quoteRevision =:quoteRevision");
			countQuery.append(" AND t.quoteRevision =:quoteRevision");
		}
		Query query = getEntityManager().createQuery(sb.toString());
		Query query2 = getEntityManager().createQuery(countQuery.toString());
		if (takeoff.getFromDate() != null && takeoff.getToDate() != null) {
			query.setParameter("fromDate", takeoff.getFromDate().getTime(), TemporalType.DATE);
			query.setParameter("toDate", takeoff.getToDate().getTime(), TemporalType.DATE);

			query2.setParameter("fromDate", takeoff.getFromDate().getTime(), TemporalType.DATE);
			query2.setParameter("toDate", takeoff.getToDate().getTime(), TemporalType.DATE);
		}

		if (takeoff.getFromDueDate() != null && takeoff.getToDueDate() != null) {
			query.setParameter("fromDueDate", takeoff.getFromDueDate(), TemporalType.DATE);
			query.setParameter("toDueDate", takeoff.getToDueDate(), TemporalType.DATE);

			query2.setParameter("fromDueDate", takeoff.getFromDueDate(), TemporalType.DATE);
			query2.setParameter("toDueDate", takeoff.getToDueDate(), TemporalType.DATE);
		}
		/* search report input */
		if (takeoff.getJobName() != null && !takeoff.getJobName().isEmpty()) {
			query.setParameter("jobName", "%" + takeoff.getJobName().toLowerCase() + "%");
			query2.setParameter("jobName", "%" + takeoff.getJobName().toLowerCase() + "%");
		}
		if (takeoff.getJobAddress() != null && !takeoff.getJobAddress().isEmpty()) {
			query.setParameter("jobAddress", "%" + takeoff.getJobAddress().toLowerCase() + "%");
			query2.setParameter("jobAddress", "%" + takeoff.getJobAddress().toLowerCase() + "%");
		}
		if (takeoff.getProjectNumber() != null && !takeoff.getProjectNumber().isEmpty()) {
			query.setParameter("projectNumber", takeoff.getProjectNumber());
			query2.setParameter("projectNumber", takeoff.getProjectNumber());
		}
		if (takeoff.getDrawingDate() != null) {
			query.setParameter("drawingDate", takeoff.getDrawingDate(), TemporalType.DATE);
			query2.setParameter("drawingDate", takeoff.getDrawingDate(), TemporalType.DATE);
		}
		if (takeoff.getRevisedDate() != null) {
			query.setParameter("revisedDate", takeoff.getRevisedDate(), TemporalType.DATE);
			query2.setParameter("revisedDate", takeoff.getRevisedDate(), TemporalType.DATE);
		}
		if (takeoff.getDrawingReceivedFrom() != null && !takeoff.getDrawingReceivedFrom().isEmpty()) {
			query.setParameter("drawingReceivedFrom", takeoff.getDrawingReceivedFrom().toLowerCase());
			query2.setParameter("drawingReceivedFrom", takeoff.getDrawingReceivedFrom().toLowerCase());
		}

		if (takeoff.getAmount() != null && takeoff.getAmount().intValue() > 0) {
			query.setParameter("amount", takeoff.getAmount());
			query2.setParameter("amount", takeoff.getAmount());
		}

		if (takeoff.getJobOrderNumber() != null && !takeoff.getJobOrderNumber().isEmpty()) {
			query.setParameter("jobOrderNumber", takeoff.getJobOrderNumber());
			query2.setParameter("jobOrderNumber", takeoff.getJobOrderNumber());
		}
		if (takeoff.getQuoteId() != null && !takeoff.getQuoteId().isEmpty()) {
			query.setParameter("quoteId", takeoff.getQuoteId());
			query2.setParameter("quoteId", takeoff.getQuoteId());
		}

		/* search report input */

		if (takeoff.getQuoteRevision() != null && !takeoff.getQuoteRevision().isEmpty()) {
			query.setParameter("quoteRevision", takeoff.getQuoteRevision());
			query2.setParameter("quoteRevision", takeoff.getQuoteRevision());
		}
		if (takeoff.getYear() > 0) {
			query.setParameter("year", takeoff.getYear());
			query2.setParameter("year", takeoff.getYear());
		}
		if (takeoff.getUserCode() != null && !takeoff.getUserCode().isEmpty()) {
			query.setParameter("userCode", takeoff.getUserCode().toLowerCase());
			query2.setParameter("userCode", takeoff.getUserCode().toLowerCase());
		}
		if (takeoff.getSalesPerson() != null) {
			query.setParameter("salesPerson", takeoff.getSalesPerson());
			query2.setParameter("salesPerson", takeoff.getSalesPerson());
		}
		if (takeoff.getEngineerId() != null) {
			query.setParameter("engineerId", takeoff.getEngineerId());
			query2.setParameter("engineerId", takeoff.getEngineerId());
		}
		if (takeoff.getArchitectureId() != null) {
			query.setParameter("architectureId", takeoff.getArchitectureId());
			query2.setParameter("architectureId", takeoff.getArchitectureId());
		}
		if (takeoff.getSpecId() != null) {
			query.setParameter("specId", Long.valueOf(takeoff.getSpecId()));
			query2.setParameter("specId", Long.valueOf(takeoff.getSpecId()));
		}
		if (takeoff.getQuoteRevision() != null && !takeoff.getQuoteRevision().isEmpty()) {
			query.setParameter("quoteRevision", takeoff.getQuoteRevision().trim());
			query2.setParameter("quoteRevision", takeoff.getQuoteRevision().trim());
		}
		if (takeoff.getPageNumber() > 0 && takeoff.getPageSize() > 0) {
			query.setFirstResult(((takeoff.getPageNumber() - 1) * takeoff.getPageSize()))
					.setMaxResults(takeoff.getPageSize());
		}
		StsResponse<Takeoff> response = new StsResponse<Takeoff>();
		if (takeoff.getPageNumber() <= 1) {
			Object object = query2.getSingleResult();
			int count = 0;
			if (object != null) {
				count = (((Long) object).intValue());
			}
			response.setTotalCount(count);
		}

		List<Takeoff> results = query.getResultList();
		if (results != null && !results.isEmpty()) {
			ListIterator<Takeoff> ti = results.listIterator();
			Takeoff result = null;
			while (ti.hasNext()) {
				result = ti.next();
				Set<Bidder> bidders = null;
				if (takeoff.getBiddersIds() != null && takeoff.getBiddersIds().length > 0) {
					bidders = new HashSet<Bidder>();
					Bidder bidder = null;
					for (String id : takeoff.getBiddersIds()) {
						bidder = new Bidder();
						bidder.setId(Long.valueOf(id));
						bidders.add(bidder);
					}
					if (result.getBidders().containsAll(bidders)) {
						enrichTakeoffForReport(result);
					} else {
						ti.remove();
					}
				} else {
					enrichTakeoffForReport(result);
				}
			}
			response.setResults(results);
		}
		return response;
	}

	@Override
	@Transactional
	public void setQuotePdfGenerated(Long takeoffId, String pdfFilePath) {
		Takeoff takeoff = getTakeoff(takeoffId);
		takeoff.setPdfGenerated(true);
		takeoff.setPdfFilePath(pdfFilePath);
		this.updateTakeoff(takeoff);

	}

	@Override
	public AutoComplete autoCompleteList(String keyword, String field) {
		AutoComplete ac = new AutoComplete();
		// dateCreated, dueDate, takeoffId, quoteId, jobOrderNumber, jobName,
		// jobAddress, projectNumber
		// userCode, vibrolayin, eng, arc, sp, spec, gc, bidder
		String columnName = "";
		boolean toLower = false;
		boolean amt = false;
		StringBuffer tableName = new StringBuffer("TAKEOFF");
		if ("takeoffId".equalsIgnoreCase(field)) {
			columnName = field.toUpperCase();
			toLower = true;
		} else if ("quoteId".equalsIgnoreCase(field)) {
			columnName = field.toUpperCase();
			toLower = true;
		} else if ("jobOrderNumber".equalsIgnoreCase(field)) {
			columnName = field.toUpperCase();
			toLower = true;
		} else if ("jobName".equalsIgnoreCase(field)) {
			columnName = field.toUpperCase();
			toLower = true;
		} else if ("jobAddress".equalsIgnoreCase(field)) {
			columnName = field.toUpperCase();
			toLower = true;
		} else if ("projectNumber".equalsIgnoreCase(field)) {
			columnName = field.toUpperCase();
			toLower = true;
		} else if ("userCode".equalsIgnoreCase(field)) {
			columnName = field.toUpperCase();
			toLower = true;
		} else if ("vibrolayin".equalsIgnoreCase(field)) {
			columnName = field.toUpperCase();
			toLower = true;
		} else if ("engineerId".equalsIgnoreCase(field)) {
			tableName = new StringBuffer("ENGINEER");
			columnName = "NAME";
			toLower = true;
		} else if ("contractorsIds".equalsIgnoreCase(field)) {
			tableName = new StringBuffer("GENERALCONTRACTOR");
			columnName = "NAME";
			toLower = true;
		} else if ("architectureId".equalsIgnoreCase(field)) {
			tableName = new StringBuffer("ARCHITECT");
			columnName = "NAME";
			toLower = true;
		} else if ("salesPerson".equalsIgnoreCase(field)) {
			tableName = new StringBuffer("USER");
			columnName = "FIRSTNAME";
			toLower = true;
		} else if ("specId".equalsIgnoreCase(field)) {
			tableName = new StringBuffer("SPEC");
			columnName = "DETAIL";
			toLower = true;
		} else if ("biddersIds".equalsIgnoreCase(field)) {
			tableName = new StringBuffer("BIDDER");
			columnName = "NAME";
			toLower = true;
		} else if ("amt".equalsIgnoreCase(field)) {
			columnName = "AMOUNT";
			amt = true;
		}
		StringBuffer query = new StringBuffer("SELECT ID, ").append(columnName).append(" AS FIELD FROM ")
				.append(tableName).append(" WHERE ");
		if (toLower) {
			query.append("LOWER(").append(columnName).append(") LIKE '%").append(keyword.trim().toLowerCase())
					.append("%'");
		} else {
			if (amt) {
				query.append(columnName).append(" = ").append(keyword.trim());
			} else {
				query.append(columnName).append(" LIKE '%").append(keyword.trim()).append("%'");
			}
		}
		System.err.println("QUERY == " + query.toString());
		@SuppressWarnings("unchecked")
		List<Object[]> results = getEntityManager().createNativeQuery(query.toString()).getResultList();
		if (results != null && !results.isEmpty()) {
			for (Object[] result : results) {
				ac.getResult().add("{\"value\": \"" + result[0].toString() + "\", \"label\": \"" + result[1].toString()
						+ "\", \"field\": \"" + field + "\"}");
			}
		}
		return ac;
	}

	@Override
	public int totalRecordsForTheYear(String recordType) {
		Calendar date = Calendar.getInstance();
		StringBuffer countQuery = new StringBuffer("SELECT COUNT(t.id) FROM ").append(Takeoff.class.getSimpleName())
				.append(" t WHERE t.archived = 'false' AND FUNC('YEAR', t.dateCreated) = :aYear");
		if ("not_quoted".equalsIgnoreCase(recordType)) {
			countQuery = new StringBuffer("SELECT COUNT(t.id) FROM ").append(Takeoff.class.getSimpleName()).append(
					" t WHERE t.archived = 'false' AND FUNC('YEAR', t.dateCreated) = :aYear AND FUNC('ISNULL', t.quoteId)");
		} else if ("quoted".equalsIgnoreCase(recordType)) {
			countQuery = new StringBuffer("SELECT COUNT(t.id) FROM ").append(Takeoff.class.getSimpleName()).append(
					" t WHERE t.archived = 'false' AND FUNC('YEAR', t.dateCreated) = :aYear AND NOT FUNC('ISNULL', t.quoteId)");
		}

		Query query = getEntityManager().createQuery(countQuery.toString());

		query.setParameter("aYear", date.get(Calendar.YEAR));
		Object object = query.getSingleResult();
		int count = 0;
		if (object != null) {
			count = (((Long) object).intValue());
		}
		return count;
	}

}

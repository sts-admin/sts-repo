package com.awacp.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.Architect;
import com.awacp.entity.Bidder;
import com.awacp.entity.Engineer;
import com.awacp.entity.GeneralContractor;
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
import com.sts.core.dto.StsResponse;
import com.sts.core.entity.User;
import com.sts.core.service.FileService;
import com.sts.core.service.UserService;
import com.sts.core.service.impl.CommonServiceImpl;

public class TakeoffServiceImpl extends CommonServiceImpl<Takeoff>implements TakeoffService {

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
	public StsResponse<Takeoff> listTakeoffs(int pageNumber, int pageSize) {
		StsResponse<Takeoff> response = listAll(pageNumber, pageSize, Takeoff.class.getSimpleName(),
				getEntityManager());
		return response.getResults() == null || response.getResults().isEmpty() ? response
				: response.setResults(initWithDetail(response.getResults()));
	}

	private List<Takeoff> initWithDetail(List<Takeoff> takeoffs) {
		if (takeoffs == null || takeoffs.isEmpty())
			return null;

		for (Takeoff takeoff : takeoffs) {
			initWithDetail(takeoff);
			takeoff.setDrawingDocCount(fileService.getFileCount(StsCoreConstant.DOC_TAKEOFF_DRAWING, takeoff.getId()));
			takeoff.setTakeoffDocCount(fileService.getFileCount(StsCoreConstant.DOC_TAKEOFF, takeoff.getId()));
			takeoff.setVibroDocCount(fileService.getFileCount(StsCoreConstant.DOC_TAKEOFF_VIBRO, takeoff.getId()));
			takeoff.setIdStyle(StringUtils.isNotEmpty(takeoff.getQuoteId()) ? "{'color':'green'}" : "{'color':'red'}");
			takeoff.setStatusStyle(takeoff.isArchived() ? "{'background':'#FFCC33'}" : "");
		}
		return takeoffs;
	}

	private void initWithDetail(Takeoff takeoff) {
		if (takeoff == null)
			return;
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
	}

	@Override
	public Takeoff getTakeoff(Long takeoffId) {
		Takeoff takeoff = getEntityManager().find(Takeoff.class, takeoffId);
		if (takeoff != null && takeoff.getSpec() != null) {
			takeoff.setSpecId(String.valueOf(takeoff.getSpec().getId()));
		}
		return takeoff;
	}

	@Override
	@Transactional
	public Takeoff saveTakeoff(Takeoff takeoff) throws Exception {
		String[] biddersIds = takeoff.getBiddersIds();
		User user = userService.getUserByUserNameOrEmail(takeoff.getUserNameOrEmail());
		if (StringUtils.isNotEmpty(takeoff.getArchitectureName())) { // NEW
			Architect architect = new Architect();
			architect.setCreatedByUserCode(takeoff.getCreatedByUserCode());
			architect.setName(takeoff.getArchitectureName());
			architect.setSalesPerson(user.getId());
			getEntityManager().persist(architect);
			getEntityManager().flush();
			takeoff.setArchitectureId(architect.getId());
		}
		if (StringUtils.isNotEmpty(takeoff.getEngineerName())) { // NEW
			Engineer engineer = new Engineer();
			engineer.setCreatedByUserCode(takeoff.getCreatedByUserCode());
			engineer.setName(takeoff.getEngineerName());
			engineer.setSalesPerson(user.getId());
			getEntityManager().persist(engineer);
			getEntityManager().flush();
			takeoff.setEngineerId(engineer.getId());
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
		if (takeoff.getId() != null && takeoff.getId() > 0) { // update
			getEntityManager().merge(takeoff);
		} else {
			getEntityManager().persist(takeoff);
			getEntityManager().flush();
			DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2
														// digits

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
		takeoff = getEntityManager().merge(takeoff);
		getEntityManager().flush();
		return takeoff;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getNewTakeoffEmails(Long takeoffId) {
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
	public List<String> listTakeoffIds(String keword) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listQuoteIds(String keword) {
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
			responses.setResults(initWithDetailForNewQuote(takeoffs));
		}
		return responses;
	}

	private List<Takeoff> initWithDetailForNewQuote(List<Takeoff> takeoffs) {
		if (takeoffs == null || takeoffs.isEmpty()) {
			return null;
		}

		for (Takeoff takeoff : takeoffs) {
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
		}
		return takeoffs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StsResponse<Takeoff> listTakeoffsForView(int pageNumber, int pageSize) {
		int totalRecordsCount = getTotalRecords(
				"SELECT COUNT(entity.id) FROM Takeoff entity WHERE entity.archived = 'false' AND NOT FUNC('ISNULL', entity.quoteId)",
				getEntityManager());
		Query query = getEntityManager().createNamedQuery("Takeoff.listTakeoffsForView");
		if (pageNumber > 0 && pageSize > 0) {
			query.setFirstResult(((pageNumber - 1) * pageSize)).setMaxResults(pageSize);
		}
		List<Takeoff> takeoffs = query.getResultList();
		StsResponse<Takeoff> responses = null;
		if (totalRecordsCount > 0) {
			responses = new StsResponse<>();
			responses.setTotalCount(totalRecordsCount);
			responses.setResults(initWithDetailForNewQuote(takeoffs));
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
			initWithDetail(takeoff);
		}
		return takeoff;
	}

	@Override
	@Transactional
	public void setWorksheetCreated(Long takeoffId, Long worksheetId) {
		Takeoff takeoff = getTakeoff(takeoffId);
		takeoff.setWsCreated(true);
		takeoff.setWsDate(Calendar.getInstance());
		takeoff.setWorksheetId(worksheetId);
		getEntityManager().merge(takeoff);
		getEntityManager().flush();

	}

}

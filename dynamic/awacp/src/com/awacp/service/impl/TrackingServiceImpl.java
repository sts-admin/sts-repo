package com.awacp.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.awacp.entity.OrderBook;
import com.awacp.entity.ShipmentStatus;
import com.awacp.entity.Tracking;
import com.awacp.entity.Trucker;
import com.awacp.service.TrackingService;
import com.awacp.util.PodPdfGenerator;
import com.awacp.util.UpsTrackingUtil;
import com.sts.core.config.AppPropConfig;
import com.sts.core.constant.ShipmentSource;
import com.sts.core.constant.StsCoreConstant;
import com.sts.core.dto.StsResponse;
import com.sts.core.exception.UnknownServiceProvider;
import com.sts.core.service.impl.CommonServiceImpl;

public class TrackingServiceImpl extends CommonServiceImpl<Tracking> implements TrackingService {
	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	@Transactional
	public String delete(Long id) {
		Tracking entity = getTracking(id);
		if (entity != null) {
			entity.setArchived(true);
			getEntityManager().merge(entity);
			getEntityManager().flush();
			return "success";
		}
		return "fail";
	}

	@Override
	@Transactional
	public Tracking updateTracking(Tracking tracking) throws Exception {
		getEntityManager().merge(tracking);
		getEntityManager().flush();
		updateShipment(tracking.getId());
		return getTracking(tracking.getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Tracking saveTracking(Tracking tracking) throws Exception {
		if (tracking.getId() != null) {
			return updateTracking(tracking);
		}
		List<OrderBook> obs = getEntityManager().createNamedQuery("OrderBook.getByNumber")
				.setParameter("orderBookNumber", tracking.getOrderBookNumber()).getResultList();

		tracking.setOrderBookId(obs.get(0).getId());
		List<Tracking> eTrackings = getEntityManager().createNamedQuery("Tracking.findByOrder")
				.setParameter("orderBookId", obs.get(0).getId()).getResultList();
		if (eTrackings != null && !eTrackings.isEmpty()) {
			Tracking eTracking = eTrackings.get(0);
			if (tracking.getTruckerOne() != null) {
				eTracking.setTruckerOne(tracking.getTruckerOne());
				eTracking.setTrackingNumberOne(tracking.getTrackingNumberOne());
				eTracking.setFreightOne(tracking.getFreightOne());
			}
			if (tracking.getTruckerTwo() != null) {
				eTracking.setTruckerTwo(tracking.getTruckerTwo());
				eTracking.setTrackingNumberTwo(tracking.getTrackingNumberTwo());
				eTracking.setFreightTwo(tracking.getFreightTwo());
			}
			if (tracking.getTruckerThree() != null) {
				eTracking.setTruckerThree(tracking.getTruckerThree());
				eTracking.setTrackingNumberThree(tracking.getTrackingNumberThree());
				eTracking.setFreightThree(tracking.getFreightThree());
			}
			getEntityManager().merge(tracking);
		} else {
			getEntityManager().persist(tracking);
		}

		getEntityManager().flush();
		updateShipment(tracking.getId());
		return getTracking(tracking.getId());
	}

	@Override
	public Tracking getTracking(Long id) {
		return getEntityManager().find(Tracking.class, id);
	}

	@Override
	public StsResponse<Tracking> listTrackings(int pageNumber, int pageSize) {
		StsResponse<Tracking> results = listAll(pageNumber, pageSize, Tracking.class.getSimpleName(),
				getEntityManager());

		return results;
	}

	@Override
	public ShipmentStatus trackShipment(String trackingNumber, String ss) throws UnknownServiceProvider {
		ShipmentStatus status = null;
		String source = ShipmentSource.getByName(ss);
		if (source == null) {
			throw new UnknownServiceProvider(
					"Unable to track shipment, reason: unknown_service_provider. Given TRACKING-NUMBER IS:"
							+ trackingNumber + ", Given service provider is:" + ss);
		}
		if (ss.equalsIgnoreCase(ShipmentSource.UPS.name())) {
			try {
				status = UpsTrackingUtil.track(trackingNumber);
			} catch (Exception e) {
				e.printStackTrace();
				status.setConnectionStatus(StsCoreConstant.SHIPMENT_TRACKING_ERROR);
			}
		} else {
			throw new UnknownServiceProvider(
					"Unable to track shipment, reason: unknown_service_provider. Given TRACKING-NUMBER IS:"
							+ trackingNumber + ", Given service provider is:" + ss);
		}
		return status;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void updateShipment(Long trackingId) throws Exception {
		Tracking tracking = getEntityManager().find(Tracking.class, trackingId);
		ShipmentStatus t1, t2, t3;
		boolean tOneDelivered, tTwoDelivered, tThreeDelivered;
		tOneDelivered = tTwoDelivered = tThreeDelivered = false;
		if (tracking.getTruckerOne() != null && tracking.getTrackingNumberOne() != null
				&& !tracking.isTruckerOneDelivered()) {
			Trucker tOne = getEntityManager().find(Trucker.class, tracking.getTruckerOne().getId());

			t1 = trackShipment(tracking.getTrackingNumberOne(), tOne.getCode());

			if (t1 != null) {
				List<ShipmentStatus> sp1s = getEntityManager()
						.createNamedQuery("ShipmentStatus.findByTruckerIdAndTrackingNumber")
						.setParameter("truckerId", tOne.getId())
						.setParameter("trackingNumber", tracking.getTrackingNumberOne()).getResultList();
				if (sp1s != null && !sp1s.isEmpty()) {
					ShipmentStatus ess = sp1s.get(0);
					ess.setTruckerId(tracking.getTruckerOne().getId());
					ess.setTrackingId(tracking.getId());
					ess.setTrackingNumber(tracking.getTrackingNumberOne());
					ess.setBilledOn(t1.getBilledOn());
					ess.setCurrStatusText(t1.getCurrStatusText());
					ess.setDelivered(t1.isDelivered());
					ess.setDeliveredOn(t1.getBilledOn());
					ess.setDeliveredTo(t1.getDeliveredTo());
					ess.setLeftAt(t1.getLeftAt());
					ess.setServiceProvider(t1.getServiceProvider());
					ess.setOrderBookId(tracking.getOrderBookId());
					getEntityManager().merge(ess);
				} else {
					t1.setOrderBookId(tracking.getOrderBookId());
					t1.setTruckerId(tracking.getTruckerOne().getId());
					t1.setTrackingId(tracking.getId());
					t1.setTrackingNumber(tracking.getTrackingNumberOne());
					getEntityManager().persist(t1);
				}
				if (t1.isDelivered()) {
					tOneDelivered = true;
				}
				if (t1.isPodGenerated()) {
					t1.setProviderLogo("pod_img_ups.gif");
					String filePath = AppPropConfig.acResourceWriteDir + "/" + t1.getTrackingNumber() + ".pdf";
					String logoPath = AppPropConfig.acImageLocalUrl + "/" + t1.getProviderLogo();
					new PodPdfGenerator(filePath, logoPath, t1).generate();
				}
			}

		}
		if (tracking.getTruckerTwo() != null && tracking.getTrackingNumberTwo() != null
				&& !tracking.isTruckerTwoDelivered()) {
			Trucker tTwo = getEntityManager().find(Trucker.class, tracking.getTruckerTwo().getId());

			t2 = trackShipment(tracking.getTrackingNumberTwo(), tTwo.getCode());
			if (t2 != null) {
				List<ShipmentStatus> sp2s = getEntityManager()
						.createNamedQuery("ShipmentStatus.findByTruckerIdAndTrackingNumber")
						.setParameter("truckerId", tTwo.getId())
						.setParameter("trackingNumber", tracking.getTrackingNumberTwo()).getResultList();
				if (sp2s != null && !sp2s.isEmpty()) {
					ShipmentStatus ess2 = sp2s.get(0);
					ess2.setTruckerId(tracking.getTruckerTwo().getId());
					ess2.setTrackingId(tracking.getId());
					ess2.setTrackingNumber(tracking.getTrackingNumberTwo());
					ess2.setBilledOn(t2.getBilledOn());
					ess2.setCurrStatusText(t2.getCurrStatusText());
					ess2.setDelivered(t2.isDelivered());
					ess2.setDeliveredOn(t2.getBilledOn());
					ess2.setDeliveredTo(t2.getDeliveredTo());
					ess2.setLeftAt(t2.getLeftAt());
					ess2.setServiceProvider(t2.getServiceProvider());
					ess2.setOrderBookId(tracking.getOrderBookId());
					getEntityManager().merge(ess2);
				} else {
					t2.setOrderBookId(tracking.getOrderBookId());
					t2.setTruckerId(tracking.getTruckerTwo().getId());
					t2.setTrackingId(tracking.getId());
					t2.setTrackingNumber(tracking.getTrackingNumberTwo());
					getEntityManager().persist(t2);
				}
				if (t2.isDelivered()) {
					tTwoDelivered = true;
				}
			}

		}
		if (tracking.getTruckerThree() != null && tracking.getTrackingNumberThree() != null
				&& !tracking.isTruckerThreeDelivered()) {
			Trucker tThree = getEntityManager().find(Trucker.class, tracking.getTruckerThree().getId());

			t3 = trackShipment(tracking.getTrackingNumberThree(), tThree.getCode());
			if (t3 != null) {
				List<ShipmentStatus> sp3s = getEntityManager()
						.createNamedQuery("ShipmentStatus.findByTruckerIdAndTrackingNumber")
						.setParameter("truckerId", tThree.getId())
						.setParameter("trackingNumber", tracking.getTrackingNumberThree()).getResultList();

				if (sp3s != null && !sp3s.isEmpty()) {
					ShipmentStatus ess3 = sp3s.get(0);
					ess3.setTruckerId(tracking.getTruckerThree().getId());
					ess3.setTrackingId(tracking.getId());
					ess3.setTrackingNumber(tracking.getTrackingNumberThree());
					ess3.setBilledOn(t3.getBilledOn());
					ess3.setCurrStatusText(t3.getCurrStatusText());
					ess3.setDelivered(t3.isDelivered());
					ess3.setDeliveredOn(t3.getBilledOn());
					ess3.setDeliveredTo(t3.getDeliveredTo());
					ess3.setLeftAt(t3.getLeftAt());
					ess3.setServiceProvider(t3.getServiceProvider());
					ess3.setOrderBookId(tracking.getOrderBookId());
					getEntityManager().merge(ess3);
				} else {
					t3.setOrderBookId(tracking.getOrderBookId());
					t3.setTruckerId(tracking.getTruckerThree().getId());
					t3.setTrackingId(tracking.getId());
					t3.setTrackingNumber(tracking.getTrackingNumberThree());
					getEntityManager().persist(t3);
				}

				if (t3.isDelivered()) {
					tThreeDelivered = true;
				}
			}

		}

		if (tOneDelivered || tTwoDelivered || tThreeDelivered) {
			boolean hasUpdate = false;
			if (tOneDelivered) {
				tracking.setTruckerOneDelivered(true);
				hasUpdate = true;
			}
			if (tTwoDelivered) {
				tracking.setTruckerTwoDelivered(true);
				hasUpdate = true;
			}
			if (tThreeDelivered) {
				tracking.setTruckerThreeDelivered(true);
				hasUpdate = true;
			}
			if (hasUpdate) {
				getEntityManager().merge(tracking);
			}
			OrderBook ob = getEntityManager().find(OrderBook.class, tracking.getOrderBookId());
			if (tOneDelivered && tTwoDelivered && tThreeDelivered) {
				ob.setDelivered(true);
			} else if (tOneDelivered && tracking.getTruckerTwo() == null && tracking.getTruckerThree() == null) {
				ob.setDelivered(true);
			} else if (tOneDelivered && tTwoDelivered && tracking.getTruckerThree() == null) {
				ob.setDelivered(true);
			} else {
				ob.setDelivered(false);
			}
			getEntityManager().merge(ob);
		}
		getEntityManager().flush();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Tracking getTrackingByOrderNumber(Long orderBookId) {
		List<Tracking> eTrackings = getEntityManager().createNamedQuery("Tracking.findByOrder")
				.setParameter("orderBookId", orderBookId).getResultList();
		return eTrackings == null || eTrackings.isEmpty() ? null : eTrackings.get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ShipmentStatus shipmentStatus(Long orderBookId) {
		List<ShipmentStatus> sss = getEntityManager().createNamedQuery("ShipmentStatus.findByOrderBook")
				.setParameter("orderBookId", orderBookId).getResultList();
		ShipmentStatus ss = null;

		if (sss != null && !sss.isEmpty()) {
			ss = sss.get(0);
			ss.setPodUrl(AppPropConfig.acBaseUrl + "/" + AppPropConfig.acResourceDir + "/" + ss.getTrackingNumber()
					+ ".pdf");
		}
		return ss;
	}
}
